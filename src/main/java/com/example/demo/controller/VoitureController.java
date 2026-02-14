package com.example.demo.controller;

import com.example.demo.model.Proprietaire;
import com.example.demo.model.Voiture;
import com.example.demo.repository.ProprietaireRepository;
import com.example.demo.repository.VoitureRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/voitures")
@Tag(name = "Voitures", description = "API pour la gestion des voitures")
public class VoitureController {

    private final VoitureRepository voitureRepository;
    private final ProprietaireRepository proprietaireRepository;

    public VoitureController(VoitureRepository voitureRepository,
            ProprietaireRepository proprietaireRepository) {
        this.voitureRepository = voitureRepository;
        this.proprietaireRepository = proprietaireRepository;
    }

    @GetMapping
    @Operation(summary = "Liste toutes les voitures")
    public List<Voiture> getAllVoitures() {
        return (List<Voiture>) voitureRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une voiture par ID")
    public ResponseEntity<Voiture> getVoitureById(@PathVariable Long id) {
        return voitureRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle voiture")
    public ResponseEntity<?> createVoiture(@RequestBody Voiture voiture) {
        if (voiture.getProprietaire() == null || voiture.getProprietaire().getId() <= 0) {
            return ResponseEntity.badRequest().body("Un propriétaire valide est requis.");
        }

        Optional<Proprietaire> proprietaire = proprietaireRepository.findById(voiture.getProprietaire().getId());
        if (proprietaire.isEmpty()) {
            return ResponseEntity.badRequest().body("Propriétaire introuvable.");
        }

        voiture.setProprietaire(proprietaire.get());
        Voiture saved = voitureRepository.save(voiture);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une voiture")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable Long id, @RequestBody Voiture voiture) {
        if (!voitureRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        voiture.setId(id);
        Voiture updated = voitureRepository.save(voiture);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une voiture")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        if (!voitureRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        voitureRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
