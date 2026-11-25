package com.dynamika.buildwise.search.config;

import com.dynamika.buildwise.search.enums.SearchType;
import com.meilisearch.sdk.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")   // les tests tournent sans Meilisearch
public class SeedData {
    // Plus tard les index seront remplis par les événements ; pour l'instant, par Swagger et ces cinq projets.
    // addDocuments remplace les documents de même id : relancer le service ne crée pas de doublons.
    // Meilisearch doit tourner avant le service, sinon le démarrage échoue ici.
    private static final String PROJECTS = """
            [
              {"id": "d0000000-0000-0000-0000-000000000001", "name": "Résidence Les Jasmins", "status": "IN_PROGRESS"},
              {"id": "d0000000-0000-0000-0000-000000000002", "name": "Pont de Radès, réfection", "status": "IN_PROGRESS"},
              {"id": "d0000000-0000-0000-0000-000000000003", "name": "École primaire El Menzah", "status": "PLANNED"},
              {"id": "d0000000-0000-0000-0000-000000000004", "name": "Entrepôt logistique Sfax Sud", "status": "DONE"},
              {"id": "d0000000-0000-0000-0000-000000000005", "name": "Clinique Les Oliviers", "status": "PLANNED"}
            ]
            """;

    // Meilisearch traite les tâches dans l'ordre : les index existent avant l'ajout des projets.
    // Un index déjà créé fait échouer sa tâche (index_already_exists) sans lever d'exception ici.
    @Bean
    public CommandLineRunner seedIndexes(Client client) {
        return args -> {
            for (SearchType t : SearchType.values()) {
                client.createIndex(t.index(), "id");
            }
            client.waitForTask(client.index(SearchType.PROJECTS.index()).addDocuments(PROJECTS, "id").getTaskUid());
        };
    }
}
