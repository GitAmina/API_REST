package com.example.API_REST.repository;

import com.example.API_REST.entites.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "etudiants")
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    @RestResource(path = "/email")
    Optional<Etudiant> findByEmail(@Param("email") String email);

    @RestResource(path = "/code")
    Optional<Etudiant> findByCodeEtudiant(@Param("codeEtudiant") String codeEtudiant);

    @RestResource(path = "/nom")
    Optional<Etudiant> findEtudiantByNom(@Param("nom") String nom);

    @RestResource(path = "/prenom")
    Optional<Etudiant> findEtudiantByPrenom(@Param("prenom") String prenom);

    @RestResource(path = "/nbrlivres")
    List<Etudiant> findByNbrLivreEmprunter(@Param("nbrLivreEmprunter") int nbrLivreEmprunter);

    @RestResource(path = "/prenom_nom")
    @Query("SELECT e FROM Etudiant e WHERE LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%')) AND LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Etudiant> findByPrenomNom(@Param("prenom") String prenom, @Param("nom") String nom);
}
