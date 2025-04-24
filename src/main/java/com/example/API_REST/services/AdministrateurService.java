package com.example.API_REST.services;

import com.example.API_REST.entites.Administrateur;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.repository.AdministrateurRepository;
import com.example.API_REST.entites.Livre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdministrateurService {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Administrateur ajouterAdministrateur(Administrateur administrateur) {
        if (administrateurRepository.findByEmail(administrateur.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }
        administrateur.setMotDePasse(passwordEncoder.encode(administrateur.getMotDePasse()));
        return administrateurRepository.save(administrateur);
    }

    public Administrateur modifierAdministrateur(Long id, Map<String, Object> updates) {
        return administrateurRepository.findById(id)
                .map(admin -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "nom":
                                admin.setNom((String) value);
                                break;
                            case "prenom":
                                admin.setPrenom((String) value);
                                break;
                            case "email":
                                admin.setEmail((String) value);
                                break;
                            case "motDePasse":
                                if (value != null) {
                                    admin.setMotDePasse(passwordEncoder.encode((String) value));
                                }
                                break;
                        }
                    });
                    return administrateurRepository.save(admin);
                })
                .orElse(null);
    }

    public boolean supprimerAdministrateur(Long id) {
        long nombreAdmins = administrateurRepository.count();

        if (nombreAdmins <= 1) {
            return false;
        }

        if (administrateurRepository.existsById(id)) {
            administrateurRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Administrateur> listerAdministrateurs() {
        return administrateurRepository.findAll();
    }

    public Optional<Administrateur> trouverParEmail(String email) {
        return administrateurRepository.findByEmail(email);
    }

    public List<Administrateur> rechercherParPrenom(String prenom) {
        return administrateurRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    public List<Administrateur> rechercherParPrenomNom(String prenom, String nom) {
        return administrateurRepository.findByPrenomNom(prenom, nom);
    }
}
