package com.example.API_REST.repository;

import com.example.API_REST.entites.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "livres")
public interface LivreRepository extends JpaRepository<Livre, Long> {

    @RestResource(path = "/titre")
    List<Livre> findByTitreContainingIgnoreCase(@Param("titre") String titre);

    @RestResource(path = "/genre")
    List<Livre> findByGenreContainingIgnoreCase(@Param("genre") String genre);

    @RestResource(path = "/auteur")
    List<Livre> findByAuteurContainingIgnoreCase(@Param("auteur") String auteur);

    @RestResource(path = "/annee")
    List<Livre> findByAnnee(@Param("annee") int annee);

    @RestResource(path = "/disponible")
    List<Livre> findByDisponibiliteTrue();

    @RestResource(path = "/indisponible")
    List<Livre> findByDisponibiliteFalse();

    @RestResource(path = "/recherche")
    List<Livre> findByTitreContainingAndGenreContainingAndAuteurContainingAllIgnoreCase(
            @Param("titre") String titre,
            @Param("genre") String genre,
            @Param("auteur") String auteur
    );
}
