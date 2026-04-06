package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "favorito", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "conteudo_id"})
})
@EqualsAndHashCode(of = "id")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Usuário é obrigatório")
    @Column(nullable = false, name = "usuario_id")
    private UUID usuarioId;

    @NotNull(message = "Conteúdo é obrigatório")
    @Column(nullable = false, name = "conteudo_id")
    private UUID conteudoId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }
}

