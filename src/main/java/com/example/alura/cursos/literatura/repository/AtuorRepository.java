package com.example.alura.cursos.literatura.repository;

import com.example.alura.cursos.literatura.model.Autor;
import com.example.alura.cursos.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AtuorRepository extends JpaRepository<Autor,Long> {
     List<Autor> findByNombre(String nombre);

     @Query(value = "SELECT * FROM autores", nativeQuery = true)
     List<Autor> findAllAutores();

     @Query(value = "SELECT * FROM autores WHERE fecha_muerte IS NULL OR fecha_muerte > :fecha", nativeQuery = true)
     List<Autor> findAutoresVivos(@Param("fecha") String fecha);

}
