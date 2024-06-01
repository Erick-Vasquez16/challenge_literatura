package com.example.alura.cursos.literatura.principal;

import com.example.alura.cursos.literatura.enumerdaor.Lenguaje;
import com.example.alura.cursos.literatura.model.*;
import com.example.alura.cursos.literatura.repository.AtuorRepository;
import com.example.alura.cursos.literatura.repository.LibrosRepository;
import com.example.alura.cursos.literatura.service.ConsumoAPI;
import com.example.alura.cursos.literatura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class Principal {
    private LibrosRepository libroRepository;
    private AtuorRepository autorRepository;

    @Autowired
    public Principal(LibrosRepository libroRepository, AtuorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibrosRepository repositorio;
    private List<DatosLibro> datosLibros = new ArrayList<>();

    public Principal(LibrosRepository repository) {this.repositorio = repository;}

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libros por titulo 
                    2 - Ver libors registrados
                    3 - Ver autores registrados
                    4 - Lista de autores vivos en un determinado año
                    5 - Listar los libors por idioma
                                  
                    0 - Salir
                    """;
            
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    librosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresVivos();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    private DatosLibro getDatosLibro(){
        System.out.println("Escriba el nombre del libro");
        String nombreLibro = teclado.nextLine();
        try {
            String nombreCodificado = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8.toString());
            String url = URL_BASE + nombreCodificado;
            String json = consumoApi.obtenerDatos(url);

            if (json == null || json.trim().isEmpty()) {
                throw new RuntimeException("La respuesta de la API está vacía");
            }

            return conversor.obtenerDatos(json, DatosLibro.class);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error al codificar el nombre del libro", e);
        }
    }

    private void buscarLibroWeb() {

        System.out.println("Escriba el nombre del libro:");
        String nombreLibro = teclado.nextLine();
        try {
            // Codificar el nombre del libro para la URL
            String nombreCodificado = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8.toString());
            String url = URL_BASE + nombreCodificado;
            // Obtener datos JSON de la API
            String json = consumoApi.obtenerDatos(url);
            if (json == null || json.trim().isEmpty()) {
                throw new RuntimeException("La respuesta de la API está vacía");
            }
            // Convertir los datos JSON en un objeto Datos
            Datos datos = conversor.obtenerDatos(json, Datos.class);
            // Verificar si se encontraron resultados
            if (datos == null || datos.resultados().isEmpty()) {
                System.out.println("No se encontraron libros con ese título.");
                return;
            }
            // Mostrar los libros encontrados
            System.out.println("Libros encontrados:");
            int index = 1;
            for (DatosLibro datosLibro : datos.resultados()) {
                System.out.println(index + ". " + datosLibro.titulo());
                index++;
            }
            // Solicitar al usuario que elija un libro
            System.out.println("Elija el número del libro que desea guardar:");
            int seleccion = teclado.nextInt();
            teclado.nextLine(); // Consumir el salto de línea

            if (seleccion < 1 || seleccion > datos.resultados().size()) {
                System.out.println("Selección inválida.");
                return;
            }
            // Obtener el libro seleccionado
            DatosLibro datosLibroSeleccionado = datos.resultados().get(seleccion - 1);

            // Convertir DatosLibro a Libro
            Libro libro = new Libro();
            libro.setTitulo(datosLibroSeleccionado.titulo());
            libro.setConteoDescargas(datosLibroSeleccionado.conteoDescargas());
            libro.setAutores(convertirAutores(datosLibroSeleccionado.listaAutores()));
            libro.setLenguaje(Lenguaje.fromString(datosLibroSeleccionado.idioma()));
            libro.setFormatos(datosLibroSeleccionado.lenguajes());

            // Guardar el libro en la base de datos
            LibrosRepository.guardarLibro(libro);

            System.out.println("Libro guardado correctamente en la base de datos.");

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error al codificar el nombre del libro", e);
        }
    }

    private void librosRegistrados() {
        List<Libro> libros = libroRepository.findAllLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            for (Libro libro : libros) {
                System.out.println(libro.getId() + ". " + libro.getTitulo());
            }
        }
    }

    private void autoresRegistrados() {
        List<Autor> autores = autorRepository.findAllAutores();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("Autores registrados:");
            for (Autor autor : autores) {
                System.out.println(autor.getId() + ". " + autor.getNombre() +
                        " (Nacido: " + autor.getFechaNacimiento() +
                        ", Fallecido: " + (autor.getFechaMuerte() != null ? autor.getFechaMuerte() : "N/A") + ")");
            }
        }
    }

    private void autoresVivos() {
        System.out.println("Escriba la fecha (YYYY-MM-DD):");
        String fechaInput = teclado.nextLine();

        try {
            LocalDate fecha = LocalDate.parse(fechaInput, DateTimeFormatter.ISO_LOCAL_DATE);
            List<Autor> autoresVivos = autorRepository.findAutoresVivos(fecha.toString());

            if (autoresVivos.isEmpty()) {
                System.out.println("No hay autores vivos después de la fecha especificada.");
            } else {
                System.out.println("Autores vivos después de " + fecha + ":");
                for (Autor autor : autoresVivos) {
                    System.out.println(autor.getId() + ". " + autor.getNombre() +
                            " (Nacido: " + autor.getFechaNacimiento() +
                            ", Fallecido: " + (autor.getFechaMuerte() != null ? autor.getFechaMuerte() : "N/A") + ")");
                }
            }
        } catch (Exception e) {
            System.out.println("Fecha inválida. Asegúrese de usar el formato YYYY-MM-DD.");
        }

    }

    private void librosPorIdioma() {
        System.out.println("Escriba el idioma (por ejemplo, 'en' para inglés, 'es' para español):");
        String idioma = teclado.nextLine();

        List<Libro> libros = libroRepository.findLibrosPorIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en el idioma especificado.");
        } else {
            System.out.println("Libros registrados en " + idioma + ":");
            for (Libro libro : libros) {
                System.out.println(libro.getId() + ". " + libro.getTitulo());
            }
        }
    }

    // Método para convertir la lista de DatosAutor a lista de Autor
    private List<Autor> convertirAutores(List<DatosAutor> datosAutores) {
        return datosAutores.stream()
                .map(d -> new Autor(d.nombre(), d.fechaNacimiento(), d.fechaMuerte()))
                .collect(Collectors.toList());
    }

}
