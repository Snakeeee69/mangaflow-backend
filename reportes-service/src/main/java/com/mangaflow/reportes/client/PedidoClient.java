package com.mangaflow.reportes.client;

import com.mangaflow.reportes.dto.PedidoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Component
public class PedidoClient {

    private final WebClient webClient;

    public PedidoClient() {
        this.webClient = WebClient.create("http://localhost:8085");
    }

    public List<PedidoDTO> obtenerPedidos() {
        try {
            return webClient.get()
                    .uri("/mangas/pedidos")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<PedidoDTO>>() {})
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
