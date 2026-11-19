package com.mangaflow.puntos.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PagosClient {

    private final WebClient webClient;

    public PagosClient() {
        this.webClient = WebClient.create("http://localhost:8086");
    }

    public boolean existePago(Long pagoId) {
        try {
            Boolean existe = webClient.get()
                    .uri("/mangas/pagos/" + pagoId + "/existe")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(existe);
        } catch (Exception e) {
            return false;
        }
    }

    public PagoInfo obtenerPago(Long pagoId) {
        try {
            return webClient.get()
                    .uri("/mangas/pagos/" + pagoId)
                    .retrieve()
                    .bodyToMono(PagoInfo.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    @Getter
    @Setter
    public static class PagoInfo {
        private Long id;
        private Long pedidoId;
        private Double monto;
        private String metodoPago;
        private String estado;
        private Boolean activo;
    }
}
