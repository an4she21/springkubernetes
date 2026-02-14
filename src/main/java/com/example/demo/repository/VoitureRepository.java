package com.example.demo.repository;

import com.example.demo.model.Voiture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoitureRepository extends CrudRepository<Voiture, Long> {

    List<Voiture> findByMarque(String marque);

    List<Voiture> findByCouleur(String couleur);

    List<Voiture> findByAnnee(int annee);

    List<Voiture> findByMarqueAndModele(String marque, String modele);
}
