package br.uniesp.si.techback.service;

import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Lista todos os usuários cadastrados
     */
    public List<Usuario> listar() {
        log.info("Buscando todos os usuários cadastrados");
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            log.debug("Total de usuários encontrados: {}", usuarios.size());
            return usuarios;
        } catch (Exception e) {
            log.error("Falha ao buscar usuários: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Busca um usuário por ID
     * @param id o UUID do usuário
     * @return o usuário encontrado, ou lança uma exceção se não existir
     */
    public Usuario buscarPorId(UUID id) {
        log.info("Buscando usuário pelo ID: {}", id);
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    log.debug("Usuário encontrado: ID={}, Nome={}", usuario.getId(), usuario.getNomeCompleto());
                    return usuario;
                })
                .orElseThrow(() -> {
                    String mensagem = String.format("Usuário não encontrado com o ID: %s", id);
                    log.warn(mensagem);
                    return new RuntimeException(mensagem);
                });
    }

    /**
     * Salva um novo usuário
     * @param usuario o usuário a ser salvo
     * @return o usuário salvo com ID gerado
     */
    @Transactional
    public Usuario salvar(Usuario usuario) {
        log.info("Salvando novo usuário com email: {}", usuario.getEmail());
        
        // Validar se email já existe
        usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(u -> {
            String mensagem = "Email já cadastrado: " + usuario.getEmail();
            log.warn(mensagem);
            throw new RuntimeException(mensagem);
        });

        // Validar se CPF/CNPJ já existe
        if (usuario.getCpfCnpj() != null && !usuario.getCpfCnpj().isEmpty()) {
            usuarioRepository.findByCpfCnpj(usuario.getCpfCnpj()).ifPresent(u -> {
                String mensagem = "CPF/CNPJ já cadastrado: " + usuario.getCpfCnpj();
                log.warn(mensagem);
                throw new RuntimeException(mensagem);
            });
        }

        try {
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            log.info("Usuário salvo com sucesso. ID: {}, Nome: {}", usuarioSalvo.getId(), usuarioSalvo.getNomeCompleto());
            return usuarioSalvo;
        } catch (Exception e) {
            log.error("Erro ao salvar usuário: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Atualiza um usuário existente
     * @param id o UUID do usuário a atualizar
     * @param usuarioAtualizado os dados atualizados
     * @return o usuário atualizado
     */
    @Transactional
    public Usuario atualizar(UUID id, Usuario usuarioAtualizado) {
        log.info("Atualizando usuário com ID: {}", id);
        
        Usuario usuario = buscarPorId(id);

        // Validar se novo email já existe em outro usuário
        if (!usuario.getEmail().equals(usuarioAtualizado.getEmail())) {
            usuarioRepository.findByEmail(usuarioAtualizado.getEmail()).ifPresent(u -> {
                String mensagem = "Email já cadastrado: " + usuarioAtualizado.getEmail();
                log.warn(mensagem);
                throw new RuntimeException(mensagem);
            });
        }

        try {
            usuario.setNomeCompleto(usuarioAtualizado.getNomeCompleto());
            usuario.setDataNascimento(usuarioAtualizado.getDataNascimento());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setSenhaHash(usuarioAtualizado.getSenhaHash());
            usuario.setCpfCnpj(usuarioAtualizado.getCpfCnpj());
            usuario.setPerfil(usuarioAtualizado.getPerfil());

            Usuario usuarioAtualizado2 = usuarioRepository.save(usuario);
            log.info("Usuário ID {} atualizado com sucesso", id);
            return usuarioAtualizado2;
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Deleta um usuário
     * @param id o UUID do usuário a deletar
     */
    @Transactional
    public void excluir(UUID id) {
        log.info("Excluindo usuário com ID: {}", id);
        
        Usuario usuario = buscarPorId(id);
        
        try {
            usuarioRepository.deleteById(id);
            log.info("Usuário ID {} excluído com sucesso. Nome: {}", id, usuario.getNomeCompleto());
        } catch (Exception e) {
            log.error("Erro ao excluir usuário ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}

