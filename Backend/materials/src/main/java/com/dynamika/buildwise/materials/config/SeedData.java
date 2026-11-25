package com.dynamika.buildwise.materials.config;

import com.dynamika.buildwise.materials.model.Material;
import com.dynamika.buildwise.materials.repository.MaterialRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")   // les tests tournent sans MongoDB
public class SeedData {
    // Trois catégories, trois jeux d'attributs différents dans specs : c'est la raison du choix de MongoDB.
    // Ids fixes, pour tester les stocks et les commandes avec un materialId connu.
    @Bean
    CommandLineRunner seedMaterials(MaterialRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.saveAll(List.of(
                    new Material(id(1), "Ciment CEM II 42,5", "sac", new BigDecimal("12.90"), "ciment",
                            Map.of("classe", "42,5 N", "poidsSacKg", 50)),
                    new Material(id(2), "Fer à béton HA12", "barre", new BigDecimal("28.40"), "acier",
                            Map.of("diametreMm", 12, "nuance", "B500B", "longueurM", 12)),
                    new Material(id(3), "Madrier sapin 63x175", "m", new BigDecimal("9.75"), "bois",
                            Map.of("essence", "sapin", "sectionMm", "63x175", "traitement", "classe 2"))));
        };
    }

    private static UUID id(int n) {
        return UUID.fromString("b0000000-0000-0000-0000-00000000000" + n);
    }
}
