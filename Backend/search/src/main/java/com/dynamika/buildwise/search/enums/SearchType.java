package com.dynamika.buildwise.search.enums;

import com.dynamika.buildwise.search.model.AlertDoc;
import com.dynamika.buildwise.search.model.MaterialDoc;
import com.dynamika.buildwise.search.model.ProjectDoc;
import com.dynamika.buildwise.search.model.WorkerDoc;
import java.util.Arrays;
import java.util.Optional;

// Chaque type connaît son index Meilisearch et la classe de ses documents.
public enum SearchType {
    PROJECTS("projects", ProjectDoc.class),
    WORKERS("workers", WorkerDoc.class),
    MATERIALS("materials", MaterialDoc.class),
    ALERTS("alerts", AlertDoc.class);

    private final String index;
    private final Class<?> docClass;

    SearchType(String index, Class<?> docClass) {
        this.index = index;
        this.docClass = docClass;
    }

    public String index() { return index; }

    public Class<?> docClass() { return docClass; }

    // "projects" -> PROJECTS ; vide si le nom ne correspond à aucun index.
    public static Optional<SearchType> fromIndex(String index) {
        return Arrays.stream(values()).filter(t -> t.index.equals(index)).findFirst();
    }
}
