package br.gov.ce.cgd.config;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.gov.ce.cgd.entity.Usuario;
import br.gov.ce.cgd.services.impl.UsuarioServiceImpl;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UsuarioServiceImpl usuarioService;

    // Injeta a URL do datasource do application.properties
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public GlobalModelAttributes(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId != null) {
            Usuario usuario = usuarioService.findById(usuarioId).orElse(null);
            if (usuario != null) {
                model.addAttribute("usuarioId", usuario.getId());
                model.addAttribute("usuarioLogado", usuario.getNome());
            }
        }

        model.addAttribute("databaseUrl", datasourceUrl);

        if (datasourceUrl != null && datasourceUrl.contains("/")) {
            String[] parts = datasourceUrl.split("/");
            String dbName = parts[parts.length - 1].split("\\?")[0];
            model.addAttribute("databaseName", dbName);
        }
    }
}
