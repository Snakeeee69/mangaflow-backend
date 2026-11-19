package com.mangaflow.pedidos.controller;

import com.mangaflow.pedidos.client.AuthClient;
import com.mangaflow.pedidos.dto.PedidoRequest;
import com.mangaflow.pedidos.dto.PedidoResponse;
import com.mangaflow.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mangas/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final AuthClient authClient;

    public PedidoController(PedidoService pedidoService, AuthClient authClient) {
        this.pedidoService = pedidoService;
        this.authClient = authClient;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PedidoResponse>> listarActivos() {
        return ResponseEntity.ok(pedidoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<PedidoResponse>> buscarPorPersona(@PathVariable Long personaId) {
        return ResponseEntity.ok(pedidoService.buscarPorPersona(personaId));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody PedidoRequest request) {
        boolean existeUsuario = authClient.existeUsuario(request.getPersonaId());
        if (!existeUsuario) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con ID " + request.getPersonaId() + " no existe");
        }
        PedidoResponse response = pedidoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<PedidoResponse> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.confirmar(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existePedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.existePedido(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.ok("Pedido eliminado correctamente");
    }
}
