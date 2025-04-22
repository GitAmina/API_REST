package com.example.API_REST.services;

import com.example.API_REST.repository.EtudiantRepository;
import com.example.API_REST.repository.LivreRepository;
import com.example.API_REST.entites.Emprunt;
import com.example.API_REST.repository.EmpruntRepository;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.entites.Livre;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private LivreRepository livreRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;

    @Transactional
    public Emprunt emprunterLivre(Emprunt emprunt) {
        Etudiant etudiant = etudiantRepository.findById(emprunt.getEtudiant().getIdEt())
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        emprunt.setEtudiant(etudiant);

        if (!verifierDisponibilite(emprunt.getLivre())) {
            throw new RuntimeException("Le livre n'est pas disponible");
        }

        etudiant.setNbrLivreEmprunter(etudiant.getNbrLivreEmprunter() + 1);
        mettreAJourDisponibiliteLivre(emprunt.getLivre().getIdL(), false);

        return empruntRepository.save(emprunt);
    }

    public Optional<Emprunt> getEmpruntById(Long id) {
        return empruntRepository.findById(id);
    }

    public Emprunt prolongerEmprunt(Long empruntId) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

        if (emprunt.isProlonge()) {
            throw new RuntimeException("Déjà prolongé une fois");
        }

        emprunt.setDateRetourPrevu(emprunt.getDateRetourPrevu().plusDays(10));
        emprunt.setProlonge(true);
        return empruntRepository.save(emprunt);
    }

    public Etudiant avertirEtudiantPourRetard(Long empruntId) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

        Etudiant etudiant = emprunt.getEtudiant();
        etudiant.setNbrAvertissement(etudiant.getNbrAvertissement() + 1);

        if (etudiant.getNbrAvertissement() >= 3) {
            etudiant.setStatut(Etudiant.StatutEtudiant.BLOQUE);
        } else if (etudiant.getNbrAvertissement() >= 1) {
            etudiant.setStatut(Etudiant.StatutEtudiant.ATTENTION);
        }

        return etudiantRepository.save(etudiant);
    }

    public List<Emprunt> trouverEmpruntsEnRetard() {
        return empruntRepository.findByDateRetourPrevuBeforeAndDateRetourIsNull(LocalDate.now());
    }

    public List<Emprunt> empruntsEnRetard() {
        return empruntRepository.findByDateRetourPrevuBeforeAndDateRetourIsNull(LocalDate.now());
    }

    @Transactional
    public boolean retournerLivre(Long id) {
        return empruntRepository.findById(id)
                .map(emprunt -> {
                    emprunt.getEtudiant().setNbrLivreEmprunter(
                            emprunt.getEtudiant().getNbrLivreEmprunter() - 1);
                    mettreAJourDisponibiliteLivre(emprunt.getLivre().getIdL(), true);

                    emprunt.setDateRetour(LocalDate.now());
                    empruntRepository.save(emprunt);

                    return true;
                })
                .orElse(false);
    }

    public List<Emprunt> listerEmprunts() {
        return empruntRepository.findAll();
    }

    public List<Emprunt> trouverEmpruntsParEtudiant(Etudiant etudiant) {
        return empruntRepository.findByEtudiant(etudiant);
    }

    public List<Emprunt> trouverEmpruntsParEtudiantId(Long etudiantId) {
        return empruntRepository.findEmpruntByEtudiantIdEt(etudiantId);
    }

    public List<Emprunt> trouverEmpruntsParLivre(Livre livre) {
        return empruntRepository.findByLivre(livre);
    }

    public boolean verifierDisponibilite(Livre livre) {
        return livre.getDisponibilite();
    }

    public void mettreAJourDisponibiliteLivre(Long livreId, boolean disponibilite) {
        Optional<Livre> livreOptional = livreRepository.findById(livreId);
        livreOptional.ifPresent(livre -> {
            livre.setDisponibilite(disponibilite);
            livreRepository.save(livre);
        });
    }

    /*public List<Emprunt> listerEmpruntsEnCours() {
        return empruntRepository.findByDateRetourIsNull();
    }*/

    public List<Emprunt> listerEmpruntsEnCours(String email) {
        Etudiant etudiant = etudiantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return empruntRepository.findByEtudiantAndDateRetourIsNull(etudiant);
    }
}
