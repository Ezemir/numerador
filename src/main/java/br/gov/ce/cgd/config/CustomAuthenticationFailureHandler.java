package br.gov.ce.cgd.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, 
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage = "Usuário ou senha inválidos";

        if (exception.getMessage().contains("inativo")) {
            errorMessage = "Usuário inativo. Entre em contato com o suporte.";
        } else if (exception.getMessage().contains("bloqueado")) {
            errorMessage = "Usuário bloqueado. Entre em contato com o suporte.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Usuário não encontrado.";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);

        response.sendRedirect("/numerador/login?error=true");
    }
}
