package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.service.UsuarioService;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Endpoint 1: GET /usuarios
     * Lista todos os usuários cadastrados
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        log.info("Requisição para listar todos os usuários");
        try {
            List<Usuario> usuarios = usuarioService.listar();
            log.debug("Total de usuários retornados: {}", usuarios.size());
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            log.error("Erro ao listar usuários: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint 2: GET /usuarios/{id}
     * Busca um usuário específico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable UUID id) {
        log.info("Requisição para buscar usuário com ID: {}", id);
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            log.debug("Usuário encontrado: {}", usuario.getNomeCompleto());
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            log.error("Erro ao buscar usuário com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 3: POST /usuarios
     * Cria um novo usuário
     */
    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        log.info("Requisição para criar novo usuário com email: {}", usuario.getEmail());
        try {
            Usuario usuarioSalvo = usuarioService.salvar(usuario);
            log.info("Usuário criado com sucesso. ID: {}, Nome: {}", usuarioSalvo.getId(), usuarioSalvo.getNomeCompleto());
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(usuarioSalvo.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(usuarioSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar usuário: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4: PUT /usuarios/{id}
     * Atualiza um usuário existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody Usuario usuario) {
        log.info("Requisição para atualizar usuário com ID: {}", id);
        try {
            Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
            log.info("Usuário ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 5: DELETE /usuarios/{id}
     * Deleta um usuário
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        log.info("Requisição para deletar usuário com ID: {}", id);
        try {
            usuarioService.excluir(id);
            log.info("Usuário ID {} deletado com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar usuário com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}

