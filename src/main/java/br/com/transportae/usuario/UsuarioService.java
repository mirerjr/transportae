package br.com.transportae.usuario;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.transportae.usuario.exceptions.UsuarioExistenteException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    public UsuarioModel cadastrarUsuario(UsuarioDto usuarioDto) {
        if (isUsuarioCadastrado(usuarioDto.getEmail(), usuarioDto.getCpf())) {
            throw new UsuarioExistenteException("Usuário já existente");
        }

        UsuarioModel novoUsuario = converterDtoParaDomain(usuarioDto);        
        return usuarioRepository.save(novoUsuario);
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
        Boolean isSenhaNovaConfirmada = request.getNovaSenha().equals(request.getNovaSenhaConfirmada());

        if (!isSenhaAtualCorreta) {
            throw new IllegalStateException("Senha atual incorreta");
        }
        
        if (!isSenhaNovaConfirmada) {
            throw new IllegalStateException("As senhas não conferem");
        }

        usuarioLogado.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuarioRepository.save(usuarioLogado);
    }
    
    private UsuarioModel getUsuarioLogado (Principal principal) {
        var usuarioPrincipal = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        UsuarioModel usuarioLogado = (UsuarioModel) usuarioPrincipal;

        return usuarioLogado;
    }
}
