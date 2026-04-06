package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.model.Favorito;
import br.uniesp.si.techback.service.FavoritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
@Slf4j
public class FavoritoController {

    private final FavoritoService favoritoService;

    /**
     * Endpoint 1: GET /favoritos
     * Lista todos os favoritos
     */
    @GetMapping
    public ResponseEntity<List<Favorito>> listar() {
        log.info("Requisição para listar todos os favoritos");
        try {
            List<Favorito> favoritos = favoritoService.listar();
            log.debug("Total de favoritos retornados: {}", favoritos.size());
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            log.error("Erro ao listar favoritos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint 2: GET /favoritos/{id}
     * Busca um favorito por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Favorito> buscarPorId(@PathVariable UUID id) {
        log.info("Requisição para buscar favorito com ID: {}", id);
        try {
            Favorito favorito = favoritoService.buscarPorId(id);
            log.debug("Favorito encontrado: {}", favorito.getId());
            return ResponseEntity.ok(favorito);
        } catch (Exception e) {
            log.error("Erro ao buscar favorito com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 3: POST /favoritos
     * Cria um novo favorito
     */
    @PostMapping
    public ResponseEntity<Favorito> criar(@Valid @RequestBody Favorito favorito) {
        log.info("Requisição para criar novo favorito. UsuarioId: {}, ConteudoId: {}", 
                favorito.getUsuarioId(), favorito.getConteudoId());
        try {
            Favorito favoritoSalvo = favoritoService.salvar(favorito);
            log.info("Favorito criado com sucesso. ID: {}", favoritoSalvo.getId());
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(favoritoSalvo.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(favoritoSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar favorito: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4: PUT /favoritos/{id}
     * Atualiza um favorito existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Favorito> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody Favorito favorito) {
        log.info("Requisição para atualizar favorito com ID: {}", id);
        try {
            Favorito favoritoAtualizado = favoritoService.atualizar(id, favorito);
            log.info("Favorito ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(favoritoAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar favorito ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 5: DELETE /favoritos/{id}
     * Deleta um favorito
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        log.info("Requisição para deletar favorito com ID: {}", id);
        try {
            favoritoService.excluir(id);
            log.info("Favorito ID {} deletado com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar favorito com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint Extra: GET /favoritos/usuario/{usuarioId}
     * Lista favoritos de um usuário específico
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Favorito>> buscarPorUsuario(@PathVariable UUID usuarioId) {
        log.info("Requisição para listar favoritos do usuário: {}", usuarioId);
        try {
            List<Favorito> favoritos = favoritoService.buscarPorUsuario(usuarioId);
            log.debug("Total de favoritos retornados: {}", favoritos.size());
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            log.error("Erro ao listar favoritos do usuário {}: {}", usuarioId, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint Extra: DELETE /favoritos/usuario/{usuarioId}/conteudo/{conteudoId}
     * Remove um favorito específico
     */
    @DeleteMapping("/usuario/{usuarioId}/conteudo/{conteudoId}")
    public ResponseEntity<Void> removerFavorito(
            @PathVariable UUID usuarioId,
            @PathVariable UUID conteudoId) {
        log.info("Requisição para remover favorito. UsuarioId: {}, ConteudoId: {}", usuarioId, conteudoId);
        try {
            favoritoService.removerFavorito(usuarioId, conteudoId);
            log.info("Favorito removido com sucesso");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao remover favorito: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}

