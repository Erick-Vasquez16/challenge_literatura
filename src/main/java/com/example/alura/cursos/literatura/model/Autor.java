package com.example.alura.cursos.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "autores")
@Getter
@Setter
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaMuerte;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    //Constructor vacio
    public Autor(){}
    //constructor con los datos

    public Autor(String nombre,Integer fechaNacimiento,Integer fechaMuerte){
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaMuerte = fechaMuerte;
    }

    @Override
    public String toString() {
        return
                "nombre='" + nombre + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", fechaMuerte=" + fechaMuerte ;
    }

    //Getter and setter

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
