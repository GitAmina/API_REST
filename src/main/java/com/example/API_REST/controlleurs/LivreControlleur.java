package com.example.API_REST.controlleurs;

import com.example.API_REST.entites.Administrateur;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.entites.Livre;
import com.example.API_REST.repository.AdministrateurRepository;
import com.example.API_REST.services.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/livres")
public class LivreControlleur {

    @Autowired
    private LivreService livreService;

    @Autowired
    private AdministrateurRepository adminRepository;

    @PostMapping
    public ResponseEntity<Livre> ajouterLivre(
            @Validated @RequestBody Livre livre,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non authentifié");
        }

        Administrateur admin = adminRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin non trouvé"));

        livre.setTitre(livre.getTitre().trim());
        livre.setAuteur(livre.getAuteur().trim());

        if (livre.getDescription() != null) {
            livre.setDescription(livre.getDescription().trim());
            if (livre.getDescription().isEmpty()) {
                livre.setDescription(null);
            }
        }

        livre.setAdministrateur(admin);
        livre.setDisponibilite(true);

        if (livre.getAnnee() < 0 || livre.getAnnee() > Year.now().getValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Année invalide");
        }

        Livre savedLivre = livreService.ajouterLivre(livre);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLivre);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Livre> modifierLivre(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Set<String> allowedFields = Set.of("titre", "genre", "auteur", "annee", "description");
        if (!updates.keySet().stream().allMatch(allowedFields::contains)) {
            return ResponseEntity.badRequest().build();
        }

        Livre livre = livreService.modifierLivre(id, updates);
        return livre != null
                ? ResponseEntity.ok(livre)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerLivre(@PathVariable Long id) {
        return livreService.supprimerLivre(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Livre> listerLivres() {
        return livreService.listerLivres();
    }

    @GetMapping("/recherche")
    public List<Livre> rechercherLivres(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String auteur,
            @RequestParam(required = false) String genre) {
        return livreService.rechercherLivresMultiCriteres(titre, genre, auteur);
    }

    @GetMapping("/recherche/titre/{titre}")
    public List<Livre> rechercherParTitre(@PathVariable String titre) {
        return livreService.rechercherParTitre(titre);
    }

    @GetMapping("/recherche/genre/{genre}")
    public List<Livre> rechercherParGenre(@PathVariable String genre) {
        return livreService.rechercherParGenre(genre);
    }

    @GetMapping("/recherche/auteur/{auteur}")
    public List<Livre> rechercherParAuteur(@PathVariable String auteur) {
        return livreService.rechercherParAuteur(auteur);
    }

    @GetMapping("/annee/{annee}")
    public List<Livre> rechercherParAnnee(@PathVariable int annee) {
        return livreService.rechercherParAnnee(annee);
    }

    @GetMapping("/disponibles")
    public List<Livre> livresDisponibles() {
        return livreService.livresDisponibles();
    }

    @GetMapping("/indisponibles")
    public List<Livre> livresIndisponibles() {
        return livreService.livresIndisponibles();
    }
}
