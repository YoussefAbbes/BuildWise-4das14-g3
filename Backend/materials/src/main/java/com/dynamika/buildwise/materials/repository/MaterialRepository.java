package com.dynamika.buildwise.materials.repository;

import com.dynamika.buildwise.materials.model.Material;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaterialRepository extends MongoRepository<Material, UUID> {}

