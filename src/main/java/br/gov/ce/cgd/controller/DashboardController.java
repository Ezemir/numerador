package br.gov.ce.cgd.controller;

import java.security.Principal;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.gov.ce.cgd.entity.Cartorio;
import br.gov.ce.cgd.entity.TipoDocumento;
import br.gov.ce.cgd.entity.Usuario;
import br.gov.ce.cgd.service.CartorioService;
import br.gov.ce.cgd.service.DocumentoService;
import br.gov.ce.cgd.service.TipoDocumentoService;
import br.gov.ce.cgd.service.UsuarioService;
import br.gov.ce.cgd.util.Chart;

@Controller
public class DashboardController {

    private final TipoDocumentoService tipoDocumentoService;
    private final DocumentoService documentoService;
    private final CartorioService cartorioService;
    private final UsuarioService usuarioService;

    public DashboardController(
            TipoDocumentoService tipoDocumentoService,
            DocumentoService documentoService,
            CartorioService cartorioService,
            UsuarioService usuarioService) {

        this.tipoDocumentoService = tipoDocumentoService;
        this.documentoService = documentoService;
        this.cartorioService = cartorioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            Principal principal,
            // Recebe parâmetros de paginação da aba usuários, com valores padrão
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        // Listar tipos de documentos ativos
        List<TipoDocumento> tipos = tipoDocumentoService.listarAtivos();

        // Obter última numeração para cada tipo
        Map<Long, Object> ultimasNumeracoes = tipos.stream()
                .collect(Collectors.toMap(
                        TipoDocumento::getId,
                        tipo -> documentoService.obterUltimaNumeracao(tipo.getId())
                ));

        // Dados para gráfico documentos por tipo
        List<Object[]> dadosGraficos = documentoService.contarDocumentosPorTipo();
        Map<String, List<?>> listasGraficos = Chart.separarNomesEQuantidades(dadosGraficos);

        // Dados para gráfico documentos por cartório
        List<Object[]> dadosCartorios = documentoService.contarDocumentosPorCartorio();
        Map<String, List<?>> listasGraficosCartorio = Chart.separarNomesEQuantidades(dadosCartorios);

        // Listar cartórios ativos
        List<Cartorio> cartorios = cartorioService.listarAtivos();

        // Obter usuário logado
        Usuario usuario = usuarioService.findByCpf(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o CPF: " + principal.getName()));

        // Paginação da lista de usuários para a aba usuários
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuarios = usuarioService.listarTodos(pageable);

        // Atributos para a view (dashboard + aba usuários)
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioPerfil", usuario.getPerfil().name());
        model.addAttribute("tiposDocumentos", tipos);
        model.addAttribute("ultimasNumeracoes", ultimasNumeracoes);
        model.addAttribute("anoAtual", Year.now().getValue());
        model.addAttribute("novoTipoDocumento", new TipoDocumento());
        model.addAttribute("novoUsuario", new Usuario());
        model.addAttribute("novoCartorio", new Cartorio());
        model.addAttribute("cartorios", cartorios);

        // Dados gráficos para documentos por tipo
        model.addAttribute("nomesGraficos", listasGraficos.get("nomes"));
        model.addAttribute("quantidadesGraficos", listasGraficos.get("quantidades"));

        // Dados gráficos para documentos por cartório
        model.addAttribute("nomesCartorios", listasGraficosCartorio.get("nomes"));
        model.addAttribute("quantidadesCartorios", listasGraficosCartorio.get("quantidades"));

        // Dados para paginação da aba usuários
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuarios.getTotalPages());

        return "dashboard";
    }
}
