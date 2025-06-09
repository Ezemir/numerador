package br.gov.ce.cgd.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.gov.ce.cgd.entity.Usuario;

@Controller
public class LoginController {
	
	private String errorMessage;

	@GetMapping("/login")
	public String loginPage(@AuthenticationPrincipal Usuario usuarioLogado, HttpSession session, Model model) {
	    if (usuarioLogado != null) {
	    		model.addAttribute("usuarioLogado", usuarioLogado);
	    }

	   session.getAttribute("errorMessage");
	    if (errorMessage != null) {
	        model.addAttribute("erro", errorMessage);
	        session.removeAttribute("errorMessage");
	    }
	    return "login";
	}


}
