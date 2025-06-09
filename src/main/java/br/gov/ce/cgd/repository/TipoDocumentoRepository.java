package br.gov.ce.cgd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.ce.cgd.entity.TipoDocumento;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {

	@Query("SELECT t FROM TipoDocumento t WHERE t.documentoAtivo = true ORDER BY t.nome ASC")
    List<TipoDocumento> findByDocumentoAtivoTrue();
}
