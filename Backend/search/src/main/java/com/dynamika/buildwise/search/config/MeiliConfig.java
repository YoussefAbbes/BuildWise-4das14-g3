package com.dynamika.buildwise.search.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeiliConfig {
    @Bean
    Client meiliClient(@Value("${buildwise.meili.url}") String url, @Value("${buildwise.meili.api-key}") String key) {
        return new Client(new Config(url, key));
    }
}
