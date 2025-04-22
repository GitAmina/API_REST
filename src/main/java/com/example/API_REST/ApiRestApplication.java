package com.example.API_REST;

import com.example.API_REST.entites.Administrateur;
import com.example.API_REST.entites.Emprunt;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.entites.Livre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class ApiRestApplication implements CommandLineRunner {

	@Autowired
	private RepositoryRestConfiguration repositoryRestConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repositoryRestConfiguration.exposeIdsFor(Administrateur.class);
		repositoryRestConfiguration.exposeIdsFor(Emprunt.class);
		repositoryRestConfiguration.exposeIdsFor(Etudiant.class);
		repositoryRestConfiguration.exposeIdsFor(Livre.class);
	}
}