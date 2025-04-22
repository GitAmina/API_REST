package com.example.API_REST.controlleurs;

import com.example.API_REST.entites.Administrateur;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.services.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/administrateurs")
public class AdministrateurControlleur {

    @Autowired
    private AdministrateurService administrateurService;

    @PostMapping
    public ResponseEntity<Administrateur> ajouterAdministrateur(@RequestBody Administrateur administrateur) {
        Administrateur admin = administrateurService.ajouterAdministrateur(administrateur);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Administrateur> modifierEtudiant(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Set<String> allowedFields = Set.of("nom", "prenom", "email", "motDePasse");
        if (!updates.keySet().stream().allMatch(allowedFields::contains)) {
            return ResponseEntity.badRequest().build();
        }

        Administrateur admin = administrateurService.modifierAdministrateur(id, updates);
        return admin != null
                ? ResponseEntity.ok(admin)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAdministrateur(@PathVariable Long id) {
        return administrateurService.supprimerAdministrateur(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Administrateur> listerAdministrateurs() {
        return administrateurService.listerAdministrateurs();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Administrateur> trouverParEmail(@PathVariable String email) {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
        Optional<Administrateur> administrateur = administrateurService.trouverParEmail(decodedEmail);
        return administrateur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/prenom/{prenom}")
    public List<Administrateur> rechercherParPrenom(@PathVariable String prenom) {
        return administrateurService.rechercherParPrenom(prenom);
    }

    @GetMapping("/prenom_nom/{prenom}/{nom}")
    public List<Administrateur> rechercherParPrenomNom(@PathVariable String prenom, @PathVariable String nom) {
        return administrateurService.rechercherParPrenomNom(prenom, nom);
    }
}
