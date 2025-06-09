package br.gov.ce.cgd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.gov.ce.cgd.entity.TipoDocumento;
import br.gov.ce.cgd.service.TipoDocumentoService;

@Controller
@RequestMapping("/tipos-documentos")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @GetMapping({"", "/", "/listar"})
    public String listarTipos(Model model) {
        model.addAttribute("tiposDocumentos", tipoDocumentoService.listarAtivos());
        model.addAttribute("novoTipoDocumento", new TipoDocumento());
        return "tipos-documentos/listarTiposDocumentos"; 
    }
    
    @GetMapping("/cadastrar")
    public String cadastrar() {
		return "tipos-documentos/listarTiposDocumentos";
    }

    @PostMapping
    public String criarTipo(@ModelAttribute TipoDocumento tipoDocumento) {
        tipoDocumento.setDocumentoAtivo(true);
        tipoDocumentoService.salvar(tipoDocumento);
        return "redirect:/dashboard";
    }

    @ResponseBody
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        boolean atualizado = tipoDocumentoService.alterarStatus(id, ativo);
        if (atualizado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
