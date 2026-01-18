package com.example.demo.modele;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nom", "prenom"}))
public class Proprietaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String nom;

    @NonNull
    private String prenom;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proprietaire")
    @JsonIgnoreProperties("proprietaire")
    private List<Voiture> voitures;
}

