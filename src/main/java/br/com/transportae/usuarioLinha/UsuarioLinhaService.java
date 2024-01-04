package br.com.transportae.usuarioLinha;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.transportae.linhaTransporte.LinhaTransporteModel;
import br.com.transportae.linhaTransporte.LinhaTransporteService;
import br.com.transportae.usuario.UsuarioModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioLinhaService {

    private final IUsuarioLinhaRepository usuarioLinhaRepository;
    private final LinhaTransporteService linhaTransporteService;

    public UsuarioLinhaModel vincularUsuarioLinha(UsuarioModel usuario, Long linhaId) {
        LinhaTransporteModel linha = linhaTransporteService.getLinhaTransporte(linhaId);
        PerfilLinha perfilLinha = PerfilLinha.valueOf(usuario.getPerfil().name());

        if (Objects.isNull(linha)) {
            throw new IllegalArgumentException("Erro ao buscar a linha de transporte para vinculação");
        }

        if (Objects.isNull(perfilLinha)) {
            throw new IllegalArgumentException("Perfil de usuário inválido");
        }

        if (isVinculoExistente(usuario, linha, perfilLinha)) {
            throw new IllegalArgumentException("Usuário já vinculado a linha de transporte");
        }

        UsuarioLinhaModel usuarioLinha =  UsuarioLinhaModel.builder()
            .linhaTransporte(linha)
            .usuario(usuario)
            .perfilLinha(perfilLinha)
            .build();

        return usuarioLinhaRepository.save(usuarioLinha);
    }

    public List<UsuarioLinhaModel> listarLinhasPorUsuario(Long id) {
        return usuarioLinhaRepository.findAllByUsuarioId(id);
    }

    public List<UsuarioLinhaModel> listarUsuariosPorLinha(Long id) {
        return usuarioLinhaRepository.findAllByLinhaTransporteIdOrderByPerfilLinhaDesc(id);
    } 

    private Boolean isVinculoExistente(UsuarioModel usuario, LinhaTransporteModel linha, PerfilLinha perfilLinha) {
        return usuarioLinhaRepository.existsByUsuarioAndLinhaTransporteAndPerfilLinha(usuario, linha, perfilLinha);
    }

    private UsuarioLinhaModel getUsuarioLinha(UsuarioModel usuario, LinhaTransporteModel linha, PerfilLinha perfilLinha) {
        return usuarioLinhaRepository.findByUsuarioAndLinhaTransporteAndPerfilLinha(usuario, linha, perfilLinha)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não vinculado a linha de transporte"));
    }

    public Integer contarUsuariosPorLinha(Long id) {
        return usuarioLinhaRepository.countByLinhaTransporteId(id);
    }    
}
