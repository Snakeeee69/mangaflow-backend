package com.mangaflow.auth.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PersonasClient {

    private final WebClient webClient;

    public PersonasClient() {
        this.webClient = WebClient.create("http://persona-service");
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
