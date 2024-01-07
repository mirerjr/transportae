package br.com.transportae.pontoParada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

import br.com.transportae.endereco.EnderecoDto;
import br.com.transportae.instituicao.InstituicaoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PontoParadaDto {

    private Long id;

    @NotBlank(message = "O nome é um campo obrigatório")
    private String nome;

    @NotNull(message = "O horário previsto da ida é um campo obrigatório")
    private LocalTime horarioPrevistoIda;

    @NotNull(message = "O horário previsto da volta é um campo obrigatório")
    private LocalTime horarioPrevistoVolta;

    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    private EnderecoDto endereco;
    private InstituicaoDto instituicao;

    private Long enderecoId;
    private Long instituicaoId;

    @NotNull(message = "A linha de transporte é um campo obrigatório")
    private Long linhaTransporteId;
}