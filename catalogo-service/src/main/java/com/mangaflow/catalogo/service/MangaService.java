package com.mangaflow.catalogo.service;

import com.mangaflow.catalogo.dto.MangaRequest;
import com.mangaflow.catalogo.dto.MangaResponse;
import com.mangaflow.catalogo.model.Manga;
import com.mangaflow.catalogo.repository.MangaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MangaService {

    private final MangaRepository mangaRepository;

    public MangaService(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public List<MangaResponse> listarTodos() {
        return mangaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MangaResponse> listarActivos() {
        return mangaRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MangaResponse buscarPorId(Long id) {
        return toResponse(obtenerManga(id));
    }

    public MangaResponse crear(MangaRequest request) {
        if (mangaRepository.existsByTituloIgnoreCaseAndNumeroTomo(request.getTitulo(), request.getNumeroTomo())) {
            throw new IllegalArgumentException("Ya existe ese manga con el mismo número de tomo");
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
                .activo(request.getActivo() == null ? true : request.getActivo())
                .build();

        return toResponse(mangaRepository.save(manga));
    }

    public MangaResponse actualizar(Long id, MangaRequest request) {
        Manga manga = obtenerManga(id);

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

        return toResponse(mangaRepository.save(manga));
    }

    public void eliminar(Long id) {
        Manga manga = obtenerManga(id);
        mangaRepository.delete(manga);
    }

    public MangaResponse desactivar(Long id) {
        Manga manga = obtenerManga(id);
        manga.setActivo(false);
        return toResponse(mangaRepository.save(manga));
    }

    public List<MangaResponse> buscarPorTitulo(String titulo) {
        return mangaRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MangaResponse> buscarPorAutor(String autor) {
        return mangaRepository.findByAutorContainingIgnoreCase(autor)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MangaResponse> buscarPorGenero(String genero) {
        return mangaRepository.findByGeneroIgnoreCase(genero)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MangaResponse> buscarPorDemografia(String demografia) {
        return mangaRepository.findByDemografiaIgnoreCase(demografia)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean existeManga(Long id) {
        return mangaRepository.existsById(id);
    }

    private Manga obtenerManga(Long id) {
        return mangaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un manga con el ID: " + id));
    }

    private MangaResponse toResponse(Manga manga) {
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
                .activo(manga.getActivo())
                .build();
    }
}
