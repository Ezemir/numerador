package br.gov.ce.cgd.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long idDocumento;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    private LocalDateTime dataCadastro;

    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private TipoDocumento tipoDocumento;
    
    @ManyToOne
    private Cartorio cartorio;

    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
}
