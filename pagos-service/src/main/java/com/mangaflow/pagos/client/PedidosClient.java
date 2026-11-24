package com.mangaflow.pagos.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PedidosClient {

    private final WebClient webClient;

    public PedidosClient(@Value("${pedidos.service.url:http://pedidos-service:8085}") String pedidosServiceUrl) {
        this.webClient = WebClient.create(pedidosServiceUrl);
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