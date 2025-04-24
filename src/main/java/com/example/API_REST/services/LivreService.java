package com.example.API_REST.services;

import com.example.API_REST.entites.Livre;
import com.example.API_REST.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LivreService {

    @Autowired
    private LivreRepository livreRepository;

    public Livre ajouterLivre(Livre livre) {
        if (livre.getAdministrateur() == null) {
            throw new IllegalStateException("Un livre doit avoir un administrateur");
        }

        return livreRepository.save(livre);
    }

    public Livre modifierLivre(Long id, Map<String, Object> updates) {
        return livreRepository.findById(id)
                .map(livre -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "titre":
                                livre.setTitre((String) value);
                                break;
                            case "genre":
                                livre.setGenre((String) value);
                                break;
                            case "auteur":
                                livre.setAuteur((String) value);
                                break;
                            case "annee":
                                livre.setAnnee((int) value);
                                break;
                            case "description":
                                livre.setDescription((String) value);
                                break;
                        }
                    });
                    return livreRepository.save(livre);
                })
                .orElse(null);
    }

    public boolean supprimerLivre(Long id) {
        if (livreRepository.existsById(id)) {
            livreRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Livre> listerLivres() {
        return livreRepository.findAll();
    }

    public List<Livre> rechercherParTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre);
    }

    public List<Livre> rechercherParGenre(String genre) {
        return livreRepository.findByGenreContainingIgnoreCase(genre);
    }

    public List<Livre> livresDisponibles() {
        return livreRepository.findByDisponibiliteTrue();
    }

    public List<Livre> livresIndisponibles() {
        return livreRepository.findByDisponibiliteFalse();
    }

    public List<Livre> rechercherParAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur);
    }

    public List<Livre> rechercherParAnnee(int annee) {
        return livreRepository.findByAnnee(annee);
    }

    public List<Livre> rechercherLivresMultiCriteres(String titre, String genre, String auteur) {
        return livreRepository.findByTitreContainingAndGenreContainingAndAuteurContainingAllIgnoreCase(
                titre, genre, auteur);
    }
}
