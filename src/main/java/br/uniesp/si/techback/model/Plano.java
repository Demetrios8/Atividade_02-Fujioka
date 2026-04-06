package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "plano")
@EqualsAndHashCode(of = "id")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Código do plano é obrigatório")
    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @Min(value = 1, message = "Limite diário deve ser maior que 0")
    @Column(nullable = false)
    private Integer limiteDiario;

    @Min(value = 1, message = "Streams simultâneos deve ser maior que 0")
    @Column(nullable = false)
    private Integer streamsSimultaneos;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}

