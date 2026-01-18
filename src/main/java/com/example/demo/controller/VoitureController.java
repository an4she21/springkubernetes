package com.example.demo.controller;

import com.example.demo.modele.Proprietaire;
import com.example.demo.modele.ProprietaireRepo;
import com.example.demo.modele.Voiture;
import com.example.demo.modele.VoitureRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/voitures")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Voitures", description = "API pour la gestion des voitures")
public class VoitureController {

    @Autowired
    private VoitureRepo voitureRepo;

    @Autowired
    private ProprietaireRepo proprietaireRepo;

    @GetMapping
    @Operation(summary = "Liste toutes les voitures", description = "Retourne la liste complète de toutes les voitures")
    public List<Voiture> getAllVoitures() {
        return (List<Voiture>) voitureRepo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une voiture par ID", description = "Retourne une voiture spécifique selon son ID")
    public ResponseEntity<Voiture> getVoitureById(@PathVariable Long id) {
        Optional<Voiture> voiture = voitureRepo.findById(id);
        return voiture.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle voiture", description = "Ajoute une nouvelle voiture à la base de données")
    public ResponseEntity<Voiture> createVoiture(@RequestBody Voiture voiture) {
        try {
            // Fetch the proprietaire from database if it has an id
            if (voiture.getProprietaire() != null && voiture.getProprietaire().getId() > 0) {
                Optional<Proprietaire> proprietaireOpt = proprietaireRepo.findById(voiture.getProprietaire().getId());
                if (proprietaireOpt.isPresent()) {
                    voiture.setProprietaire(proprietaireOpt.get());
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
            Voiture savedVoiture = voitureRepo.save(voiture);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVoiture);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une voiture", description = "Modifie les informations d'une voiture existante")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable Long id, @RequestBody Voiture voiture) {
        if (!voitureRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        voiture.setId(id);
        Voiture updatedVoiture = voitureRepo.save(voiture);
        return ResponseEntity.ok(updatedVoiture);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une voiture", description = "Supprime une voiture de la base de données")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        if (!voitureRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        voitureRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
