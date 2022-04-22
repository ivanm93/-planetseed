/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package planetseed.proyectofinal.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import planetseed.proyectofinal.entidades.Imagen;
import planetseed.proyectofinal.entidades.Usuario;
import planetseed.proyectofinal.enumeraciones.Role;
import planetseed.proyectofinal.errores.ErrorServicio;
import planetseed.proyectofinal.repositorios.UsuarioRepo;


@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepo usuarioRepo;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public Usuario crear(String nombre, String apellido, Integer edad, String email, 
            String password, MultipartFile archivo) throws ErrorServicio {

//        validar();

        Usuario u = new Usuario();
        
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setEdad(edad);
        u.setEmail(email);
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        u.setPassword(encoder.encode(password));
        
        Imagen imagen = imagenServicio.guardar(archivo);
        u.setImagen(imagen);
        
        u.setRole(Role.USER);
        
        u.setPuntos(0);
        
        u.setAlta(true);

        return usuarioRepo.save(u);
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepo.buscarPorEmail(email);
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = buscarPorEmail(email);
        if (u != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + u.getRole());
            permisos.add(p1);
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", u);

            User user = new User(u.getEmail(), u.getPassword(), permisos);
            return user;

        } else {
            return null;
        }
    }

    
}
