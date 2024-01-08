package br.com.transportae.ItinerarioPontoStatus;

import java.time.LocalDateTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItinerarioPontoStatusDto {

    private Long id;
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoItinerarioPontoStatus status;

    private Long itinerarioPontoId;
    private Long usuarioId;

    private String usuarioNome;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
}
