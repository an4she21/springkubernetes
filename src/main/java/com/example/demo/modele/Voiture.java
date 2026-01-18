package com.example.demo.modele;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
public class Voiture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String marque;

    @NonNull
    private String modele;

    @NonNull
    private String couleur;

    @NonNull
    private String immatricule;

    private int annee;

    private int prix;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id")
    private Proprietaire proprietaire;

    public Voiture(@NonNull String marque, @NonNull String modele, @NonNull String couleur, 
                   @NonNull String immatricule, int annee, int prix, @NonNull Proprietaire proprietaire) {
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.immatricule = immatricule;
        this.annee = annee;
        this.prix = prix;
        this.proprietaire = proprietaire;
    }
}

