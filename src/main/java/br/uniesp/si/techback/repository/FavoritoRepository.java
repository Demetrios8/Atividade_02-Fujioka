package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, UUID> {
    List<Favorito> findByUsuarioId(UUID usuarioId);
    Optional<Favorito> findByUsuarioIdAndConteudoId(UUID usuarioId, UUID conteudoId);
    void deleteByUsuarioIdAndConteudoId(UUID usuarioId, UUID conteudoId);
}

