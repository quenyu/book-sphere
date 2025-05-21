package com.alice.book_sphere.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient externalBookWebClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)
                )
                .build();


        return WebClient.builder()
                .baseUrl("https://openlibrary.org")
                .exchangeStrategies(strategies)
                .build();
    }
}
