package br.com.transportae.config;

import org.springframework.stereotype.Component;

import br.com.transportae.usuario.UsuarioService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfiguracaoInicial {

    private final UsuarioService usuarioService;

    @PostConstruct
    public void iniciar() {
        if (!usuarioService.hasUsuarioAdmin()) {
            usuarioService.cadastrarUsuarioAdmin();
        }
    }    
}
