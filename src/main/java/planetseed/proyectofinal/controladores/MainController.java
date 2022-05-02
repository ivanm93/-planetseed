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
import planetseed.proyectofinal.servicios.UsuarioServicio;

    @Controller
@RequestMapping("/") //localhost:8080/
public class MainController {
   
    @Autowired
    private UsuarioServicio usuarioServicio;
        
    @GetMapping("/")
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
    
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        try{
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.put("usuario", usuario);
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
        }
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "perfil.html";
    } 
   
    @PostMapping("/perfil/editar")
    public String editar(ModelMap modelo, RedirectAttributes redirectAttributes, @RequestParam String id, 
            @RequestParam(required = false) String nombre, @RequestParam(required = false)String apellido, 
            @RequestParam(required = false)Integer edad, @RequestParam(required = false)String email, 
            @RequestParam(required = false) String password, @RequestParam(required = false)MultipartFile archivo) {        
        try {           
            usuarioServicio.editar(id, nombre, apellido, edad, email, password, archivo);               
            modelo.put("exito","Se ha editado el perfil");
            redirectAttributes.addFlashAttribute("exito", "Usuario editado con éxito");
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }      
        return "redirect:/perfil/editar";
    }
    
    @GetMapping("/contenido")
    public String Contenido(){
        return "contenido.html";
    }
    
    @GetMapping("/resumen")
     public String Resumen(ModelMap modelo, @RequestParam(required = false) Integer valor){
        modelo.put("valor", valor);
        
        return "resumen.html";
    }
    
    @GetMapping("/cuestionario")
    public String Cuestionario(){
        return "cuestionario.html";
    }
    
     @GetMapping("/editarusuario")
    public String EditarUsuario(){
        return "editarusuario.html";
    }
    
    @GetMapping("/comunidad")
    public String comunidad(ModelMap modelo){
        List<Usuario> listaUsuarios = usuarioServicio.findAll();
        modelo.addAttribute("listaUsuarios", listaUsuarios);
        return "comunidad.html";
    }
    
    
}
