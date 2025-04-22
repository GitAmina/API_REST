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
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idL;

    @NonNull
    private String titre;
    private String genre;
    private String auteur;
    private int annee;
    private String description;
    private Boolean disponibilite = true;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Administrateur administrateur;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emprunt> emprunts;
}
