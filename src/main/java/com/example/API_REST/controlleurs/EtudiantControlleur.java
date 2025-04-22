package com.example.API_REST.controlleurs;

import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.services.EtudiantService;
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
@RequestMapping("/api/etudiants")
public class EtudiantControlleur {

    @Autowired
    private EtudiantService etudiantService;

    @PostMapping
    public ResponseEntity<Etudiant> ajouterEtudiant(@RequestBody Etudiant etudiant) {
        etudiant.setNbrLivreEmprunter(0);
        Etudiant etu = etudiantService.ajouterEtudiant(etudiant);
        return new ResponseEntity<>(etu, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Etudiant> modifierEtudiant(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Set<String> allowedFields = Set.of("nom", "prenom", "email", "motDePasse");
        if (!updates.keySet().stream().allMatch(allowedFields::contains)) {
            return ResponseEntity.badRequest().build();
        }

        Etudiant etu = etudiantService.modifierEtudiant(id, updates);
        return etu != null
                ? ResponseEntity.ok(etu)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEtudiant(@PathVariable Long id) {
        return etudiantService.supprimerEtudiant(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Etudiant> listerEtudiants() {
        return etudiantService.listerEtudiants();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Etudiant> trouverParEmail(@PathVariable String email) {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
        Optional<Etudiant> etudiant = etudiantService.trouverParEmail(decodedEmail);
        return etudiant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{codeEtudiant}")
    public ResponseEntity<Etudiant> trouverParCodeEtudiant(@PathVariable String codeEtudiant) {
        Optional<Etudiant> etudiant = etudiantService.trouverParCodeEtudiant(codeEtudiant);
        return etudiant.isPresent() ? new ResponseEntity<>(etudiant.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recherche")
    public List<Etudiant> rechercherParPrenomNom(@RequestParam String prenom, @RequestParam String nom) {
        return etudiantService.rechercherParPrenomNom(prenom, nom);
    }

    @GetMapping("/nbrlivres/{nbrLivreEmprunter}")
    public List<Etudiant> rechercherParNbrLivreEmprunter(@PathVariable int nbrLivreEmprunter) {
        return etudiantService.rechercherParNbrLivreEmprunter(nbrLivreEmprunter);
    }

    @PatchMapping("/{id}/reinitialiser-avertissements")
    public ResponseEntity<Etudiant> reinitialiserAvertissements(@PathVariable Long id) {
        Etudiant etudiant = etudiantService.reinitialiserAvertissements(id);
        return ResponseEntity.ok(etudiant);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Etudiant> changerStatutEtudiant(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String nouveauStatut = request.get("statut");
        Etudiant etudiant = etudiantService.changerStatutEtudiant(id, nouveauStatut);
        return ResponseEntity.ok(etudiant);
    }
}
