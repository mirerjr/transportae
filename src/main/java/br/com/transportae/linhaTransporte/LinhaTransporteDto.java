package br.com.transportae.linhaTransporte;

import br.com.transportae.usuario.UsuarioDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinhaTransporteDto {

    private Long id;
    private boolean ativa;

    @NotBlank(message = "O nome é um campo obrigatório")
    private String nome;

    @NotNull(message = "O turno é um campo obrigatório")
    @Enumerated(EnumType.STRING)
    private Turno turno;

    @NotNull(message = "É necessário vincular um motorista")
    private Long motoristaId;

    private UsuarioDto motorista;

    //TODO Retornar quantitativo de alunos e pontos vinculados
}