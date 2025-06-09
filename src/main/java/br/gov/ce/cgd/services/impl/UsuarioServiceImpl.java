package br.gov.ce.cgd.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import br.gov.ce.cgd.entity.Usuario;
import br.gov.ce.cgd.enumerate.Perfil;
import br.gov.ce.cgd.enumerate.Status;
import br.gov.ce.cgd.repository.UsuarioRepository;
import br.gov.ce.cgd.service.EmailService;
import br.gov.ce.cgd.service.GoogleAuthenticatorService;
import br.gov.ce.cgd.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleAuthenticatorService googleAuthenticatorService;
    private final EmailService emailService;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             GoogleAuthenticatorService googleAuthenticatorService,
                             EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.googleAuthenticatorService = googleAuthenticatorService;
        this.emailService = emailService;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        if (usuario.getPerfil() == null && usuario.getStatus() == null) {
            usuario.setPerfil(Perfil.USER);
            usuario.setStatus(Status.ATIVO);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    @Override
    public Page<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
    
  @Override
  public List<Usuario> listarTodos() {
      return usuarioRepository.findAllUsuariosOrderById();
  }

    @Override
    public Page<Usuario> buscarPorNome(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContaining(nome, pageable);
    }

    @Override
    public Page<Usuario> buscarPorCpf(String cpf, Pageable pageable) {
        return usuarioRepository.findByCpfContaining(cpf, pageable);
    }

    @Override
    public Usuario atualizarUsuario(Long id, Usuario usuario, String senhaAtual) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        if (senhaAtual != null && !senhaAtual.isEmpty()) {
            if (!passwordEncoder.matches(senhaAtual, usuarioExistente.getSenha())) {
                throw new RuntimeException("Senha atual está incorreta.");
            }
        }

        if (usuario.getNome() != null && !usuario.getNome().isEmpty()) {
            usuarioExistente.setNome(usuario.getNome());
        }
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
            usuarioExistente.setEmail(usuario.getEmail());
        }

        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        if (usuario.getPerfil() != null) {
            usuarioExistente.setPerfil(usuario.getPerfil());
        }

        usuarioExistente.setStatus(usuario.getStatus());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void alterarSenha(Long id, String senhaAtual, String novaSenha, String confirmarNovaSenha) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        if (!passwordEncoder.matches(senhaAtual, usuarioExistente.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        if (!novaSenha.equals(confirmarNovaSenha)) {
            throw new RuntimeException("As senhas não coincidem.");
        }

        usuarioExistente.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuarioExistente);
    }

    // Novo método para alterar a senha do usuário logado
    public void alterarSenhaUsuarioLogado(String senhaAtual, String novaSenha, String confirmarNovaSenha) {
        Usuario usuario = getUsuarioLogado();

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        if (!novaSenha.equals(confirmarNovaSenha)) {
            throw new RuntimeException("As senhas não coincidem.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    // Método para obter o usuário logado do contexto de segurança
    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }
        String cpf = authentication.getName();
        return usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado no banco"));
    }

    public void atualizarDataLogin(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setDataLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }

    @Override
    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public String enable2FA(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        GoogleAuthenticatorKey key = googleAuthenticatorService.generateSecretKey();
        usuario.setCodigoValidador(key.getKey());
        usuarioRepository.save(usuario);

        try {
            sendQRCodeEmail(id);
        } catch (IOException | WriterException | MessagingException e) {
            throw new RuntimeException("Erro ao enviar o QR Code por e-mail.", e);
        }

        return googleAuthenticatorService.getQRCodeUrl("Numerador", usuario.getEmail(), key);
    }

    @Override
    public boolean validate2FA(Long id, int codigoValidador) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        return googleAuthenticatorService.validateCode(usuario.getCodigoValidador(), codigoValidador);
    }

    @Override
    public void disable2FA(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuario.setCodigoValidador(null);
        usuarioRepository.save(usuario);
    }

    @Override
    public byte[] generateQRCode(Long id) throws IOException, WriterException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        String qrCodeUrl = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                "Numerador",
                usuario.getEmail(),
                usuario.getCodigoValidador(),
                "Numerador"
        );

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getCpf() == null || usuario.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
    }

    public boolean validarSenha(Usuario usuario, String senha) {
        return passwordEncoder.matches(senha, usuario.getSenha());
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o CPF: " + cpf));

        if (usuario.getStatus() == Status.INATIVO) {
            throw new DisabledException("Usuário inativo. Entre em contato com o suporte.");
        }
        if (usuario.getStatus() == Status.BLOQUEADO) {
            throw new LockedException("Usuário bloqueado. Entre em contato com o suporte.");
        }
        if (usuario.getStatus() == null) {
            throw new LockedException("Entre em contato com o suporte.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getPerfil() == Perfil.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (usuario.getPerfil() == Perfil.USER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }else if (usuario.getPerfil() == Perfil.MANAGER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        }

        return User.builder()
                .username(usuario.getCpf())
                .password(usuario.getSenha())
                .authorities(authorities)
                .build();
    }

//    public Optional<Usuario> findBycodigoValidador(int codigoValidador) {
//        return usuarioRepository.findByCodigoValidador(codigoValidador);
//    }

    public void sendQRCodeEmail(Long id) throws IOException, WriterException, MessagingException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        byte[] qrCodeImage = generateQRCode(id);

        String subject = "Seu QR Code para 2FA";
        String text = String.format("Olá %s, \n\nAqui está o seu QR Code para ativação do 2FA.", usuario.getNome());

        emailService.sendEmailWithQRCode(usuario.getEmail(), subject, text, qrCodeImage);
    }
}
