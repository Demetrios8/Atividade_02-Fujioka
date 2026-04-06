package br.uniesp.si.techback.service;

import br.uniesp.si.techback.model.Favorito;
import br.uniesp.si.techback.repository.FavoritoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public List<Favorito> listar() {
        log.info("Buscando todos os favoritos cadastrados");
        try {
            List<Favorito> favoritos = favoritoRepository.findAll();
            log.debug("Total de favoritos encontrados: {}", favoritos.size());
            return favoritos;
        } catch (Exception e) {
            log.error("Falha ao buscar favoritos: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Favorito buscarPorId(UUID id) {
        log.info("Buscando favorito pelo ID: {}", id);
        return favoritoRepository.findById(id)
                .map(favorito -> {
                    log.debug("Favorito encontrado: ID={}, UsuarioId={}", favorito.getId(), favorito.getUsuarioId());
                    return favorito;
                })
                .orElseThrow(() -> {
                    String mensagem = String.format("Favorito não encontrado com o ID: %s", id);
                    log.warn(mensagem);
                    return new RuntimeException(mensagem);
                });
    }

    @Transactional
    public Favorito salvar(Favorito favorito) {
        log.info("Salvando novo favorito. UsuarioId: {}, ConteudoId: {}", 
                favorito.getUsuarioId(), favorito.getConteudoId());
        
        // Verificar se já existe
        favoritoRepository.findByUsuarioIdAndConteudoId(
                favorito.getUsuarioId(), 
                favorito.getConteudoId()
        ).ifPresent(f -> {
            String mensagem = "Este conteúdo já está nos favoritos deste usuário";
            log.warn(mensagem);
            throw new RuntimeException(mensagem);
        });

        try {
            Favorito favoritoSalvo = favoritoRepository.save(favorito);
            log.info("Favorito salvo com sucesso. ID: {}", favoritoSalvo.getId());
            return favoritoSalvo;
        } catch (Exception e) {
            log.error("Erro ao salvar favorito: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Favorito atualizar(UUID id, Favorito favoritoAtualizado) {
        log.info("Atualizando favorito com ID: {}", id);
        
        Favorito favorito = buscarPorId(id);

        try {
            favorito.setUsuarioId(favoritoAtualizado.getUsuarioId());
            favorito.setConteudoId(favoritoAtualizado.getConteudoId());

            Favorito favorito2 = favoritoRepository.save(favorito);
            log.info("Favorito ID {} atualizado com sucesso", id);
            return favorito2;
        } catch (Exception e) {
            log.error("Erro ao atualizar favorito ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void excluir(UUID id) {
        log.info("Excluindo favorito com ID: {}", id);
        
        Favorito favorito = buscarPorId(id);
        
        try {
            favoritoRepository.deleteById(id);
            log.info("Favorito ID {} excluído com sucesso", id);
        } catch (Exception e) {
            log.error("Erro ao excluir favorito ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Busca favoritos de um usuário específico
     */
    public List<Favorito> buscarPorUsuario(UUID usuarioId) {
        log.info("Buscando favoritos do usuário: {}", usuarioId);
        try {
            List<Favorito> favoritos = favoritoRepository.findByUsuarioId(usuarioId);
            log.debug("Total de favoritos encontrados para o usuário {}: {}", usuarioId, favoritos.size());
            return favoritos;
        } catch (Exception e) {
            log.error("Erro ao buscar favoritos do usuário {}: {}", usuarioId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Remove um favorito específico por usuário e conteúdo
     */
    @Transactional
    public void removerFavorito(UUID usuarioId, UUID conteudoId) {
        log.info("Removendo favorito. UsuarioId: {}, ConteudoId: {}", usuarioId, conteudoId);
        
        try {
            favoritoRepository.deleteByUsuarioIdAndConteudoId(usuarioId, conteudoId);
            log.info("Favorito removido com sucesso");
        } catch (Exception e) {
            log.error("Erro ao remover favorito: {}", e.getMessage(), e);
            throw e;
        }
    }
}

