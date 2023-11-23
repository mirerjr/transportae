package br.com.transportae.usuario;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.transportae.email.EmailService;
import br.com.transportae.usuario.exceptions.UsuarioExistenteException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    public UsuarioModel cadastrarUsuario(UsuarioDto usuarioDto) {
        if (isUsuarioCadastrado(usuarioDto.getEmail(), usuarioDto.getCpf())) {
            throw new UsuarioExistenteException("Usuário já existente");
        }

        UsuarioModel novoUsuario = converterDtoParaDomain(usuarioDto); 
        novoUsuario.setSenha("*****");

        return usuarioRepository.save(novoUsuario);
    }

    public void liberarAcessoUsuario(Long id) {
        String senha = gerarSenhaPrimeiroAcesso();

        UsuarioModel usuario = usuarioRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            
        usuario.setSenha(passwordEncoder.encode(senha));
        
        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuario);

        emailService.enviarEmailPrimeiroAcesso(usuarioAtualizado, senha);        
    }

    public UsuarioModel converterDtoParaDomain (UsuarioDto usuarioDto) {
        return UsuarioModel.builder()
            .matricula(usuarioDto.getMatricula())
            .dataNascimento(usuarioDto.getDataNascimento())
            .nome(usuarioDto.getNome())
            .email(usuarioDto.getEmail())
            .cpf(usuarioDto.getCpf())
            .perfil(usuarioDto.getPerfil())
            .build();
    }

    public Boolean isUsuarioCadastrado(String email, String cpf) {
        return usuarioRepository.findByEmailOrCpf(email, cpf).isPresent();
    }

    public void alterarSenha(AlterarSenhaRequest request, Principal principal) {
        UsuarioModel usuarioLogado = getUsuarioLogado(principal);

        Boolean isSenhaAtualCorreta = passwordEncoder.matches(request.getSenhaAtual(), usuarioLogado.getSenha());
        Boolean isSenhaNovaConfirmada = request.getSenhaNova().equals(request.getSenhaNovaConfirmada());

        if (!isSenhaAtualCorreta) {
            throw new IllegalStateException("Senha atual incorreta");
        }
        
        if (!isSenhaNovaConfirmada) {
            throw new IllegalStateException("As senhas não conferem");
        }

        if (Objects.isNull(usuarioLogado.getDataPrimeiroAcesso())) {
            usuarioLogado.setDataPrimeiroAcesso(LocalDateTime.now());
        }

        if (!usuarioLogado.isEmailVerificado()) {
            usuarioLogado.setEmailVerificado(true);
        }

        usuarioLogado.setSenha(passwordEncoder.encode(request.getSenhaNova()));
        usuarioRepository.save(usuarioLogado);
    }
    
	public boolean hasUsuarioAdmin() {
        Optional<UsuarioModel> admin = usuarioRepository.findByPerfil(Perfil.ADMIN);
		return admin.isPresent();
	}

	public void cadastrarUsuarioAdmin() {
        UsuarioModel admin = UsuarioModel.builder()
            .perfil(Perfil.ADMIN)
            .nome("Mirer Balbino de Andrade Junior")
            .email("mirer.rmj@gmail.com")
            .matricula("01")
            .cpf("00000000000")
            .senha("$2a$12$FJve86hShTAnCXXjHjVHNOB7nA7B/0DEc.jeUGzP3TcQYqPehFl.a")
            .build();
        
        usuarioRepository.save(admin);
	}
    
    public String gerarSenhaPrimeiroAcesso() {
        int tamanhoSenha = 10;
        boolean hasLetras = true;
        boolean hasDigitos = true;

        return RandomStringUtils.random(tamanhoSenha, hasLetras, hasDigitos);        
    }  

    private UsuarioModel getUsuarioLogado (Principal principal) {
        var usuarioPrincipal = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        UsuarioModel usuarioLogado = (UsuarioModel) usuarioPrincipal;

        return usuarioLogado;
    }
}
