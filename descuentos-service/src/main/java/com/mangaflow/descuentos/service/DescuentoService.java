package com.mangaflow.descuentos.service;

import com.mangaflow.descuentos.client.CatalogoClient;
import com.mangaflow.descuentos.client.PersonasClient;
import com.mangaflow.descuentos.dto.CalcularDescuentoRequest;
import com.mangaflow.descuentos.dto.DescuentoResponse;
import com.mangaflow.descuentos.model.Descuento;
import com.mangaflow.descuentos.model.TipoDescuento;
import com.mangaflow.descuentos.repository.DescuentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;
    private final PersonasClient personasClient;
    private final CatalogoClient catalogoClient;

    public DescuentoService(
            DescuentoRepository descuentoRepository,
            PersonasClient personasClient,
            CatalogoClient catalogoClient
    ) {
        this.descuentoRepository = descuentoRepository;
        this.personasClient = personasClient;
        this.catalogoClient = catalogoClient;
    }

    public List<DescuentoResponse> listarDescuentos() {
        return descuentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DescuentoResponse buscarPorId(Long id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe descuento con ID: " + id));

        return toResponse(descuento);
    }

    public List<DescuentoResponse> buscarPorPersona(Long personaId) {
        return descuentoRepository.findByPersonaId(personaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DescuentoResponse> buscarPorManga(Long mangaId) {
        return descuentoRepository.findByMangaId(mangaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DescuentoResponse calcularDescuento(CalcularDescuentoRequest request) {

        if (!personasClient.existePersona(request.getPersonaId())) {
            throw new IllegalArgumentException("No se puede calcular descuento: la persona no existe");
        }

        if (!catalogoClient.existeManga(request.getMangaId())) {
            throw new IllegalArgumentException("No se puede calcular descuento: el manga no existe");
        }

        double porcentaje = obtenerPorcentaje(request.getTipoDescuento());
        double montoDescuento = request.getPrecioOriginal() * (porcentaje / 100);
        double precioFinal = request.getPrecioOriginal() - montoDescuento;

        Descuento descuento = Descuento.builder()
                .personaId(request.getPersonaId())
                .mangaId(request.getMangaId())
                .precioOriginal(request.getPrecioOriginal())
                .porcentajeDescuento(porcentaje)
                .montoDescuento(montoDescuento)
                .precioFinal(precioFinal)
                .tipoDescuento(request.getTipoDescuento())
                .motivo(obtenerMotivo(request.getTipoDescuento()))
                .fechaCalculo(LocalDateTime.now())
                .activo(true)
                .build();

        descuentoRepository.save(descuento);

        return toResponse(descuento);
    }

    public boolean existeDescuento(Long id) {
        return descuentoRepository.existsByIdAndActivoTrue(id);
    }

    public void eliminarDescuento(Long id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe descuento con ID: " + id));

        descuento.setActivo(false);
        descuentoRepository.save(descuento);
    }

    private double obtenerPorcentaje(TipoDescuento tipoDescuento) {
        return switch (tipoDescuento) {
            case DIGITAL_A_FISICO -> 30.0;
            case PROMOCION -> 15.0;
            case PREVENTA -> 20.0;
            case NINGUNO -> 0.0;
        };
    }

    private String obtenerMotivo(TipoDescuento tipoDescuento) {
        return switch (tipoDescuento) {
            case DIGITAL_A_FISICO -> "Descuento por compra previa de versión digital";
            case PROMOCION -> "Descuento por promoción activa";
            case PREVENTA -> "Descuento aplicado por preventa";
            case NINGUNO -> "No se aplicó descuento";
        };
    }

    private DescuentoResponse toResponse(Descuento descuento) {
        return DescuentoResponse.builder()
                .id(descuento.getId())
                .personaId(descuento.getPersonaId())
                .mangaId(descuento.getMangaId())
                .precioOriginal(descuento.getPrecioOriginal())
                .porcentajeDescuento(descuento.getPorcentajeDescuento())
                .montoDescuento(descuento.getMontoDescuento())
                .precioFinal(descuento.getPrecioFinal())
                .tipoDescuento(descuento.getTipoDescuento())
                .motivo(descuento.getMotivo())
                .fechaCalculo(descuento.getFechaCalculo())
                .activo(descuento.getActivo())
                .build();
    }
}
