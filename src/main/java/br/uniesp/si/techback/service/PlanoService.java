package br.uniesp.si.techback.service;

import br.uniesp.si.techback.model.Plano;
import br.uniesp.si.techback.repository.PlanoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;

    public List<Plano> listar() {
        log.info("Buscando todos os planos cadastrados");
        try {
            List<Plano> planos = planoRepository.findAll();
            log.debug("Total de planos encontrados: {}", planos.size());
            return planos;
        } catch (Exception e) {
            log.error("Falha ao buscar planos: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Plano buscarPorId(UUID id) {
        log.info("Buscando plano pelo ID: {}", id);
        return planoRepository.findById(id)
                .map(plano -> {
                    log.debug("Plano encontrado: ID={}, Código={}", plano.getId(), plano.getCodigo());
                    return plano;
                })
                .orElseThrow(() -> {
                    String mensagem = String.format("Plano não encontrado com o ID: %s", id);
                    log.warn(mensagem);
                    return new RuntimeException(mensagem);
                });
    }

    @Transactional
    public Plano salvar(Plano plano) {
        log.info("Salvando novo plano com código: {}", plano.getCodigo());
        
        planoRepository.findByCodigo(plano.getCodigo()).ifPresent(p -> {
            String mensagem = "Código de plano já existe: " + plano.getCodigo();
            log.warn(mensagem);
            throw new RuntimeException(mensagem);
        });

        try {
            Plano planoSalvo = planoRepository.save(plano);
            log.info("Plano salvo com sucesso. ID: {}, Código: {}", planoSalvo.getId(), planoSalvo.getCodigo());
            return planoSalvo;
        } catch (Exception e) {
            log.error("Erro ao salvar plano: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Plano atualizar(UUID id, Plano planoAtualizado) {
        log.info("Atualizando plano com ID: {}", id);
        
        Plano plano = buscarPorId(id);

        if (!plano.getCodigo().equals(planoAtualizado.getCodigo())) {
            planoRepository.findByCodigo(planoAtualizado.getCodigo()).ifPresent(p -> {
                String mensagem = "Código de plano já existe: " + planoAtualizado.getCodigo();
                log.warn(mensagem);
                throw new RuntimeException(mensagem);
            });
        }

        try {
            plano.setCodigo(planoAtualizado.getCodigo());
            plano.setLimiteDiario(planoAtualizado.getLimiteDiario());
            plano.setStreamsSimultaneos(planoAtualizado.getStreamsSimultaneos());

            Plano plano2 = planoRepository.save(plano);
            log.info("Plano ID {} atualizado com sucesso", id);
            return plano2;
        } catch (Exception e) {
            log.error("Erro ao atualizar plano ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void excluir(UUID id) {
        log.info("Excluindo plano com ID: {}", id);
        
        Plano plano = buscarPorId(id);
        
        try {
            planoRepository.deleteById(id);
            log.info("Plano ID {} excluído com sucesso. Código: {}", id, plano.getCodigo());
        } catch (Exception e) {
            log.error("Erro ao excluir plano ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}

