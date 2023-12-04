package br.com.transportae.usuario;

import java.security.Principal;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.transportae.auth.AutenticacaoService;
import br.com.transportae.email.EmailService;
import br.com.transportae.usuario.exceptions.UsuarioExistenteException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AutenticacaoService autenticacaoService;

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

    public Page<UsuarioModel> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
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

    public UsuarioModel converterDtoParaDomain(UsuarioDto usuarioDto) {
        return UsuarioModel.builder()
            .matricula(usuarioDto.getMatricula())
            .dataNascimento(usuarioDto.getDataNascimento())
            .nome(usuarioDto.getNome())
            .email(usuarioDto.getEmail())
            .cpf(usuarioDto.getCpf())
            .perfil(usuarioDto.getPerfil())
            .build();
    }

    public UsuarioDto converterDomainParaDto(UsuarioModel usuario) {
        return UsuarioDto.builder()
            .matricula(usuario.getMatricula())
            .dataNascimento(usuario.getDataNascimento())
            .nome(usuario.getNome())
            .email(usuario.getEmail())
            .cpf(usuario.getCpf())
            .perfil(usuario.getPerfil())
            .build();
    }

    public UsuarioDto getUsuarioLogado(Principal principal) {
        UsuarioModel usuarioModel = autenticacaoService.getUsuarioLogado(principal);
        return converterDomainParaDto(usuarioModel);
    }

    public Boolean isUsuarioCadastrado(String email, String cpf) {
        return usuarioRepository.findByEmailOrCpf(email, cpf).isPresent();
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
            .telefone("(00) 00000-0000")
            .matricula("01")
            .cpf("00000000000")
            .senha("$2a$12$FJve86hShTAnCXXjHjVHNOB7nA7B/0DEc.jeUGzP3TcQYqPehFl.a")
            .build();
        
        usuarioRepository.save(admin);
	}

    public void cadastrarUsuarioMock(int quantidade) {
        for (int pos = 1; pos <= quantidade; pos++) {
            String cpf = pos > 9 ? "000000000" + pos : "0000000000" + pos;

            UsuarioModel usuario = UsuarioModel.builder()
                .perfil(Perfil.ALUNO)
                .nome("Mirer " + pos)
                .email("mirer"+ pos +"@gmail.com")
                .telefone("(00) 00000-0000")
                .matricula("0" + pos)
                .cpf(cpf)
                .senha("$2a$12$FJve86hShTAnCXXjHjVHNOB7nA7B/0DEc.jeUGzP3TcQYqPehFl.a")
                .build();        
            usuarioRepository.save(usuario);
        }
	}
    
    public String gerarSenhaPrimeiroAcesso() {
        int tamanhoSenha = 10;
        boolean hasLetras = true;
        boolean hasDigitos = true;

        return RandomStringUtils.random(tamanhoSenha, hasLetras, hasDigitos);        
    }
}
