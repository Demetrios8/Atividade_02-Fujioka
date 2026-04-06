package br.uniesp.si.techback.service;

import br.uniesp.si.techback.model.Conteudo;
import br.uniesp.si.techback.repository.ConteudoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;

    public List<Conteudo> listar() {
        log.info("Buscando todos os conteúdos cadastrados");
        try {
            List<Conteudo> conteudos = conteudoRepository.findAll();
            log.debug("Total de conteúdos encontrados: {}", conteudos.size());
            return conteudos;
        } catch (Exception e) {
            log.error("Falha ao buscar conteúdos: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Conteudo buscarPorId(UUID id) {
        log.info("Buscando conteúdo pelo ID: {}", id);
        return conteudoRepository.findById(id)
                .map(conteudo -> {
                    log.debug("Conteúdo encontrado: ID={}, Título={}", conteudo.getId(), conteudo.getTitulo());
                    return conteudo;
                })
                .orElseThrow(() -> {
                    String mensagem = String.format("Conteúdo não encontrado com o ID: %s", id);
                    log.warn(mensagem);
                    return new RuntimeException(mensagem);
                });
    }

    @Transactional
    public Conteudo salvar(Conteudo conteudo) {
        log.info("Salvando novo conteúdo: {}", conteudo.getTitulo());
        
        try {
            Conteudo conteudoSalvo = conteudoRepository.save(conteudo);
            log.info("Conteúdo salvo com sucesso. ID: {}, Título: {}", conteudoSalvo.getId(), conteudoSalvo.getTitulo());
            return conteudoSalvo;
        } catch (Exception e) {
            log.error("Erro ao salvar conteúdo: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Conteudo atualizar(UUID id, Conteudo conteudoAtualizado) {
        log.info("Atualizando conteúdo com ID: {}", id);
        
        Conteudo conteudo = buscarPorId(id);

        try {
            conteudo.setTitulo(conteudoAtualizado.getTitulo());
            conteudo.setTipo(conteudoAtualizado.getTipo());
            conteudo.setAno(conteudoAtualizado.getAno());
            conteudo.setDuracaoMinutos(conteudoAtualizado.getDuracaoMinutos());
            conteudo.setRelevancia(conteudoAtualizado.getRelevancia());
            conteudo.setSinopse(conteudoAtualizado.getSinopse());
            conteudo.setTrailerUrl(conteudoAtualizado.getTrailerUrl());
            conteudo.setGenero(conteudoAtualizado.getGenero());

            Conteudo conteudo2 = conteudoRepository.save(conteudo);
            log.info("Conteúdo ID {} atualizado com sucesso", id);
            return conteudo2;
        } catch (Exception e) {
            log.error("Erro ao atualizar conteúdo ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void excluir(UUID id) {
        log.info("Excluindo conteúdo com ID: {}", id);
        
        Conteudo conteudo = buscarPorId(id);
        
        try {
            conteudoRepository.deleteById(id);
            log.info("Conteúdo ID {} excluído com sucesso. Título: {}", id, conteudo.getTitulo());
        } catch (Exception e) {
            log.error("Erro ao excluir conteúdo ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}

