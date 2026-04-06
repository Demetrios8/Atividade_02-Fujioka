    package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.model.Assinatura;
import br.uniesp.si.techback.service.AssinaturaService;
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
@RequestMapping("/assinaturas")
@RequiredArgsConstructor
@Slf4j
public class AssinaturaController {

    private final AssinaturaService assinaturaService;

    /**
     * Endpoint 1: GET /assinaturas
     * Lista todas as assinaturas
     */
    @GetMapping
    public ResponseEntity<List<Assinatura>> listar() {
        log.info("Requisição para listar todas as assinaturas");
        try {
            List<Assinatura> assinaturas = assinaturaService.listar();
            log.debug("Total de assinaturas retornadas: {}", assinaturas.size());
            return ResponseEntity.ok(assinaturas);
        } catch (Exception e) {
            log.error("Erro ao listar assinaturas: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint 2: GET /assinaturas/{id}
     * Busca uma assinatura por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Assinatura> buscarPorId(@PathVariable UUID id) {
        log.info("Requisição para buscar assinatura com ID: {}", id);
        try {
            Assinatura assinatura = assinaturaService.buscarPorId(id);
            log.debug("Assinatura encontrada: {}", assinatura.getStatus());
            return ResponseEntity.ok(assinatura);
        } catch (Exception e) {
            log.error("Erro ao buscar assinatura com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 3: POST /assinaturas
     * Cria uma nova assinatura
     */
    @PostMapping
    public ResponseEntity<Assinatura> criar(@Valid @RequestBody Assinatura assinatura) {
        log.info("Requisição para criar nova assinatura para usuário: {}", assinatura.getUsuarioId());
        try {
            Assinatura assinaturaSalva = assinaturaService.salvar(assinatura);
            log.info("Assinatura criada com sucesso. ID: {}", assinaturaSalva.getId());
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(assinaturaSalva.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(assinaturaSalva);
        } catch (Exception e) {
            log.error("Erro ao criar assinatura: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4: PUT /assinaturas/{id}
     * Atualiza uma assinatura existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Assinatura> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody Assinatura assinatura) {
        log.info("Requisição para atualizar assinatura com ID: {}", id);
        try {
            Assinatura assinaturaAtualizada = assinaturaService.atualizar(id, assinatura);
            log.info("Assinatura ID {} atualizada com sucesso", id);
            return ResponseEntity.ok(assinaturaAtualizada);
        } catch (Exception e) {
            log.error("Erro ao atualizar assinatura ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 5: DELETE /assinaturas/{id}
     * Deleta uma assinatura
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        log.info("Requisição para deletar assinatura com ID: {}", id);
        try {
            assinaturaService.excluir(id);
            log.info("Assinatura ID {} deletada com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar assinatura com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint Extra: PATCH /assinaturas/{id}/cancelar
     * Cancela uma assinatura
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Assinatura> cancelar(@PathVariable UUID id) {
        log.info("Requisição para cancelar assinatura com ID: {}", id);
        try {
            Assinatura assinaturaCancelada = assinaturaService.cancelar(id);
            log.info("Assinatura ID {} cancelada com sucesso", id);
            return ResponseEntity.ok(assinaturaCancelada);
        } catch (Exception e) {
            log.error("Erro ao cancelar assinatura ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}

