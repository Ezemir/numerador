package br.gov.ce.cgd.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.gov.ce.cgd.entity.Documento;
import br.gov.ce.cgd.service.DocumentoService;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

//    @PostMapping("/gerar/{tipoDocumentoId}/{cartorioId}")
//    public String gerarNumeracao(@PathVariable Long tipoDocumentoId, @PathVariable Long cartorioId, Principal principal) {
//        documentoService.gerarNovaNumeracao(tipoDocumentoId, cartorioId, principal.getName());
//        return "redirect:/dashboard";
//    }
    
    @PostMapping("/gerar")
    public String gerarNumeracao(
            @RequestParam Long tipoDocumentoId, 
            @RequestParam Long cartorioId, 
            Principal principal) {
        documentoService.gerarNovaNumeracao(tipoDocumentoId, cartorioId, principal.getName());
        return "redirect:/dashboard";
    }
    
    @GetMapping("/ultimos")
    @ResponseBody
    public List<Documento> getUltimosDocumentos() {
        return documentoService.buscarUltimosDocumentos(5);
    }
    
}
