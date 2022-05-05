/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetseed.proyectofinal.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

   @Entity
@Getter @Setter
public class Pregunta {

    @Id
    private Integer id; 
    private Boolean alta;

    public Pregunta() {
    }

    public Pregunta( Boolean alta) {
        this.id = id;
        this.alta = alta;
    }


    
    
} 

