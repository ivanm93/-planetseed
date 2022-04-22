/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package planetseed.proyectofinal.entidades;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import planetseed.proyectofinal.enumeraciones.Role;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String nombre;
    private String apellido;
    private Integer edad;
    private String email;
    private String password;
    private Imagen imagen;
    
    @Enumerated(EnumType.STRING)
    private Role role;  
    
    private Integer puntos;
    
    @OneToOne
    private Arbol arbol;
    
    private Boolean alta;
}


