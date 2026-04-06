package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "conteudo")
@EqualsAndHashCode(of = "id")
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "Tipo é obrigatório")
    @Column(nullable = false, length = 10)
    private String tipo; // FILME ou SERIE

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1888, message = "Ano deve ser maior que 1888")
    @Max(value = 2100, message = "Ano deve ser menor que 2100")
    @Column(nullable = false)
    private Integer ano;

    @NotNull(message = "Duração é obrigatória")
    @Min(value = 1, message = "Duração deve ser maior que 0")
    @Max(value = 999, message = "Duração deve ser menor que 999")
    @Column(nullable = false)
    private Integer duracaoMinutos;

    @NotNull(message = "Relevância é obrigatória")
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    @Digits(integer = 2, fraction = 2)
    @Column(nullable = false)
    private BigDecimal relevancia;

    @Column(columnDefinition = "TEXT")
    private String sinopse;

    @Size(max = 500, message = "URL do trailer deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String trailerUrl;

    @Size(max = 50, message = "Gênero deve ter no máximo 50 caracteres")
    @Column(length = 50)
    private String genero;

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

