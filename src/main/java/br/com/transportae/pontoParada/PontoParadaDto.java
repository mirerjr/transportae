package br.com.transportae.pontoParada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PontoParadaDto {

    private Long id;
    private String nome;
    private LocalTime horarioPrevisto;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    private Long enderecoId;

    // Para definir quais pontos de parada são instituições de ensino
    private Long instituicaoId;

    // Para adicionar ponto de parada a uma linha de transporte
    private Long linhaTransporteId;
}