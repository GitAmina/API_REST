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
public class Administrateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAd;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    private String motDePasse;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "administrateur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Livre> livres;
}
