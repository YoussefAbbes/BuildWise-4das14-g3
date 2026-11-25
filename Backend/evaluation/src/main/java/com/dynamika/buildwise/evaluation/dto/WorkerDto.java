package com.dynamika.buildwise.evaluation.dto;

import java.util.UUID;

// Partie lue de WorkerResponse (workforce) : les autres champs sont ignorés.
public record WorkerDto(UUID id, String fullName) {}
