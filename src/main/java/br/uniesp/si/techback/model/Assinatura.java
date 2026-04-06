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
@Table(name = "assinatura")
@EqualsAndHashCode(of = "id")
public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Usuário é obrigatório")
    @Column(nullable = false)
    private UUID usuarioId;

    @NotNull(message = "Plano é obrigatório")
    @Column(nullable = false)
    private UUID planoId;

    @Column(nullable = false, length = 20)
    private String status = "ATIVA";  // ATIVA, EM_ATRASO, CANCELADA

    @Column(nullable = false)
    private LocalDateTime iniciadaEm;

    @Column(nullable = true)
    private LocalDateTime canceladaEm;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.iniciadaEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}

