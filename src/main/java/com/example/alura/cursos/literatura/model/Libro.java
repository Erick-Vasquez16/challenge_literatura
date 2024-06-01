package com.example.alura.cursos.literatura.model;

import com.example.alura.cursos.literatura.enumerdaor.Lenguaje;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "libros")
@Getter
@Setter
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private Integer conteoDescargas;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "autor_libro",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "lenguaje")
    private Lenguaje lenguaje;

    @ElementCollection
    @CollectionTable(name = "libro_formatos", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "formato")
    private List<String> formatos;

    public Libro() {
        this.autores = new ArrayList<>();
    }

    public Libro(Long Id, String titulo, Integer conteoDescargas,
                 String tipoDeMedio, List<Autor> autores,Lenguaje lenguaje) {
        this.Id = Id;
        this.titulo = titulo;
        this.conteoDescargas = conteoDescargas;
        this.autores = autores;
        this.lenguaje = lenguaje;
    }

    @Override
    public String toString() {
        return
                ", titulo='" + titulo + '\'' +
                ", conteoDescargas=" + conteoDescargas +
                ", autores=" + autores +
                ", lenguaje=" + lenguaje ;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getConteoDescargas() {
        return conteoDescargas;
    }

    public void setConteoDescargas(Integer conteoDescargas) {
        this.conteoDescargas = conteoDescargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public Lenguaje getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(Lenguaje lenguaje) {
        this.lenguaje = lenguaje;
    }

    public List<String> getFormatos() {
        return formatos;
    }

    public void setFormatos(List<String> formatos) {
        this.formatos = formatos;
    }
}
