package com.mangaflow.inventario.service;

import com.mangaflow.inventario.client.CatalogoClient;
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

    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }

    public List<Inventario> listarActivos() {
        return inventarioRepository.findByActivoTrue();
    }

    public Inventario buscarPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe registro de inventario con ID: " + id));
    }

    public Inventario buscarPorMangaId(Long mangaId) {
        return inventarioRepository.findByMangaId(mangaId)
                .orElseThrow(() -> new RuntimeException("No existe inventario para el manga ID: " + mangaId));
    }

    public Inventario crear(Inventario inventario) {
        if (!catalogoClient.existeManga(inventario.getMangaId())) {
            throw new RuntimeException("No se puede registrar inventario: el manga no existe en catálogo");
        }

        if (inventarioRepository.existsByMangaId(inventario.getMangaId())) {
            throw new RuntimeException("Ya existe inventario registrado para ese manga");
        }

        if (inventario.getActivo() == null) {
            inventario.setActivo(true);
        }

        return inventarioRepository.save(inventario);
    }

    public Inventario actualizar(Long id, Inventario datosNuevos) {
        Inventario inventario = buscarPorId(id);

        if (!catalogoClient.existeManga(datosNuevos.getMangaId())) {
            throw new RuntimeException("No se puede actualizar inventario: el manga no existe en catálogo");
        }

        inventario.setMangaId(datosNuevos.getMangaId());
        inventario.setStock(datosNuevos.getStock());
        inventario.setStockMinimo(datosNuevos.getStockMinimo());

        return inventarioRepository.save(inventario);
    }

    public void eliminar(Long id) {
        Inventario inventario = buscarPorId(id);
        inventarioRepository.delete(inventario);
    }

    public Inventario desactivar(Long id) {
        Inventario inventario = buscarPorId(id);
        inventario.setActivo(false);
        return inventarioRepository.save(inventario);
    }

    public boolean hayStockDisponible(Long mangaId, Integer cantidad) {
        Inventario inventario = buscarPorMangaId(mangaId);
        return inventario.getStock() >= cantidad;
    }

    public Inventario descontarStock(Long mangaId, Integer cantidad) {
        Inventario inventario = buscarPorMangaId(mangaId);

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad a descontar debe ser mayor a 0");
        }

        if (inventario.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el manga ID: " + mangaId);
        }

        inventario.setStock(inventario.getStock() - cantidad);
        return inventarioRepository.save(inventario);
    }

    public Inventario agregarStock(Long mangaId, Integer cantidad) {
        Inventario inventario = buscarPorMangaId(mangaId);

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad a agregar debe ser mayor a 0");
        }

        inventario.setStock(inventario.getStock() + cantidad);
        return inventarioRepository.save(inventario);
    }

    public List<Inventario> stockBajo() {
        return inventarioRepository.findAll()
                .stream()
                .filter(item -> item.getStock() <= item.getStockMinimo())
                .toList();
    }
}
