package com.dynamika.buildwise.evaluation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

// Application complète, sans Eureka (profil test). workforce pointe vers http://localhost:1
// (application-test.properties) : l'appel Feign échoue, comme quand workforce est arrêté.
// Ces deux cas s'arrêtent avant MongoDB : aucun avis n'est écrit.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewControllerTest {
    private static final String VALID = """
            {"workerId":"a0000000-0000-0000-0000-000000000001","taskId":"e0000000-0000-0000-0000-000000000099",
             "projectId":"d0000000-0000-0000-0000-000000000001","score":4,"comment":"essai",
             "authorId":"f0000000-0000-0000-0000-000000000001"}""";

    @Autowired
    MockMvc mvc;

    @Test
    void invalidScoreIsRejectedWithTheFieldName() throws Exception {
        mvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON).content(VALID.replace("\"score\":4", "\"score\":9")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.score").exists());
    }

    @Test
    void workforceDownGives503() throws Exception {
        mvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isServiceUnavailable());
    }
}
