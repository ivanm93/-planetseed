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
import org.springframework.web.multipart.MultipartFile;
import planetseed.proyectofinal.entidades.Imagen;
import planetseed.proyectofinal.errores.ErrorServicio;
import planetseed.proyectofinal.repositorios.ImagenRepo;


@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepo imagenRepo;
    
    @Transactional
    public Imagen guardar(MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Imagen i = new Imagen();
                i.setMime(archivo.getContentType());
                i.setNombre(archivo.getName());
                i.setContenido(archivo.getBytes());

                return imagenRepo.save(i);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    @Transactional
    public Imagen actualizar(String imagenId, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Imagen i = new Imagen();

                if (imagenId != null) {
                    Optional<Imagen> respuesta = imagenRepo.findById(imagenId);
                    if (respuesta.isPresent()) {
                        i = respuesta.get();
                    }
                }

                i.setMime(archivo.getContentType());
                i.setNombre(archivo.getName());
                i.setContenido(archivo.getBytes());

                return imagenRepo.save(i);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
