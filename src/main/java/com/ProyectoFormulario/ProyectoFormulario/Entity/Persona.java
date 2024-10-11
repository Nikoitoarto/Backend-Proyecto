package com.ProyectoFormulario.ProyectoFormulario.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "persona")
public class Persona extends ABaseEntity{

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "identificacion", nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(name = "telefono", length = 15)
    private int telefono;

    @Column(name = "direccion", length = 200)
    private String direccion;

}