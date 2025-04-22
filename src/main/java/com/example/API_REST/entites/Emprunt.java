package com.example.API_REST.entites;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livre_id")
    private Livre livre;

    private LocalDate dateEmprunt = LocalDate.now();
    private LocalDate dateRetour;

    private LocalDate dateRetourPrevu;

    private boolean prolonge = false;

    @PrePersist
    protected void onCreate() {
        if (this.dateEmprunt == null) {
            this.dateEmprunt = LocalDate.now();
        }
        if (this.dateRetourPrevu == null) {
            this.dateRetourPrevu = this.dateEmprunt.plusDays(10);
        }
    }
}