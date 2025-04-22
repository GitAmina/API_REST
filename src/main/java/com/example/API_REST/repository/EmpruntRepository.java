package com.example.API_REST.repository;

import com.example.API_REST.entites.Emprunt;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.entites.Livre;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "emprunts")
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    @RestResource(path = "/etudiant")
    List<Emprunt> findByEtudiant(@Param("etudiant") Etudiant etudiant);

    @RestResource(path = "/etudiantId")
    List<Emprunt> findEmpruntByEtudiantIdEt(@Param("etudiantId") Long etudiantId);

    @RestResource(path = "/livre")
    List<Emprunt> findByLivre(@Param("livre") Livre livre);

    @RestResource(path = "/en_cours")
    List<Emprunt> findByDateRetourIsNull();

    List<Emprunt> findByEtudiantAndDateRetourIsNull(@Param("etudiant") Etudiant etudiant);

    @RestResource(path = "/retard")
    @Query("SELECT e FROM Emprunt e WHERE e.dateRetourPrevu < :date AND e.dateRetour IS NULL")
    List<Emprunt> findEmpruntsEnRetard(@Param("date") LocalDate date);

    List<Emprunt> findByDateRetourPrevuBeforeAndDateRetourIsNull(LocalDate date);

}