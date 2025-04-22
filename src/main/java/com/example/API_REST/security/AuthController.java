package com.example.API_REST.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/etudiant/login")
    public ResponseEntity<?> loginEtudiant(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticateEtudiant(request.email(), request.motDePasse());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthService.AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticateAdmin(request.email(), request.motDePasse());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthService.AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    public record LoginRequest(String email, String motDePasse) {}
    public record AuthResponse(String token) {}
}