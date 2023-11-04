package br.com.transportae.auth;

import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.transportae.config.JwtService;
import br.com.transportae.usuario.IUsuarioRepository;
import br.com.transportae.usuario.Perfil;
import br.com.transportae.usuario.UsuarioModel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AutenticacaoResponse cadastrar(CadastroRequest request) {
        Perfil perfilSelecionado = Objects.nonNull(request.getPerfil()) 
            ? request.getPerfil()
            : Perfil.ALUNO;

        UsuarioModel novoUsuario = UsuarioModel.builder()
            .nome(request.getNome())
            .email(request.getEmail())
            .cpf(request.getCpf())
            .matricula(request.getMatricula())
            .dataNascimento(request.getDataNascimento())
            .perfil(perfilSelecionado)
            .senha(passwordEncoder.encode(request.getSenha()))
            .build();

        usuarioRepository.save(novoUsuario);

        String jwtToken = jwtService.gerarToken(novoUsuario);

        return AutenticacaoResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AutenticacaoResponse logar(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getSenha()
            )
        );

        UsuarioModel usuarioEncontrado = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = jwtService.gerarToken(usuarioEncontrado);

        return AutenticacaoResponse.builder()
            .token(jwtToken)
            .build();
    }
    
}
