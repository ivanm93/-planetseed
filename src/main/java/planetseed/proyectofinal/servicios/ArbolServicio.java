
package planetseed.proyectofinal.servicios;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import planetseed.proyectofinal.entidades.Arbol;
import planetseed.proyectofinal.enumeraciones.Tipo;
import planetseed.proyectofinal.errores.ErrorServicio;
import planetseed.proyectofinal.repositorios.ArbolRepo;

@Service
public class ArbolServicio {

    @Autowired
    private ArbolRepo arbolRepo;

    @Transactional
    public Arbol crear(String nombre) throws ErrorServicio {

        validar(nombre);
        Arbol a = new Arbol();

        a.setNombre(nombre);
        a.setTipo(Tipo.MANZANO);
        a.setAlta(true);

        return arbolRepo.save(a);
    }

    @Transactional
    public Arbol editar(String id, String nombre) throws ErrorServicio {
        Optional<Arbol> respuesta = arbolRepo.findById(id);
        if (respuesta.isPresent()) {
            Arbol a = respuesta.get();
            a.setNombre(nombre);
            return arbolRepo.save(a);
        } else {
            throw new ErrorServicio("No se ha encontrado el árbol");
        }
    }
    
    @Transactional
    public void darBaja(String id) throws Exception {
        Optional<Arbol> respuesta = arbolRepo.findById(id);
        if (respuesta.isPresent()) {
            Arbol a = respuesta.get();
            a.setAlta(false);
            arbolRepo.save(a);
        } else {
            throw new Exception("No se ha encontrado el árbol");
        }
    }
    
    @Transactional
    public void eliminar(String id) throws Exception {
        Optional<Arbol> respuesta = arbolRepo.findById(id);
        if (respuesta.isPresent()) {
            Arbol a = respuesta.get();           
            arbolRepo.delete(a);
        } else {
            throw new Exception("No se ha encontrado el árbol");
        }
    }
    
    public void validar(String nombre) throws ErrorServicio {
             
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre es inválido");
        }
    }

}
