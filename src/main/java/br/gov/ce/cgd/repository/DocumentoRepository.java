package br.gov.ce.cgd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.ce.cgd.entity.Documento;
import br.gov.ce.cgd.entity.TipoDocumento;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    Optional<Documento> findTopByTipoDocumentoOrderByNumeroDesc(TipoDocumento tipoDocumento);

    @Query("SELECT MAX(d.numero) FROM Documento d WHERE d.tipoDocumento.id = :tipoDocumentoId AND FUNCTION('YEAR', d.dataCadastro) = :ano")
    Integer findMaxNumeroByTipoDocumentoAndAno(@Param("tipoDocumentoId") Long tipoDocumentoId, @Param("ano") int ano);

    @Query("SELECT d.tipoDocumento.nome, COUNT(d) FROM Documento d GROUP BY d.tipoDocumento.nome ORDER BY COUNT(d) DESC")
    List<Object[]> contarDocumentosPorTipo();
    
    @Query("SELECT d.cartorio.nome, COUNT(d) FROM Documento d GROUP BY d.cartorio.nome ORDER BY d.cartorio.nome ASC")
    List<Object[]> contarTotalDocumentosPorCartorio();
    
    List<Documento> findByOrderByDataCadastroDesc(Pageable pageable);

}
