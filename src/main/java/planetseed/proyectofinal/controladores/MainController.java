/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetseed.proyectofinal.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

    @Controller
@RequestMapping("/") //localhost:8080/
public class MainController {
    
    @GetMapping("/")
    public String Login(@RequestParam(required = false) String logout){
        return "login.html";
    }
    
    @GetMapping("/registro")
    public String Registro(){
        return "registro.html";
    }
    
       @GetMapping("/perfil")
    public String Perfil(){
        return "perfil.html";
    }
    
       @GetMapping("/contenido")
    public String Contenido(){
        return "contenido.html";
    }
    
       @GetMapping("/resumen")
     public String Resumen(){
        return "resumen.html";
    }
    
         @GetMapping("/cuestionario")
    public String Cuestionario(){
        return "cuestionario.html";
    }
    
         @GetMapping("/arbol")
    public String Arbol(){
        return "arbol.html";
    }
}
