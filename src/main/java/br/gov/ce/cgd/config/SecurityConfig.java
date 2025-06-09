package br.gov.ce.cgd.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.gov.ce.cgd.entity.Usuario;
import br.gov.ce.cgd.enumerate.Perfil;
import br.gov.ce.cgd.services.impl.UsuarioServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UsuarioServiceImpl usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/**/css/**", "/**/javascript/**", "/**/images/**", "/login/**").permitAll()
                .antMatchers("/numerador/dashboard/**").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().authenticated()
            .and()
            .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/logout");
                })
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            .and()
            .csrf().disable();
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            String username = authentication.getName();
            Usuario usuario = usuarioService.findByCpf(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

//            String codigoValidador = request.getParameter("codigoValidador");
//
//            try {
//                int codigo = Integer.parseInt(codigoValidador);
//                if (!usuarioService.validate2FA(usuario.getId(), codigo)) {
//                    SecurityContextHolder.clearContext();
//                    response.sendRedirect("/numerador/login?error=2fa");
//                    return;
//                }
//            } catch (NumberFormatException e) {
//                SecurityContextHolder.clearContext();
//                response.sendRedirect("/numerador/login?error=2fa");
//                return;
//            }

            usuarioService.atualizarDataLogin(usuario.getId());

            HttpSession session = request.getSession();
            session.setAttribute("usuarioId", usuario.getId());

            if (usuario.getPerfil() == Perfil.ADMIN) {
                response.sendRedirect("/numerador/dashboard/");
            } else if (usuario.getPerfil() == Perfil.USER) {
                response.sendRedirect("/numerador/dashboard/");
	        }
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
