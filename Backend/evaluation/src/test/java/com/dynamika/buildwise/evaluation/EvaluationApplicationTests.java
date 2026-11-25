package com.dynamika.buildwise.evaluation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Profil "test" : lit src/test/resources/application-test.properties
// par-dessus le fichier principal (pas d'Eureka pendant les tests).
@SpringBootTest
@ActiveProfiles("test")
class EvaluationApplicationTests {

    @Test
    void contextLoads() {
    }

}
