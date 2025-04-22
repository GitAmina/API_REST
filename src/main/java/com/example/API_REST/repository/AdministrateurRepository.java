package com.example.API_REST.repository;

import com.example.API_REST.entites.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "administrateurs")
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    @RestResource(path = "/email")
    Optional<Administrateur> findByEmail(@Param("email") String email);

    @RestResource(path = "/prenom")
    List<Administrateur> findByPrenomContainingIgnoreCase(@Param("prenom") String prenom);

    @RestResource(path = "/nom")
    List<Administrateur> findByNomContainingIgnoreCase(@Param("nom") String nom);

    @RestResource(path = "/prenom_nom")
    @Query("SELECT a FROM Administrateur a WHERE LOWER(a.prenom) LIKE LOWER(CONCAT('%', :prenom, '%')) AND LOWER(a.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Administrateur> findByPrenomNom(@Param("prenom") String prenom, @Param("nom") String nom);
}
