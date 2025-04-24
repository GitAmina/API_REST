package com.example.API_REST.controlleurs;

import com.example.API_REST.entites.Emprunt;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.entites.Livre;
import com.example.API_REST.repository.EtudiantRepository;
import com.example.API_REST.repository.LivreRepository;
import com.example.API_REST.services.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/emprunts")
public class EmpruntControlleur {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @PostMapping
    public ResponseEntity<Emprunt> emprunterLivre(
            @RequestBody Map<String, Long> request,
            @RequestHeader("Authorization") String token) {

        Long livreId = request.get("livreId");
        Long etudiantId = request.get("etudiantId");

        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        Emprunt emprunt = new Emprunt();
        emprunt.setLivre(livre);
        emprunt.setEtudiant(etudiant);

        Emprunt e = empruntService.emprunterLivre(emprunt);
        return new ResponseEntity<>(e, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> retournerLivre(@PathVariable Long id) {
        return empruntService.retournerLivre(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Emprunt> listerEmprunts() {
        return empruntService.listerEmprunts();
    }

    @GetMapping("/etudiant/{id}")
    public List<Emprunt> listerEmpruntsParEtudiant(@PathVariable Long id) {
        return empruntService.trouverEmpruntsParEtudiantId(id);
    }

    @GetMapping("/en_cours")
    public List<Emprunt> listerEmpruntsEnCours(@RequestParam String email) {
        return empruntService.listerEmpruntsEnCours(email);
    }

    @GetMapping("/retard")
    public List<Emprunt> empruntsEnRetard() {
        return empruntService.empruntsEnRetard();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprunt> getEmpruntById(@PathVariable Long id) {
        Optional<Emprunt> emprunt = empruntService.getEmpruntById(id);
        return emprunt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/prolonger")
    public ResponseEntity<?> prolongerEmprunt(@PathVariable Long id) {
        try {
            Emprunt emprunt = empruntService.prolongerEmprunt(id);
            return ResponseEntity.ok(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/avertir")
    public ResponseEntity<?> avertirEtudiant(@PathVariable Long id) {
        try {
            Etudiant etudiant = empruntService.avertirEtudiantPourRetard(id);
            return ResponseEntity.ok(etudiant);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
