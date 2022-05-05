/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetseed.proyectofinal.servicios;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import planetseed.proyectofinal.entidades.Pregunta;
import planetseed.proyectofinal.errores.ErrorServicio;
import planetseed.proyectofinal.repositorios.PreguntaRepositorio;

@Service
public class PreguntaServicio {
      
    @Autowired
    private PreguntaRepositorio preguntarepositorio;

    
    @Transactional
    public Pregunta crear(Integer id) throws ErrorServicio {
        Pregunta pregunta = new Pregunta();
        pregunta.setId(id);
        pregunta.setAlta(true);
      return  preguntarepositorio.save(pregunta);
    }
    
          @Transactional
    public void darAlta(String id) throws Exception {
        Optional<Pregunta> respuesta = preguntarepositorio.findById(id);
        if (respuesta.isPresent()) {
            Pregunta p = respuesta.get();
            p.setAlta(true);
            preguntarepositorio.save(p);
        } else {
            throw new Exception("No se ha encontrado pregunta");
        }
    } 
    
       @Transactional
    public void darBaja(String id) throws Exception {
        Optional<Pregunta> respuesta = preguntarepositorio.findById(id);
        if (respuesta.isPresent()) {
            Pregunta p = respuesta.get();
            p.setAlta(false);
            preguntarepositorio.save(p);
        } else {
            throw new Exception("No se ha encontrado pregunta");
        }
    } 
}
