package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.model.Plano;
import br.uniesp.si.techback.service.PlanoService;
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
@RequestMapping("/planos")
@RequiredArgsConstructor
@Slf4j
public class PlanoController {

    private final PlanoService planoService;

    /**
     * Endpoint 1: GET /planos
     * Lista todos os planos
     */
    @GetMapping
    public ResponseEntity<List<Plano>> listar() {
        log.info("Requisição para listar todos os planos");
        try {
            List<Plano> planos = planoService.listar();
            log.debug("Total de planos retornados: {}", planos.size());
            return ResponseEntity.ok(planos);
        } catch (Exception e) {
            log.error("Erro ao listar planos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint 2: GET /planos/{id}
     * Busca um plano por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plano> buscarPorId(@PathVariable UUID id) {
        log.info("Requisição para buscar plano com ID: {}", id);
        try {
            Plano plano = planoService.buscarPorId(id);
            log.debug("Plano encontrado: {}", plano.getCodigo());
            return ResponseEntity.ok(plano);
        } catch (Exception e) {
            log.error("Erro ao buscar plano com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 3: POST /planos
     * Cria um novo plano
     */
    @PostMapping
    public ResponseEntity<Plano> criar(@Valid @RequestBody Plano plano) {
        log.info("Requisição para criar novo plano com código: {}", plano.getCodigo());
        try {
            Plano planoSalvo = planoService.salvar(plano);
            log.info("Plano criado com sucesso. ID: {}, Código: {}", planoSalvo.getId(), planoSalvo.getCodigo());
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(planoSalvo.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(planoSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar plano: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4: PUT /planos/{id}
     * Atualiza um plano existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plano> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody Plano plano) {
        log.info("Requisição para atualizar plano com ID: {}", id);
        try {
            Plano planoAtualizado = planoService.atualizar(id, plano);
            log.info("Plano ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(planoAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar plano ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 5: DELETE /planos/{id}
     * Deleta um plano
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        log.info("Requisição para deletar plano com ID: {}", id);
        try {
            planoService.excluir(id);
            log.info("Plano ID {} deletado com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar plano com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}

