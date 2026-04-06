package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConteudoRepository extends JpaRepository<Conteudo, UUID> {
    List<Conteudo> findByTipo(String tipo);
    List<Conteudo> findByGenero(String genero);
    List<Conteudo> findByTituloContainingIgnoreCase(String titulo);
}

