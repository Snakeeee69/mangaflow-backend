package com.mangaflow.catalogo.service;

import com.mangaflow.catalogo.dto.MangaRequest;
import com.mangaflow.catalogo.dto.MangaResponse;
import com.mangaflow.catalogo.exception.BusinessRuleException;
import com.mangaflow.catalogo.exception.ResourceNotFoundException;
import com.mangaflow.catalogo.model.Manga;
import com.mangaflow.catalogo.repository.MangaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MangaService {

    private final MangaRepository mangaRepository;

    public MangaService(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public List<MangaResponse> listarTodos() {
        return mangaRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<MangaResponse> listarActivos() {
        return mangaRepository.findByActivoTrue().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public MangaResponse buscarPorIdDto(Long id) {
        return mapToResponse(buscarPorIdEntity(id));
    }

    public Manga buscarPorIdEntity(Long id) {
        return mangaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un manga con el ID: " + id));
    }

    public MangaResponse crear(MangaRequest request) {
        if (mangaRepository.existsByTituloIgnoreCaseAndNumeroTomo(request.getTitulo(), request.getNumeroTomo())) {
            throw new BusinessRuleException("Ya existe ese manga con el mismo número de tomo");
        }

        Manga manga = Manga.builder()
                .titulo(request.getTitulo())
                .autor(request.getAutor())
                .editorial(request.getEditorial())
                .genero(request.getGenero())
                .demografia(request.getDemografia())
                .sinopsis(request.getSinopsis())
                .numeroTomo(request.getNumeroTomo())
                .precioFisico(request.getPrecioFisico())
                .precioDigital(request.getPrecioDigital())
                .formato(request.getFormato())
                .imagen(request.getImagen() != null && !request.getImagen().isBlank()
                        ? request.getImagen()
                        : "/images/logo/logoMangaFlow.png")
                .stock(request.getStock() != null ? request.getStock() : 10)
                .proximamenteFisico(Boolean.TRUE.equals(request.getProximamenteFisico()))
                .activo(true)
                .build();

        return mapToResponse(mangaRepository.save(manga));
    }

    public MangaResponse actualizar(Long id, MangaRequest request) {
        Manga manga = buscarPorIdEntity(id);
        manga.setTitulo(request.getTitulo());
        manga.setAutor(request.getAutor());
        manga.setEditorial(request.getEditorial());
        manga.setGenero(request.getGenero());
        manga.setDemografia(request.getDemografia());
        manga.setSinopsis(request.getSinopsis());
        manga.setNumeroTomo(request.getNumeroTomo());
        manga.setPrecioFisico(request.getPrecioFisico());
        manga.setPrecioDigital(request.getPrecioDigital());
        manga.setFormato(request.getFormato());
        if (request.getImagen() != null && !request.getImagen().isBlank()) {
            manga.setImagen(request.getImagen());
        }
        if (request.getStock() != null) {
            manga.setStock(request.getStock());
        }
        if (request.getProximamenteFisico() != null) {
            manga.setProximamenteFisico(request.getProximamenteFisico());
        }

        return mapToResponse(mangaRepository.save(manga));
    }

    public void eliminar(Long id) {
        Manga manga = buscarPorIdEntity(id);
        mangaRepository.delete(manga);
    }

    public MangaResponse desactivar(Long id) {
        Manga manga = buscarPorIdEntity(id);
        manga.setActivo(false);
        return mapToResponse(mangaRepository.save(manga));
    }

    public MangaResponse activar(Long id) {
        Manga manga = buscarPorIdEntity(id);
        manga.setActivo(true);
        return mapToResponse(mangaRepository.save(manga));
    }

    public List<MangaResponse> buscarPorTitulo(String titulo) {
        return mangaRepository.findByTituloContainingIgnoreCase(titulo).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<MangaResponse> buscarPorAutor(String autor) {
        return mangaRepository.findByAutorContainingIgnoreCase(autor).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<MangaResponse> buscarPorGenero(String genero) {
        return mangaRepository.findByGeneroIgnoreCase(genero).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<MangaResponse> buscarPorDemografia(String demografia) {
        return mangaRepository.findByDemografiaIgnoreCase(demografia).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public boolean existeManga(Long id) {
        return mangaRepository.existsById(id);
    }

    private MangaResponse mapToResponse(Manga manga) {
        return MangaResponse.builder()
                .id(manga.getId())
                .titulo(manga.getTitulo())
                .autor(manga.getAutor())
                .editorial(manga.getEditorial())
                .genero(manga.getGenero())
                .demografia(manga.getDemografia())
                .sinopsis(manga.getSinopsis())
                .numeroTomo(manga.getNumeroTomo())
                .precioFisico(manga.getPrecioFisico())
                .precioDigital(manga.getPrecioDigital())
                .formato(manga.getFormato())
                .imagen(manga.getImagen())
                .stock(manga.getStock())
                .proximamenteFisico(manga.getProximamenteFisico())
                .activo(manga.getActivo())
                .build();
    }
}