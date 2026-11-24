package com.mangaflow.pedidos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class InventarioClient {

    private final WebClient webClient;

    public InventarioClient(@Value("${servicios.inventario.url:http://inventario-service:8084}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public boolean hayStockDisponible(Long mangaId, Integer cantidad) {
        try {
            Boolean disponible = webClient.get()
                    .uri("/mangas/inventario/disponible/" + mangaId + "/" + cantidad)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(disponible);
        } catch (WebClientResponseException.NotFound e) {
            // Si el manga aún no tiene registro en inventario-service (ej. tomo digital), permite continuar
            System.out.println("Manga ID " + mangaId + " sin registro en inventario-service; omitiendo validación de stock.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al consultar inventario-service: " + e.getMessage());
            return false;
        }
    }

    public void descontarStock(Long mangaId, Integer cantidad) {
        try {
            webClient.post()
                    .uri("/mangas/inventario/descontar/" + mangaId + "/" + cantidad)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            System.out.println("Manga ID " + mangaId + " sin registro en inventario-service; no se descontó stock.");
        }
    }
}