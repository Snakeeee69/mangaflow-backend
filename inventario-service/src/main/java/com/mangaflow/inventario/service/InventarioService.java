package com.mangaflow.inventario.service;

import com.mangaflow.inventario.client.CatalogoClient;
import com.mangaflow.inventario.dto.InventarioRequest;
import com.mangaflow.inventario.dto.InventarioResponse;
import com.mangaflow.inventario.exception.BusinessRuleException;
import com.mangaflow.inventario.exception.ResourceNotFoundException;
import com.mangaflow.inventario.model.Inventario;
import com.mangaflow.inventario.repository.InventarioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final CatalogoClient catalogoClient;

    public InventarioService(InventarioRepository inventarioRepository, CatalogoClient catalogoClient) {
        this.inventarioRepository = inventarioRepository;
        this.catalogoClient = catalogoClient;
    }

    private InventarioResponse mapToResponse(Inventario inventario) {
        return InventarioResponse.builder()
                .id(inventario.getId())
                .mangaId(inventario.getMangaId())
                .stock(inventario.getStock())
                .stockMinimo(inventario.getStockMinimo())
                .activo(inventario.getActivo())
                .build();
    }

    private Inventario obtenerEntidadPorMangaId(Long mangaId) {
        return inventarioRepository.findByMangaId(mangaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe registro en almacén para el manga con ID: " + mangaId));
    }

    public List<InventarioResponse> listarTodos() {
        return inventarioRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<InventarioResponse> listarActivos() {
        return inventarioRepository.findByActivoTrue().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public InventarioResponse buscarPorId(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe registro de inventario con ID: " + id));
        return mapToResponse(inventario);
    }

    public InventarioResponse buscarPorMangaId(Long mangaId) {
        return mapToResponse(obtenerEntidadPorMangaId(mangaId));
    }

    public InventarioResponse crear(@Valid InventarioRequest request) {
        if (inventarioRepository.existsByMangaId(request.getMangaId())) {
            throw new BusinessRuleException("Ya existe un registro de stock para el manga con ID: " + request.getMangaId());
        }

        Inventario nuevoInventario = Inventario.builder()
                .mangaId(request.getMangaId())
                .stock(request.getStock())
                .stockMinimo(request.getStockMinimo())
                .activo(true)
                .build();

        return mapToResponse(inventarioRepository.save(nuevoInventario));
    }

    public void eliminar(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. ID inexistente: " + id));
        inventarioRepository.delete(inventario);
    }

    public InventarioResponse desactivar(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede desactivar. ID inexistente: " + id));
        inventario.setActivo(false);
        return mapToResponse(inventarioRepository.save(inventario));
    }

    public boolean hayStockDisponible(Long mangaId, Integer cantidad) {
        Inventario inventario = obtenerEntidadPorMangaId(mangaId);
        return inventario.getActivo() && inventario.getStock() >= cantidad;
    }

    public InventarioResponse descontarStock(Long mangaId, Integer cantidad) {
        Inventario inventario = obtenerEntidadPorMangaId(mangaId);

        if (cantidad <= 0) {
            throw new BusinessRuleException("La cantidad a descontar debe ser mayor a cero");
        }
        if (inventario.getStock() < cantidad) {
            throw new BusinessRuleException("Stock insuficiente en almacén. Stock actual: " + inventario.getStock());
        }

        inventario.setStock(inventario.getStock() - cantidad);
        return mapToResponse(inventarioRepository.save(inventario));
    }

    public InventarioResponse agregarStock(Long mangaId, Integer cantidad) {
        Inventario inventario = obtenerEntidadPorMangaId(mangaId);

        if (cantidad <= 0) {
            throw new BusinessRuleException("La cantidad a ingresar al almacén debe ser mayor a cero");
        }

        inventario.setStock(inventario.getStock() + cantidad);
        return mapToResponse(inventarioRepository.save(inventario));
    }

    public List<InventarioResponse> stockBajo() {
        return inventarioRepository.findByActivoTrue().stream()
                .filter(i -> i.getStock() <= i.getStockMinimo())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
