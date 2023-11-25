package br.com.transportae.auth;

import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.transportae.config.JwtService;
import br.com.transportae.usuario.IUsuarioRepository;
import br.com.transportae.usuario.UsuarioModel;
import br.com.transportae.usuario.exceptions.UsuarioExistenteException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AutenticacaoResponse logar(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getSenha()
            )
        );

        UsuarioModel usuarioEncontrado = usuarioRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new UsuarioExistenteException("Usuário não encontrado"));

        String jwtToken = jwtService.gerarToken(usuarioEncontrado);

        if (!usuarioEncontrado.isEmailVerificado()) {
            return AutenticacaoResponse.builder()
                .isPrimeiroAcesso(true)
                .token(jwtToken)
                .build();
        }

        return AutenticacaoResponse.builder()
            .token(jwtToken)
            .build();
    }
    
}
