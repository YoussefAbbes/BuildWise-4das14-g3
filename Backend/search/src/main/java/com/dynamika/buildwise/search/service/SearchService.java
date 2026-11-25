package com.dynamika.buildwise.search.service;

import com.dynamika.buildwise.search.enums.SearchType;
import com.dynamika.buildwise.search.exception.NotFoundException;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
public class SearchService implements ISearchService {
    private static final Set<String> ABSENT = Set.of("document_not_found", "index_not_found");

    private final Client client;
    private final JsonMapper json;   // le Jackson de Spring : UUID et enum en texte, comme dans les réponses REST

    public SearchService(Client client, JsonMapper json) {
        this.client = client;
        this.json = json;
    }

    @Override
    public Map<String, List<Map<String, Object>>> search(String q, SearchType type) {
        Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();
        for (SearchType t : type == null ? SearchType.values() : new SearchType[] {type}) {
            result.put(t.index(), List.copyOf(client.index(t.index()).search(q).getHits()));
        }
        return result;
    }

    // addDocuments remplace le document de même id. On attend la tâche : un GET juste après le voit.
    @Override
    public <T> T save(SearchType type, T doc) {
        String body = json.writeValueAsString(List.of(doc));
        client.waitForTask(client.index(type.index()).addDocuments(body, "id").getTaskUid());
        return doc;
    }

    @Override
    public Object find(SearchType type, UUID id) {
        try {
            return json.readValue(client.index(type.index()).getRawDocument(id.toString()), type.docClass());
        } catch (MeilisearchApiException e) {
            if (ABSENT.contains(e.getCode())) {
                throw new NotFoundException("document introuvable dans " + type.index() + " : " + id);
            }
            throw e;
        }
    }

    // Meilisearch accepte de supprimer un id absent : pas de 404 ici.
    @Override
    public void delete(SearchType type, UUID id) {
        client.waitForTask(client.index(type.index()).deleteDocument(id.toString()).getTaskUid());
    }
}
