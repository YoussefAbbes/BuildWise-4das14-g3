package com.dynamika.buildwise.workforce.config;

import com.dynamika.buildwise.workforce.enums.Skill;
import com.dynamika.buildwise.workforce.model.Worker;
import com.dynamika.buildwise.workforce.repository.WorkerRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class SeedData {
    static final UUID COMPANY_1 = UUID.fromString("c0000000-0000-0000-0000-000000000001");
    static final UUID COMPANY_2 = UUID.fromString("c0000000-0000-0000-0000-000000000002");

    @Bean
    CommandLineRunner seedWorkers(WorkerRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    worker(1, COMPANY_1, "Ali Ben Salah", "18.50", Skill.MASON),
                    worker(2, COMPANY_1, "Sami Trabelsi", "22.00", Skill.ELECTRICIAN),
                    worker(3, COMPANY_1, "Karim Jaziri", "20.00", Skill.PLUMBER),
                    worker(4, COMPANY_2, "Hedi Mansour", "27.50", Skill.CRANE_OPERATOR)));
        };
    }

    private static Worker worker(int n, UUID company, String name, String rate, Skill skill) {
        return new Worker(UUID.fromString("a0000000-0000-0000-0000-00000000000" + n),
                company, name, new BigDecimal(rate), Set.of(skill));
    }
}
