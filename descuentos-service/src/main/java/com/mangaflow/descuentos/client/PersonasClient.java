package com.mangaflow.descuentos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PersonasClient {

    private final WebClient webClient;

    public PersonasClient(@Value("${personas.service.url:http://personas-service:8081}") String personasServiceUrl) {
        this.webClient = WebClient.create(personasServiceUrl);
    }

    public boolean existePersona(Long personaId) {
        try {
            Boolean existe = webClient.get()
                    .uri("/mangas/personas/" + personaId + "/existe")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(existe);
        } catch (Exception e) {
            return false;
        }
    }
}