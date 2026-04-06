package br.uniesp.si.techback.service;

import br.uniesp.si.techback.model.Assinatura;
import br.uniesp.si.techback.repository.AssinaturaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;

    public List<Assinatura> listar() {
        log.info("Buscando todas as assinaturas cadastradas");
        try {
            List<Assinatura> assinaturas = assinaturaRepository.findAll();
            log.debug("Total de assinaturas encontradas: {}", assinaturas.size());
            return assinaturas;
        } catch (Exception e) {
            log.error("Falha ao buscar assinaturas: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Assinatura buscarPorId(UUID id) {
        log.info("Buscando assinatura pelo ID: {}", id);
        return assinaturaRepository.findById(id)
                .map(assinatura -> {
                    log.debug("Assinatura encontrada: ID={}, Status={}", assinatura.getId(), assinatura.getStatus());
                    return assinatura;
                })
                .orElseThrow(() -> {
                    String mensagem = String.format("Assinatura não encontrada com o ID: %s", id);
                    log.warn(mensagem);
                    return new RuntimeException(mensagem);
                });
    }

    @Transactional
    public Assinatura salvar(Assinatura assinatura) {
        log.info("Salvando nova assinatura para usuário: {}", assinatura.getUsuarioId());
        
        try {
            Assinatura assinaturaSalva = assinaturaRepository.save(assinatura);
            log.info("Assinatura salva com sucesso. ID: {}, Usuário: {}", 
                    assinaturaSalva.getId(), assinaturaSalva.getUsuarioId());
            return assinaturaSalva;
        } catch (Exception e) {
            log.error("Erro ao salvar assinatura: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Assinatura atualizar(UUID id, Assinatura assinaturaAtualizada) {
        log.info("Atualizando assinatura com ID: {}", id);
        
        Assinatura assinatura = buscarPorId(id);

        try {
            assinatura.setStatus(assinaturaAtualizada.getStatus());
            assinatura.setPlanoId(assinaturaAtualizada.getPlanoId());

            Assinatura assinatura2 = assinaturaRepository.save(assinatura);
            log.info("Assinatura ID {} atualizada com sucesso", id);
            return assinatura2;
        } catch (Exception e) {
            log.error("Erro ao atualizar assinatura ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void excluir(UUID id) {
        log.info("Excluindo assinatura com ID: {}", id);
        
        Assinatura assinatura = buscarPorId(id);
        
        try {
            assinaturaRepository.deleteById(id);
            log.info("Assinatura ID {} excluída com sucesso", id);
        } catch (Exception e) {
            log.error("Erro ao excluir assinatura ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Cancela uma assinatura ativa
     */
    @Transactional
    public Assinatura cancelar(UUID id) {
        log.info("Cancelando assinatura com ID: {}", id);
        
        Assinatura assinatura = buscarPorId(id);
        
        if (assinatura.getCanceladaEm() != null) {
            String mensagem = "Assinatura já foi cancelada";
            log.warn(mensagem);
            throw new RuntimeException(mensagem);
        }
        
        try {
            assinatura.setStatus("CANCELADA");
            assinatura.setCanceladaEm(LocalDateTime.now());
            
            Assinatura assinaturaCancelada = assinaturaRepository.save(assinatura);
            log.info("Assinatura ID {} cancelada com sucesso", id);
            return assinaturaCancelada;
        } catch (Exception e) {
            log.error("Erro ao cancelar assinatura ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}

