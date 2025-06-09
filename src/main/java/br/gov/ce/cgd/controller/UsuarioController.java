package br.gov.ce.cgd.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.gov.ce.cgd.entity.Usuario;
import br.gov.ce.cgd.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/adicionar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/adicionar";
    }

    @PostMapping
    public String criarUsuario(@Valid @ModelAttribute Usuario usuario, Model model) {
        try {
            usuario.setDataCadastro(LocalDateTime.now());
            usuarioService.salvarUsuario(usuario);
            return "redirect:/dashboard"; 
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao salvar usuário: " + e.getMessage());
            return "usuarios/adicionar";
        }
    }

    @GetMapping
    public String listarUsuarios(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Usuario> usuarios = usuarioService.listarTodos(pageable);
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuarios.getTotalPages());
        model.addAttribute("totalItems", usuarios.getTotalElements());

        return "usuarios/listar"; 
    }

    @GetMapping("/{id}")
    public String buscarUsuarioPorId(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        usuario.ifPresent(u -> model.addAttribute("usuario", u));
        return usuario.isPresent() ? "usuarios/editar" : "redirect:/usuarios";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuarios/editar"; 
        }
        return "redirect:/usuarios";
    }

    @PutMapping("/{id}")
    public String editarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuario, String novaSenha) {
        usuarioService.atualizarUsuario(id, usuario, novaSenha);
        return "redirect:/usuarios";
    }

    @DeleteMapping("/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/buscarPorNome")
    public String buscarPorNome(@RequestParam String nome, Pageable pageable, Model model) {
        Page<Usuario> usuarios = usuarioService.buscarPorNome(nome, pageable);
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar";
    }

    @GetMapping("/buscarPorCpf")
    public String buscarPorCpf(@RequestParam String cpf, Pageable pageable, Model model) {
        Page<Usuario> usuarios = usuarioService.buscarPorCpf(cpf, pageable);
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar";
    }
    @GetMapping("/alterar-senha")
    public String alterarSenha(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            Optional<Usuario> usuario = usuarioService.findById(usuarioId);
            if (usuario.isPresent()) {
                model.addAttribute("usuario", usuario.get());
                return "usuarios/alterar-senha"; 
            }
        }
        
        return "redirect:/usuarios"; 
    }
    
    @PostMapping("/alterar-senha")
    public String alterarSenha(@RequestParam String senhaAtual,
                               @RequestParam String novaSenha,
                               @RequestParam(name = "confirmarSenha") String confirmarNovaSenha,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuário não autenticado.");
            return "redirect:/login"; 
        }

        try {
            usuarioService.alterarSenha(usuarioId, senhaAtual, novaSenha, confirmarNovaSenha);
            redirectAttributes.addFlashAttribute("successMessage", "Senha alterada com sucesso.");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("incorreta")) {
                redirectAttributes.addFlashAttribute("errorMessage", "A senha atual está incorreta.");
            } else if (e.getMessage().contains("não coincidem")) {
                redirectAttributes.addFlashAttribute("errorMessage", "A nova senha e a confirmação não coincidem.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Erro ao alterar a senha.");
            }
        }

        return "redirect:/dashboard";
    }
    

//  @PostMapping("/{id}/enable2fa")
//  public String enable2FA(@PathVariable Long id, Model model) {
//      String qrCodeUrl = usuarioService.enable2FA(id);
//      model.addAttribute("qrCodeUrl", qrCodeUrl);
//      model.addAttribute("successMessage", "QR Code gerado e enviado para o e-mail!");
//      return "redirect:/usuarios";  
//  }
//
//  @GetMapping(value = "/{id}/qrcode", produces = "image/png")
//  public ResponseEntity<byte[]> getQRCode(@PathVariable Long id) throws Exception {
//      byte[] qrCodeImage = usuarioService.generateQRCode(id);
//      return ResponseEntity.ok().body(qrCodeImage);
//  }
//
//  @PostMapping("/{id}/validar")
//  public String validate2FA(@PathVariable Long id, @RequestParam int code, Model model) {
//      boolean isValid = usuarioService.validate2FA(id, code);
//      model.addAttribute("isValid", isValid);
//      return isValid ? "redirect:/solicitar" : "usuarios/login";
//  }


}
