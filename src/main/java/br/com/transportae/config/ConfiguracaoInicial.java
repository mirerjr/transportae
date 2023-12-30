package br.com.transportae.config;

import org.springframework.stereotype.Component;

import br.com.transportae.instituicao.InstituicaoService;
import br.com.transportae.usuario.UsuarioService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfiguracaoInicial {

    private final UsuarioService usuarioService;
    private final InstituicaoService instituicaoService;

    @PostConstruct
    public void iniciar() {
        if (!usuarioService.hasUsuarioAdmin()) {
            usuarioService.cadastrarUsuarioAdmin();

            usuarioService.cadastrarUsuarioMock(20);
            instituicaoService.cadastrarInstituicaoMock(5);
        }
    }    
}
