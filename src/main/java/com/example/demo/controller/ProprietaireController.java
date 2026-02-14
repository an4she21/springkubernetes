package com.example.demo.controller;

import com.example.demo.model.Proprietaire;
import com.example.demo.repository.ProprietaireRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proprietaires")
@Tag(name = "Propriétaires", description = "API pour la gestion des propriétaires")
public class ProprietaireController {

    private final ProprietaireRepository proprietaireRepository;

    public ProprietaireController(ProprietaireRepository proprietaireRepository) {
        this.proprietaireRepository = proprietaireRepository;
    }

    @GetMapping
    @Operation(summary = "Liste tous les propriétaires")
    public List<Proprietaire> getAllProprietaires() {
        return (List<Proprietaire>) proprietaireRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un propriétaire par ID")
    public ResponseEntity<Proprietaire> getProprietaireById(@PathVariable Long id) {
        Optional<Proprietaire> proprietaire = proprietaireRepository.findById(id);
        return proprietaire.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée un nouveau propriétaire")
    public ResponseEntity<Proprietaire> createProprietaire(@RequestBody Proprietaire proprietaire) {
        Proprietaire saved = proprietaireRepository.save(proprietaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
