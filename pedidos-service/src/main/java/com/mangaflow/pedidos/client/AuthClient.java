package com.mangaflow.pedidos.client;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthClient {

    private final WebClient webClient;

    public AuthClient() {
        // Enlazamos directamente con el nombre del servicio registrado en Eureka
        this.webClient = WebClient.create("http://auth-service");
    }

    public boolean existeUsuario(Long usuarioId) {
        try {
            Boolean existe = webClient.get()
                    .uri("/mangas/personas/" + usuarioId + "/existe")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(existe);
        } catch (Exception e) {
            System.err.println("Error al comunicarse con auth-service: " + e.getMessage());
            return false;
        }
    }
}