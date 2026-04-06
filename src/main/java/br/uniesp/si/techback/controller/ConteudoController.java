package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.model.Conteudo;
import br.uniesp.si.techback.service.ConteudoService;
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
@RequestMapping("/conteudos")
@RequiredArgsConstructor
@Slf4j
public class ConteudoController {

    private final ConteudoService conteudoService;

    /**
     * Endpoint 1: GET /conteudos
     * Lista todos os conteúdos
     */
    @GetMapping
    public ResponseEntity<List<Conteudo>> listar() {
        log.info("Requisição para listar todos os conteúdos");
        try {
            List<Conteudo> conteudos = conteudoService.listar();
            log.debug("Total de conteúdos retornados: {}", conteudos.size());
            return ResponseEntity.ok(conteudos);
        } catch (Exception e) {
            log.error("Erro ao listar conteúdos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint 2: GET /conteudos/{id}
     * Busca um conteúdo por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Conteudo> buscarPorId(@PathVariable UUID id) {
        log.info("Requisição para buscar conteúdo com ID: {}", id);
        try {
            Conteudo conteudo = conteudoService.buscarPorId(id);
            log.debug("Conteúdo encontrado: {}", conteudo.getTitulo());
            return ResponseEntity.ok(conteudo);
        } catch (Exception e) {
            log.error("Erro ao buscar conteúdo com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 3: POST /conteudos
     * Cria um novo conteúdo
     */
    @PostMapping
    public ResponseEntity<Conteudo> criar(@Valid @RequestBody Conteudo conteudo) {
        log.info("Requisição para criar novo conteúdo: {}", conteudo.getTitulo());
        try {
            Conteudo conteudoSalvo = conteudoService.salvar(conteudo);
            log.info("Conteúdo criado com sucesso. ID: {}, Título: {}", conteudoSalvo.getId(), conteudoSalvo.getTitulo());
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(conteudoSalvo.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(conteudoSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar conteúdo: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4: PUT /conteudos/{id}
     * Atualiza um conteúdo existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Conteudo> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody Conteudo conteudo) {
        log.info("Requisição para atualizar conteúdo com ID: {}", id);
        try {
            Conteudo conteudoAtualizado = conteudoService.atualizar(id, conteudo);
            log.info("Conteúdo ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(conteudoAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar conteúdo ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 5: DELETE /conteudos/{id}
     * Deleta um conteúdo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        log.info("Requisição para deletar conteúdo com ID: {}", id);
        try {
            conteudoService.excluir(id);
            log.info("Conteúdo ID {} deletado com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar conteúdo com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}

