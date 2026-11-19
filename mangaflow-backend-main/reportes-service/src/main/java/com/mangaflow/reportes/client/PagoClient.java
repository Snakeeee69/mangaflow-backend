package com.mangaflow.reportes.client;

import com.mangaflow.reportes.dto.PagoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Component
public class PagoClient {

    private final WebClient webClient;

    public PagoClient() {
        this.webClient = WebClient.create("http://localhost:8086");
    }

    public List<PagoDTO> obtenerPagos() {
        try {
            return webClient.get()
                    .uri("/mangas/pagos")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<PagoDTO>>() {})
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
