package br.com.transportae.usuarioLinha;

import java.time.LocalDateTime;

import br.com.transportae.linhaTransporte.LinhaTransporteDto;
import br.com.transportae.usuario.UsuarioDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLinhaDto {

    private Long id;
    
    private UsuarioDto usuario;
    private LinhaTransporteDto linhaTransporte;

    @Enumerated(EnumType.STRING)
    private PerfilLinha perfilLinha;

    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    private Long usuarioId;
    private Long linhaTransporteId;
}
