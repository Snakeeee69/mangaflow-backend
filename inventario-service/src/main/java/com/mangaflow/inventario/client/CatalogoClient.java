package com.mangaflow.inventario.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CatalogoClient {

    private final WebClient webClient;

    public CatalogoClient(@Value("${servicios.catalogo.url:http://catalogo-service:8083}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
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