package com.mangaflow.pedidos.client;

import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.stereotype.Component;

@Component
public class CatalogoClient {

    private final WebClient webClient;

    public CatalogoClient() {
        this.webClient = WebClient.create("http://localhost:8083");
    }

    public boolean existeManga(Long mangaId) {
        try {
            Boolean existe = webClient.get()
                    .uri("/mangas/catalogo/" + mangaId + "/existe")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(existe);
        } catch (Exception e) {
            return false;
        }
    }
}
