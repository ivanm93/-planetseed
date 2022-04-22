/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package planetseed.proyectofinal.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import planetseed.proyectofinal.entidades.Imagen;


public interface ImagenRepo extends JpaRepository<Imagen, String>{

}
