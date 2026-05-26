package com.mangaflow.catalogo.service;

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

    public List<Manga> listarTodos() {
        return mangaRepository.findAll();
    }

    public List<Manga> listarActivos() {
        return mangaRepository.findByActivoTrue();
    }

    public Manga buscarPorId(Long id) {
        return mangaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un manga con el ID: " + id));
    }

    public Manga crear(Manga manga) {
        if (mangaRepository.existsByTituloIgnoreCaseAndNumeroTomo(manga.getTitulo(), manga.getNumeroTomo())) {
            throw new RuntimeException("Ya existe ese manga con el mismo número de tomo");
        }

        if (manga.getActivo() == null) {
            manga.setActivo(true);
        }

        return mangaRepository.save(manga);
    }

    public Manga actualizar(Long id, Manga datosNuevos) {
        Manga manga = buscarPorId(id);

        manga.setTitulo(datosNuevos.getTitulo());
        manga.setAutor(datosNuevos.getAutor());
        manga.setEditorial(datosNuevos.getEditorial());
        manga.setGenero(datosNuevos.getGenero());
        manga.setDemografia(datosNuevos.getDemografia());
        manga.setSinopsis(datosNuevos.getSinopsis());
        manga.setNumeroTomo(datosNuevos.getNumeroTomo());
        manga.setPrecioFisico(datosNuevos.getPrecioFisico());
        manga.setPrecioDigital(datosNuevos.getPrecioDigital());
        manga.setFormato(datosNuevos.getFormato());

        return mangaRepository.save(manga);
    }

    public void eliminar(Long id) {
        Manga manga = buscarPorId(id);
        mangaRepository.delete(manga);
    }

    public Manga desactivar(Long id) {
        Manga manga = buscarPorId(id);
        manga.setActivo(false);
        return mangaRepository.save(manga);
    }

    public List<Manga> buscarPorTitulo(String titulo) {
        return mangaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Manga> buscarPorAutor(String autor) {
        return mangaRepository.findByAutorContainingIgnoreCase(autor);
    }

    public List<Manga> buscarPorGenero(String genero) {
        return mangaRepository.findByGeneroIgnoreCase(genero);
    }

    public List<Manga> buscarPorDemografia(String demografia) {
        return mangaRepository.findByDemografiaIgnoreCase(demografia);
    }

    public boolean existeManga(Long id) {
        return mangaRepository.existsById(id);
    }
}
