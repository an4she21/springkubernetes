package com.example.demo.controller;

import com.example.demo.modele.Proprietaire;
import com.example.demo.modele.ProprietaireRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/proprietaires")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Propriétaires", description = "API pour la gestion des propriétaires")
public class ProprietaireController {

    @Autowired
    private ProprietaireRepo proprietaireRepo;

    @GetMapping
    @Operation(summary = "Liste tous les propriétaires", description = "Retourne la liste complète de tous les propriétaires")
    public List<Proprietaire> getAllProprietaires() {
        List<Proprietaire> allProprietaires = (List<Proprietaire>) proprietaireRepo.findAll();
        // Remove duplicates based on ID (in case of data issues)
        return allProprietaires.stream()
                .filter(p -> p.getId() > 0) // Filter valid proprietaires
                .collect(java.util.stream.Collectors.toMap(
                    Proprietaire::getId,
                    p -> p,
                    (existing, replacement) -> existing // Keep first occurrence if duplicate ID
                ))
                .values()
                .stream()
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un propriétaire par ID", description = "Retourne un propriétaire spécifique selon son ID")
    public ResponseEntity<Proprietaire> getProprietaireById(@PathVariable Long id) {
        Optional<Proprietaire> proprietaire = proprietaireRepo.findById(id);
        return proprietaire.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée un nouveau propriétaire", description = "Ajoute un nouveau propriétaire à la base de données")
    public ResponseEntity<Proprietaire> createProprietaire(@RequestBody Proprietaire proprietaire) {
        Proprietaire savedProprietaire = proprietaireRepo.save(proprietaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProprietaire);
    }
}

