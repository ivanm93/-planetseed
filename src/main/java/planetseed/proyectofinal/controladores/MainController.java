/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetseed.proyectofinal.controladores;

import static java.lang.System.console;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import planetseed.proyectofinal.entidades.Usuario;
import planetseed.proyectofinal.errores.ErrorServicio;
import planetseed.proyectofinal.servicios.ArbolServicio;
import planetseed.proyectofinal.servicios.UsuarioServicio;

    @Controller
@RequestMapping("/") //localhost:8080/
public class MainController {
   
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private ArbolServicio arbolservicio;
          
         @GetMapping("/")
    public String Redirecciono(){
        return "login.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, 
            @RequestParam(required = false) String logout, @RequestParam(required = false) String email, 
            @RequestParam(required = false) String password, ModelMap model){
        if (error != null) {
            model.put("error", "Usuario o contraseña incorrectos");      
            model.put("email", email);
            model.put("password", password);  
            return "login.html";
        } else if (logout != null) {
            model.put("logout","Desconectado correctamente");
            return "login.html";
        } else {           
            return "login.html";
        }     
    }
     @GetMapping("/registro")
    public String Registro(){
        return "registro.html";
    }
    @PostMapping("/registro")
    public String registro(ModelMap modelo, @RequestParam(required = false) String nombre, 
            @RequestParam(required = false)String apellido, @RequestParam(required = false)Integer edad, 
            @RequestParam(required = false)String email, @RequestParam(required = false) String password, 
            @RequestParam(required = false)MultipartFile archivo){
        
        try {
            usuarioServicio.crear(nombre, apellido, edad, email, password, archivo);
            modelo.put("exito", "Se ha registrado correctamente");
            return "registro.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("edad", edad);
            modelo.put("email", email);
            modelo.put("password", password);
            return "registro.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        try{
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");     
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
        }
   
        return "perfil.html";
    } 
       
     @GetMapping("/editarusuario")
    public String EditarUsuario(ModelMap modelo, HttpSession session){
        try{
            
          Usuario usuario = (Usuario) session.getAttribute("usuariosession");
          usuario.getPassword();
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        }catch (Exception ex) {
            modelo.put("error", ex.getMessage());
        }
        return "editarusuario.html";
    }
    
    @PostMapping("/editarusuario")
    public String editar(ModelMap modelo, RedirectAttributes redirectAttributes, @RequestParam String id, 
            @RequestParam(required = false) String nombre, @RequestParam(required = false)String apellido, 
            @RequestParam(required = false)Integer edad, @RequestParam(required = false)String descripcion,
            @RequestParam(required = false)MultipartFile archivo) {        
        try {           
            usuarioServicio.editar(id, nombre, apellido, edad, descripcion, archivo);               
            modelo.put("exito","Se ha editado el perfil");
            redirectAttributes.addFlashAttribute("exito", "Usuario editado con éxito");
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }      
        return "redirect:/editarusuario";
    }
    
        @PostMapping("/editararbol")
    public String editarArbol(ModelMap modelo, RedirectAttributes redirectAttributes, @RequestParam String idArbol, 
            @RequestParam(required = false) String nombreArbol) {        
        try {           
            arbolservicio.editar(idArbol, nombreArbol);
            modelo.put("check","Se ha editado el Nombre");
            redirectAttributes.addFlashAttribute("exito", "Nombre del arbol editado exitosamente");
        } catch (ErrorServicio ex) {
            modelo.put("fall", ex.getMessage());
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }      
        return "redirect:/editarusuario";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/contenido")
    public String Contenido(ModelMap modelo, HttpSession session ){
           Usuario usuario = (Usuario) session.getAttribute("usuariosession");     
        try {
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        } catch (ErrorServicio ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "contenido.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/resumen")
     public String Resumen(ModelMap modelo, @RequestParam(required = false) Integer valor, HttpSession session ){
                  Usuario usuario = (Usuario) session.getAttribute("usuariosession");     
        try {
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        } catch (ErrorServicio ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.put("valor", valor);
        
        return "resumen.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/cuestionario")
    public String Cuestionario(ModelMap modelo, @RequestParam(required = false) Integer question, HttpSession session ){
           Usuario usuario = (Usuario) session.getAttribute("usuariosession");     
        try {
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        } catch (ErrorServicio ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
      modelo.put("question", question);

        return "cuestionario.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/comunidad")
    public String comunidad(ModelMap modelo, HttpSession session ) throws ErrorServicio{
           Usuario usuario = (Usuario) session.getAttribute("usuariosession");     
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        List<Usuario> listaUsuarios = usuarioServicio.findAll();
        modelo.addAttribute("listaUsuarios", listaUsuarios);
        return "comunidad.html";
    }
    
    
}
