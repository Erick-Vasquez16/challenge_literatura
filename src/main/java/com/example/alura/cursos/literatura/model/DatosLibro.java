package com.example.alura.cursos.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("download_count") Integer conteoDescargas,
        @JsonAlias("languages")  String idioma,
        @JsonAlias("authors") List<DatosAutor> listaAutores,
        @JsonAlias("languages") List<String> lenguajes
) {
}
//Para informacion de libros solo se utilizara lo siguiente,titulo,autor,idioma,numero de descargas