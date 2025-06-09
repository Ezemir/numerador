package br.gov.ce.cgd.service;

import br.gov.ce.cgd.entity.TipoDocumento;
import br.gov.ce.cgd.repository.TipoDocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public List<TipoDocumento> listarTodos() {
        return tipoDocumentoRepository.findAll();
    }

    public List<TipoDocumento> listarAtivos() {
        return tipoDocumentoRepository.findByDocumentoAtivoTrue();
    }

    public TipoDocumento buscarPorId(Long id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Documento não encontrado com ID: " + id));
    }

    @Transactional
    public TipoDocumento salvar(TipoDocumento tipoDocumento) {
        tipoDocumento.setDocumentoAtivo(true); 
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    @Transactional
    public TipoDocumento atualizar(Long id, TipoDocumento tipoDocumentoAtualizado) {
        TipoDocumento tipoDocumento = buscarPorId(id);
        tipoDocumento.setNome(tipoDocumentoAtualizado.getNome());
        tipoDocumento.setDocumentoAtivo(tipoDocumentoAtualizado.isDocumentoAtivo());
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    public boolean alterarStatus(Long id, boolean ativo) {
        return tipoDocumentoRepository.findById(id).map(tipo -> {
            tipo.setDocumentoAtivo(ativo);
            tipoDocumentoRepository.save(tipo);
            return true;
        }).orElse(false);
    }

    @Transactional
    public void deletar(Long id) {
        if (!tipoDocumentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de Documento não encontrado com ID: " + id);
        }
        tipoDocumentoRepository.deleteById(id);
    }
}
