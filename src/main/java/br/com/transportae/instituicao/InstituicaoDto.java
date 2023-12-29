package br.com.transportae.instituicao;
import org.hibernate.validator.constraints.Length;

import br.com.transportae.endereco.EnderecoDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstituicaoDto {

    private Long id;

    @NotBlank(message = "O nome da instituição é obrigatório")
    private String nome;

    @NotBlank(message = "A sigla da instituição é obrigatória")
    @Length(max = 10, message = "A sigla deve ter no máximo 10 caracteres")
    private String sigla;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo da instituição é obrigatório")
    private TipoInstituicao tipoInstituicao;

    @Valid
    @NotNull(message = "O endereço é obrigatório")
    private EnderecoDto endereco;
}
