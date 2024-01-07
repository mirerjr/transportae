package br.com.transportae.usuarioLinha;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.transportae.linhaTransporte.LinhaTransporteModel;
import br.com.transportae.usuario.UsuarioModel;

public interface UsuarioLinhaRepository extends JpaRepository<UsuarioLinhaModel, Long> {

    List<UsuarioLinhaModel> findAllByLinhaTransporteIdOrderByPerfilLinhaDesc(Long id);
    List<UsuarioLinhaModel> findAllByLinhaTransporteIdAndPerfilLinha(Long id, PerfilLinha perfilLinha);

    Boolean existsByUsuarioAndLinhaTransporteAndPerfilLinha(UsuarioModel usuario, LinhaTransporteModel linhaTransporte, PerfilLinha perfilLinha);
    Optional<UsuarioLinhaModel> findByUsuarioAndLinhaTransporteAndPerfilLinha(UsuarioModel usuario, LinhaTransporteModel linhaTransporte, PerfilLinha perfilLinha);
    
    List<UsuarioLinhaModel> findAllByUsuarioId(Long id);
    Integer countByLinhaTransporteId(Long id);
}

