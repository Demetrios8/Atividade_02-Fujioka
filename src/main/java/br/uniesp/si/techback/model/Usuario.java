package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 150, message = "Nome completo deve ter no máximo 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nomeCompleto;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    @Size(max = 254, message = "E-mail deve ter no máximo 254 caracteres")
    @Column(nullable = false, length = 254, unique = true)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    @Column(nullable = false, length = 255)
    private String senhaHash;

    @Column(length = 14, unique = true)
    private String cpfCnpj;

    @Column(nullable = false, length = 20)
    private String perfil = "USER";  // ADMIN ou USER

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

