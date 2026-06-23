package com.mangaflow.inventario.service;

import com.mangaflow.inventario.client.CatalogoClient;
import com.mangaflow.inventario.dto.InventarioRequest;
import com.mangaflow.inventario.dto.InventarioResponse;
import com.mangaflow.inventario.model.Inventario;
import com.mangaflow.inventario.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final CatalogoClient catalogoClient;

    public InventarioService(InventarioRepository inventarioRepository, CatalogoClient catalogoClient) {
        this.inventarioRepository = inventarioRepository;
        this.catalogoClient = catalogoClient;
    }

    public List<InventarioResponse> listarTodos() {
        return inventarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InventarioResponse> listarActivos() {
        return inventarioRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InventarioResponse buscarPorId(Long id) {
        return toResponse(obtenerPorId(id));
    }

    public InventarioResponse buscarPorMangaId(Long mangaId) {
        return toResponse(obtenerPorMangaId(mangaId));
    }

    public InventarioResponse crear(InventarioRequest request) {
        if (!catalogoClient.existeManga(request.getMangaId())) {
            throw new IllegalArgumentException("No se puede registrar inventario: el manga no existe en catálogo");
        }

        if (inventarioRepository.existsByMangaId(request.getMangaId())) {
            throw new IllegalArgumentException("Ya existe inventario registrado para ese manga");
        }

        Inventario inventario = Inventario.builder()
                .mangaId(request.getMangaId())
                .stock(request.getStock())
                .stockMinimo(request.getStockMinimo())
                .activo(request.getActivo() == null ? true : request.getActivo())
                .build();

        return toResponse(inventarioRepository.save(inventario));
    }

    public InventarioResponse actualizar(Long id, InventarioRequest request) {
        Inventario inventario = obtenerPorId(id);

        if (!catalogoClient.existeManga(request.getMangaId())) {
            throw new IllegalArgumentException("No se puede actualizar inventario: el manga no existe en catálogo");
        }

        inventario.setMangaId(request.getMangaId());
        inventario.setStock(request.getStock());
        inventario.setStockMinimo(request.getStockMinimo());

        return toResponse(inventarioRepository.save(inventario));
    }

    public void eliminar(Long id) {
        Inventario inventario = obtenerPorId(id);
        inventarioRepository.delete(inventario);
    }

    public InventarioResponse desactivar(Long id) {
        Inventario inventario = obtenerPorId(id);
        inventario.setActivo(false);
        return toResponse(inventarioRepository.save(inventario));
    }

    public boolean hayStockDisponible(Long mangaId, Integer cantidad) {
        Inventario inventario = obtenerPorMangaId(mangaId);
        return inventario.getStock() >= cantidad;
    }

    public InventarioResponse descontarStock(Long mangaId, Integer cantidad) {
        Inventario inventario = obtenerPorMangaId(mangaId);

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a 0");
        }

        if (inventario.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el manga ID: " + mangaId);
        }

        inventario.setStock(inventario.getStock() - cantidad);
        return toResponse(inventarioRepository.save(inventario));
    }

    public InventarioResponse agregarStock(Long mangaId, Integer cantidad) {
        Inventario inventario = obtenerPorMangaId(mangaId);

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor a 0");
        }

        inventario.setStock(inventario.getStock() + cantidad);
        return toResponse(inventarioRepository.save(inventario));
    }

    public List<InventarioResponse> stockBajo() {
        return inventarioRepository.findAll()
                .stream()
                .filter(item -> item.getStock() <= item.getStockMinimo())
                .map(this::toResponse)
                .toList();
    }

    private Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe registro de inventario con ID: " + id));
    }

    private Inventario obtenerPorMangaId(Long mangaId) {
        return inventarioRepository.findByMangaId(mangaId)
                .orElseThrow(() -> new RuntimeException("No existe inventario para el manga ID: " + mangaId));
    }

    private InventarioResponse toResponse(Inventario inventario) {
        return InventarioResponse.builder()
                .id(inventario.getId())
                .mangaId(inventario.getMangaId())
                .stock(inventario.getStock())
                .stockMinimo(inventario.getStockMinimo())
                .activo(inventario.getActivo())
                .build();
    }
}
