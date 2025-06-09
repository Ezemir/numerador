package br.gov.ce.cgd.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.gov.ce.cgd.enumerate.Perfil;
import br.gov.ce.cgd.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true,  length = 11)
    private String cpf;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @Enumerated(EnumType.STRING)
    private Status status;
    
    private LocalDateTime dataCadastro;
    private LocalDateTime dataLogin;
    

    @Column(name = "codigo_validador")
    private String codigoValidador;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
}
