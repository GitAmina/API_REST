package com.example.API_REST.services;

import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Etudiant ajouterEtudiant(Etudiant etudiant) {
        etudiant.setMotDePasse(passwordEncoder.encode(etudiant.getMotDePasse()));
        etudiant.setStatut(Etudiant.StatutEtudiant.CONFORME);
        return etudiantRepository.save(etudiant);
    }

    public Etudiant modifierEtudiant(Long id, Map<String, Object> updates) {
        return etudiantRepository.findById(id)
                .map(etudiant -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "nom":
                                etudiant.setNom((String) value);
                                break;
                            case "prenom":
                                etudiant.setPrenom((String) value);
                                break;
                            case "email":
                                etudiant.setEmail((String) value);
                                break;
                            case "motDePasse":
                                if (value != null) {
                                    etudiant.setMotDePasse(passwordEncoder.encode((String) value));
                                }
                                break;
                        }
                    });
                    return etudiantRepository.save(etudiant);
                })
                .orElse(null);
    }

    public boolean supprimerEtudiant(Long id) {
        if (etudiantRepository.existsById(id)) {
            etudiantRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Etudiant> listerEtudiants() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> trouverParEmail(String email) {
        return etudiantRepository.findByEmail(email);
    }

    public Optional<Etudiant> trouverParCodeEtudiant(String codeEtudiant) {
        return etudiantRepository.findByCodeEtudiant(codeEtudiant);
    }

    public List<Etudiant> rechercherParPrenomNom(String prenom, String nom) {
        return etudiantRepository.findByPrenomNom(prenom, nom);
    }


    public List<Etudiant> rechercherParNbrLivreEmprunter(int nbrLivreEmprunter) {
        return etudiantRepository.findByNbrLivreEmprunter(nbrLivreEmprunter);
    }

    public Etudiant changerStatutEtudiant(Long id, String nouveauStatut) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        etudiant.setStatut(Etudiant.StatutEtudiant.valueOf(nouveauStatut));
        return etudiantRepository.save(etudiant);
    }

    public Etudiant reinitialiserAvertissements(Long id) {
        return etudiantRepository.findById(id)
                .map(etudiant -> {
                    etudiant.setNbrAvertissement(0);
                    etudiant.setStatut(Etudiant.StatutEtudiant.CONFORME);
                    return etudiantRepository.save(etudiant);
                })
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
    }
}
