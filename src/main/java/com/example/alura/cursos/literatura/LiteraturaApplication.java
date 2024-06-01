package com.example.alura.cursos.literatura;

import com.example.alura.cursos.literatura.principal.Principal;
import com.example.alura.cursos.literatura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {


	@Autowired
	private LibrosRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.muestraMenu();

	}
}
