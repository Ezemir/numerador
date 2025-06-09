package br.gov.ce.cgd.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.zxing.WriterException;

import br.gov.ce.cgd.entity.Usuario;

public interface UsuarioService extends UserDetailsService {

    Usuario salvarUsuario(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCpf(String cpf);
    Usuario atualizarUsuario(Long id, Usuario usuario, String SenhaAtual);
    Page<Usuario> listarTodos(Pageable pageable);
    void deletarUsuario(Long id);
//    String enable2FA(Long id);
//    boolean validate2FA(Long id, int codigoValidador);
//    void disable2FA(Long id);
//    byte[] generateQRCode(Long id) throws IOException, WriterException;
    boolean validarSenha(Usuario usuario, String senha);
	void alterarSenha(Long id, String senhaAtual, String novaSenha, String confirmarNovaSenha);
	Page<Usuario> buscarPorNome(String nome, Pageable pageable);
	Page<Usuario> buscarPorCpf(String cpf, Pageable pageable);
	List<Usuario> listarTodos();
}
