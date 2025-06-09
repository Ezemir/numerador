package br.gov.ce.cgd.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.gov.ce.cgd.entity.Cartorio;
import br.gov.ce.cgd.service.CartorioService;

@Controller
@RequestMapping("/cartorios")
public class CartorioController {

    private final CartorioService cartorioService;

    public CartorioController(CartorioService cartorioService) {
        this.cartorioService = cartorioService;
    }

    @GetMapping
    public ResponseEntity<List<Cartorio>> listarTodos() {
        return ResponseEntity.ok(cartorioService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Cartorio>> listarAtivos() {
        return ResponseEntity.ok(cartorioService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cartorio> buscarPorId(@PathVariable Long id) {
        Cartorio cartorio = cartorioService.buscarPorId(id);
        return ResponseEntity.ok(cartorio);
    }

    @PostMapping
    public String criar(@ModelAttribute Cartorio cartorio) {
        cartorio.setCartorioAtivo(true);
        cartorioService.salvar(cartorio);
        return "redirect:/dashboard";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cartorio> atualizar(@PathVariable Long id, @RequestBody Cartorio cartorioAtualizado) {
        Cartorio cartorio = cartorioService.atualizar(id, cartorioAtualizado);
        return ResponseEntity.ok(cartorio);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        cartorioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cartorioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/gerar-com-cartorio")
    public String gerarNumeracaoComCartorio(@RequestParam Long tipoDocumentoId, 
                                           @RequestParam Long cartorioId, 
                                           Principal principal) {
//        documentoService.gerarNovaNumeracaoComCartorio(tipoDocumentoId, cartorioId, principal.getName());
        return "redirect:/dashboard";
    }

}
