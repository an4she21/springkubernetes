package com.example.demo;

import com.example.demo.model.Proprietaire;
import com.example.demo.model.Voiture;
import com.example.demo.repository.ProprietaireRepository;
import com.example.demo.repository.VoitureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	private final VoitureRepository voitureRepository;
	private final ProprietaireRepository proprietaireRepository;

	public DemoApplication(VoitureRepository voitureRepository,
			ProprietaireRepository proprietaireRepository) {
		this.voitureRepository = voitureRepository;
		this.proprietaireRepository = proprietaireRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			if (proprietaireRepository.count() == 0) {
				Proprietaire ali = new Proprietaire("Ali", "Hassan");
				Proprietaire najat = new Proprietaire("Najat", "Bani");
				proprietaireRepository.save(ali);
				proprietaireRepository.save(najat);

				if (voitureRepository.count() == 0) {
					voitureRepository.save(new Voiture("Toyota", "Corolla", "Grise", "A-1-9090", 2018, 95000, ali));
					voitureRepository.save(new Voiture("Ford", "Fiesta", "Rouge", "A-2-8090", 2015, 90000, ali));
					voitureRepository.save(new Voiture("Honda", "CRV", "Bleu", "A-3-7090", 2016, 140000, najat));
				}
			}
		};
	}
}
