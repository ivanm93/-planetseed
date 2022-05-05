/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetseed.proyectofinal.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import planetseed.proyectofinal.entidades.Arbol;
import planetseed.proyectofinal.entidades.Imagen;
import planetseed.proyectofinal.entidades.Pregunta;
import planetseed.proyectofinal.entidades.Usuario;
import planetseed.proyectofinal.enumeraciones.Role;
import planetseed.proyectofinal.enumeraciones.Tipo;
import planetseed.proyectofinal.errores.ErrorServicio;
import planetseed.proyectofinal.repositorios.UsuarioRepo;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private ImagenServicio imagenServicio;
    
        @Autowired
    private ArbolServicio arbolservicio;
               
        @Autowired
    private PreguntaServicio preguntaservicio;

    @Transactional
    public Usuario crear(String nombre, String apellido, Integer edad, String email,
            String password, MultipartFile archivo) throws ErrorServicio {
   
        validar(nombre, apellido, edad, email, password);      //validar datos

        Usuario u = new Usuario(); //creo un nuevo usuario para setear

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //codifico contraseña
        
        Imagen imagen = imagenServicio.guardar(archivo);  //creo una imagen

        Arbol arbol = new Arbol(); //Crear objeto de arbol vacio para un usuario nuevo 
        arbolservicio.crear(arbol);
        
      //  List<Pregunta> preguntas = new ArrayList();
        // preguntas.add(preguntaservicio.crear(1)); 
       // u.setPregunta(preguntas);
       
       //seteo todos los datos del usuario
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setEdad(edad);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setImagen(imagen);
        u.setRole(Role.USER);
        u.setPuntos(0);
        u.setAlta(true);
        u.setDescripcion("sin descripción");
        u.setArbol(arbol);
        
        return usuarioRepo.save(u);
    }

    @Transactional
    public Usuario editar(String id, String nombre, String apellido, Integer edad,
           String descripcion, MultipartFile archivo) throws ErrorServicio {
        
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario u = respuesta.get();

            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setEdad(edad);
            u.setDescripcion(descripcion);

            Imagen imagen = imagenServicio.guardar(archivo);
            u.setImagen(imagen);
       
            return usuarioRepo.save(u);           
        } else {
            throw new ErrorServicio("No se ha encontrado el usuario");
        }
    }

     @Transactional
    public Usuario buscarPorId(String id) throws ErrorServicio {
        
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario u = respuesta.get();

       return u;             
        } else {
            throw new ErrorServicio("No se ha encontrado el usuario");
        }
    }
    
    @Transactional
    public void darBaja(String id) throws Exception {
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario u = respuesta.get();
            u.setAlta(false);
            usuarioRepo.save(u);
        } else {
            throw new Exception("No se ha encontrado el usuario");
        }
    }
    
     @Transactional
    public void sumar(String id) throws Exception {
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario u = respuesta.get();
            u.setPuntos(u.getPuntos()+1);
            usuarioRepo.save(u);
        } else {
            throw new Exception("No se ha encontrado el usuario");
        }
    }

    @Transactional
    public void eliminar(String id) throws Exception {
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario u = respuesta.get();
            usuarioRepo.delete(u);
        } else {
            throw new Exception("No se ha encontrado el usuario");
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepo.buscarPorEmail(email);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepo.findAll();
    }

    public void validar(String nombre, String apellido, Integer edad, String email,
            String password) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre es inválido");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido es inválido");
        }
        if (edad == null || edad < 1) {
            throw new ErrorServicio("La edad es inválida");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("El mail es inválido");
        }
        if (password == null || password.isEmpty() || password.length() < 3) {
            throw new ErrorServicio("La contraseña es inválida");
        }
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
