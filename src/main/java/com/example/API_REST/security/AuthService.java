package com.example.API_REST.security;

import com.example.API_REST.entites.Administrateur;
import com.example.API_REST.entites.Etudiant;
import com.example.API_REST.repository.AdministrateurRepository;
import com.example.API_REST.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private AdministrateurRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateEtudiant(String email, String motDePasse) {
        Etudiant etudiant = etudiantRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Email étudiant non trouvé"));

        if (!passwordEncoder.matches(motDePasse, etudiant.getMotDePasse())) {
            throw new AuthenticationException("Mot de passe incorrect");
        }

        return JwtUtil.generateToken(etudiant.getEmail(), "ETUDIANT");
    }

    public String authenticateAdmin(String email, String motDePasse) {
        Administrateur admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Email admin non trouvé"));

        if (!passwordEncoder.matches(motDePasse, admin.getMotDePasse())) {
            throw new AuthenticationException("Mot de passe incorrect");
        }

        return JwtUtil.generateToken(admin.getEmail(), "ADMIN");
    }

    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}