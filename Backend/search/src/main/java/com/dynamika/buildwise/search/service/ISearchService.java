package com.dynamika.buildwise.search.service;

import com.dynamika.buildwise.search.enums.SearchType;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ISearchService {
    // type null : les quatre index. Résultat : nom de l'index -> documents trouvés.
    Map<String, List<Map<String, Object>>> search(String q, SearchType type);

    <T> T save(SearchType type, T doc);

    Object find(SearchType type, UUID id);

    void delete(SearchType type, UUID id);
}
