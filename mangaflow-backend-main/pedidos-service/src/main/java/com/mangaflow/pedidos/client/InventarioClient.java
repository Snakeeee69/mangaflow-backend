package com.mangaflow.pedidos.client;

import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.stereotype.Component;

@Component
public class InventarioClient {

    private final WebClient webClient;

    public InventarioClient() {
        this.webClient = WebClient.create("http://localhost:8084");
    }

    public boolean hayStockDisponible(Long mangaId, Integer cantidad) {
        try {
            Boolean disponible = webClient.get()
                    .uri("/mangas/inventario/disponible/" + mangaId + "/" + cantidad)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(disponible);
        } catch (Exception e) {
            return false;
        }
    }

    public void descontarStock(Long mangaId, Integer cantidad) {
        webClient.post()
                .uri("/mangas/inventario/descontar/" + mangaId + "/" + cantidad)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
