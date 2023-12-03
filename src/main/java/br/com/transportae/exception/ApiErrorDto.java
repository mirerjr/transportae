package br.com.transportae.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDto {

    String rota;
    String titulo;
    String mensagem;
    int codigoStatus;
    LocalDateTime dataOcorrencia;

    List<CampoInvalidoDto> camposInvalidos;

    public ApiErrorDto(String rota, String titulo, String mensagem, int codigoStatus, LocalDateTime dataOcorrencia) {
        this.rota = rota;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.codigoStatus = codigoStatus;
        this.dataOcorrencia = dataOcorrencia;
    }
}
