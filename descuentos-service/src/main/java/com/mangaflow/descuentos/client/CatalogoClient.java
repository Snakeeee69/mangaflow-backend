package com.mangaflow.descuentos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CatalogoClient {

    private final WebClient webClient;

    public CatalogoClient(@Value("${catalogo.service.url:http://catalogo-service:8083}") String catalogoServiceUrl) {
        this.webClient = WebClient.create(catalogoServiceUrl);
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