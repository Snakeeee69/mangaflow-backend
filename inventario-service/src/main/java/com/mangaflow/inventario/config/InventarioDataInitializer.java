package com.mangaflow.inventario.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangaflow.inventario.model.Inventario;
import com.mangaflow.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

@Component
public class InventarioDataInitializer implements CommandLineRunner {

    private final InventarioRepository inventarioRepository;
    private final WebClient catalogoWebClient;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record MangaResumen(Long id) {}

    public InventarioDataInitializer(
            InventarioRepository inventarioRepository,
            @Value("${servicios.catalogo.url:http://catalogo-service:8083}") String catalogoUrl
    ) {
        this.inventarioRepository = inventarioRepository;
        this.catalogoWebClient = WebClient.create(catalogoUrl);
    }

    @Override
    public void run(String... args) {
        if (inventarioRepository.count() > 0) {
            return;
        }

        List<Long> mangaIds = obtenerIdsDesdeCatalogo();

        for (Long mangaId : mangaIds) {
            if (!inventarioRepository.existsByMangaId(mangaId)) {
                inventarioRepository.save(Inventario.builder()
                        .mangaId(mangaId)
                        .stock(30)
                        .stockMinimo(5)
                        .activo(true)
                        .build());
            }
        }

        System.out.println("Inventario inicializado con stock para " + mangaIds.size() + " mangas.");
    }

    private List<Long> obtenerIdsDesdeCatalogo() {
        try {
            MangaResumen[] mangas = catalogoWebClient.get()
                    .uri("/mangas/catalogo")
                    .retrieve()
                    .bodyToMono(MangaResumen[].class)
                    .block();

            if (mangas != null && mangas.length > 0) {
                return Arrays.stream(mangas)
                        .map(MangaResumen::id)
                        .filter(Objects::nonNull)
                        .toList();
            }
        } catch (Exception e) {
            System.out.println("No se pudo leer catalogo-service al iniciar; usando IDs 1 al 22 por defecto.");
        }
        return LongStream.rangeClosed(1, 22).boxed().toList();
    }
}