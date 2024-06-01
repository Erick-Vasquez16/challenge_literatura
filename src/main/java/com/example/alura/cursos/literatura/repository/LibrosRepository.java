package com.example.alura.cursos.literatura.repository;

import com.example.alura.cursos.literatura.enumerdaor.Lenguaje;
import com.example.alura.cursos.literatura.model.Libro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibrosRepository extends JpaRepository<Libro, Long> {
    static void guardarLibro(Libro libro) {
    }

    List<Libro> findByTitulo(String titulo);

    @Query(value = "SELECT * FROM libros", nativeQuery = true)
    List<Libro> findAllLibros();

    @Query(value = "SELECT * FROM libros WHERE lenguaje = :lenguaje", nativeQuery = true)
    List<Libro> findLibrosPorIdioma(@Param("lenguaje") String lenguaje);

}
