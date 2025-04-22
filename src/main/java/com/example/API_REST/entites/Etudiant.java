package com.example.API_REST.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEt;

    @Column(unique = true, nullable = false)
    private String codeEtudiant;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    private String motDePasse;

    private int nbrLivreEmprunter = 0;

    @Enumerated(EnumType.STRING)
    private StatutEtudiant statut = StatutEtudiant.CONFORME;

    private int nbrAvertissement = 0;

    public enum StatutEtudiant {
        CONFORME,
        ATTENTION,
        BLOQUE
    }

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emprunt> emprunts;
}
