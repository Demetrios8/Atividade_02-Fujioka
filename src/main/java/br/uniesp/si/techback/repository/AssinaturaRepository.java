package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssinaturaRepository extends JpaRepository<Assinatura, UUID> {
    List<Assinatura> findByUsuarioId(UUID usuarioId);
    List<Assinatura> findByStatus(String status);
}

