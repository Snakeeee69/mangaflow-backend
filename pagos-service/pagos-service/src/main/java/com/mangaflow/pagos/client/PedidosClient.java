package com.mangaflow.pagos.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PedidosClient {

    private final WebClient webClient;

    public PedidosClient() {
        this.webClient = WebClient.create("http://localhost:8085");
    }

    public boolean existePedido(Long pedidoId) {
        try {
            Boolean existe = webClient.get()
                    .uri("/mangas/pedidos/" + pedidoId + "/existe")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(existe);
        } catch (Exception e) {
            return false;
        }
    }
}
