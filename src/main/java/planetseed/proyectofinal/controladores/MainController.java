/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetseed.proyectofinal.controladores;

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

    
    /*PERFIL Y CONTENIDO*/
    @GetMapping("/")
    public String Redirecciono() {
        return "login.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout, @RequestParam(required = false) String email,
            @RequestParam(required = false) String password, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o contraseña incorrectos");
            model.put("email", email);
            model.put("password", password);
            return "login.html";
        } else if (logout != null) {
            model.put("logout", "Desconectado correctamente");
            return "login.html";
        } else {
            return "login.html";
        }
    }

    @GetMapping("/registro")
    public String Registro() {
        return "registro.html";
    }
    
        @PostMapping("/registro")
    public String registro(ModelMap modelo, @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido, @RequestParam(required = false) Integer edad,
            @RequestParam(required = false) String email, @RequestParam(required = false) String password) {

        try {
            usuarioServicio.crear(nombre, apellido, edad, email, password);
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
    
    /*PERFIL; CONTENIDO;COMUNIDAD*/
        @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
        }

        return "perfil.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/contenido")
    public String Contenido(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        } catch (ErrorServicio ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "contenido.html";
    }
    
        @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/comunidad")
    public String comunidad(ModelMap modelo, HttpSession session) throws ErrorServicio {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        List<Usuario> listaUsuarios = usuarioServicio.findAll();
        modelo.addAttribute("listaUsuarios", listaUsuarios);
        return "comunidad.html";
    }
    
    /*PERFILES;CUESTIONARIO; RESUMEN*/
   @GetMapping("/perfiles/{id}")
    public String Perfiles(ModelMap modelo, @PathVariable(name = "id") String id) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            modelo.put("usuario", usuario);
        } catch (ErrorServicio ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "perfiles.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/cuestionario")
    public String Cuestionario(ModelMap modelo, @RequestParam(required = false) Integer question, HttpSession session) {
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
    @GetMapping("/resumen")
    public String Resumen(ModelMap modelo, @RequestParam(required = false) Integer valor, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
        } catch (ErrorServicio ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.put("valor", valor);
        return "resumen.html";
    }
    
    
    /*CRUD*/
    
        @GetMapping("/regar")
    public String RegarArbol(ModelMap modelo,RedirectAttributes redirectAttributes, HttpSession session) throws ErrorServicio {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
            usuarioServicio.restar(usuario.getId());
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/perfil";
    }
    
    
    @GetMapping("/editarusuario")
    public String EditarUsuario(ModelMap modelo, HttpSession session,@RequestParam(required = false) Integer opcion) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.put("usuario", usuarioServicio.buscarPorId(usuario.getId()));
           modelo.put("opcion", opcion);

        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
        }
        return "editarusuario.html";
    }

    @PostMapping("/editarusuario")
    public String editar(ModelMap modelo, RedirectAttributes redirectAttributes, @RequestParam String id,
            @RequestParam(required = false) String nombre, @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Integer edad, @RequestParam(required = false) String descripcion) {
        try {
            usuarioServicio.editar(id, nombre, apellido, edad, descripcion);
            redirectAttributes.addFlashAttribute("exito", "Usuario editado con éxito");
        } catch (ErrorServicio ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/editarusuario?opcion=1";
    }
    
        @PostMapping("/cambiarfoto")
    public String cambiarFoto(ModelMap modelo, RedirectAttributes redirectAttributes, @RequestParam String id,
            @RequestParam(required = false) MultipartFile archivo) {
        try {
            usuarioServicio.editarFoto(id, archivo);
            redirectAttributes.addFlashAttribute("guardado", "Foto guardada correctamente");
        } catch (ErrorServicio ex) {
            redirectAttributes.addFlashAttribute("singuardar", ex.getMessage());
        }
        return "redirect:/editarusuario?opcion=2";
    }

    @PostMapping("/editararbol")
    public String editarArbol(ModelMap modelo, RedirectAttributes redirectAttributes, @RequestParam String idArbol,
            @RequestParam(required = false) String nombreArbol) {
        try {
            arbolservicio.editar(idArbol, nombreArbol);
            redirectAttributes.addFlashAttribute("check", "Nombre del arbol editado exitosamente");
        } catch (ErrorServicio ex) {
            modelo.put("fall", ex.getMessage());
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/editarusuario?opcion=4";
    }

        @PostMapping("/cambiarcontraseña")
    public String cambiarContraseña( RedirectAttributes redirectAttributes, @RequestParam String id,
            @RequestParam(required = false) String password1, @RequestParam(required = false) String password2) {
        try {
            usuarioServicio.editarContraseña(id, password1, password2);
            redirectAttributes.addFlashAttribute("correcto", "Contraseña editada exitosamente");
        } catch (ErrorServicio ex) {
            redirectAttributes.addFlashAttribute("incorrecto", ex.getMessage());
        }
        return "redirect:/editarusuario?opcion=3";
    }
    
    
    @PostMapping("/gestionar/{id}")
    public String Gestionar(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=5";
    }

    @PostMapping("/gestionar2/{id}")
    public String Gestionar2(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=1";
    }

    @PostMapping("/gestionar3/{id}")
    public String Gestionar3(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=4";
    }

    @PostMapping("/gestionar4/{id}")
    public String Gestionar4(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=3";
    }

    @PostMapping("/gestionar5/{id}")
    public String Gestionar5(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=7";
    }

    @PostMapping("/gestionar6/{id}")
    public String Gestionar6(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=2";
    }

    @PostMapping("/gestionar7/{id}")
    public String Gestionar7(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=6";
    }

    @PostMapping("/gestionar8/{id}")
    public String Gestionar8(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, @RequestParam(required = false, defaultValue = "0") Integer flexRadio1, @RequestParam(required = false, defaultValue = "0") Integer flexRadio2, @RequestParam(required = false, defaultValue = "0") Integer flexRadio3, @RequestParam(required = false, defaultValue = "0") Integer flexRadio4, @RequestParam(required = false, defaultValue = "0") Integer flexRadio5) {

        if (flexRadio1 == 1) {
            redirectAttributes.addFlashAttribute("res1c", "Respuesta Correcta +1 Punto ");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res1i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio2 == 1) {
            redirectAttributes.addFlashAttribute("res2c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res2i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio3 == 1) {
            redirectAttributes.addFlashAttribute("res3c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res3i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio4 == 1) {
            redirectAttributes.addFlashAttribute("res4c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res4i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        if (flexRadio5 == 1) {
            redirectAttributes.addFlashAttribute("res5c", "Respuesta Correcta +1 Punto");
            try {
                usuarioServicio.sumar(id);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectAttributes.addFlashAttribute("bot", "v");
        } else {
            redirectAttributes.addFlashAttribute("res5i", "Respuesta Incorrecta");
            redirectAttributes.addFlashAttribute("bot", "v");
        }
        return "redirect:/cuestionario?question=8";
    }
}
