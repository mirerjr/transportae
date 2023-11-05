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
public class ApiErrorDTO {

    String rota;
    String mensagem;
    int codigoStatus;
    LocalDateTime dataOcorrencia;

    List<CampoInvalidoDTO> camposInvalidos;

    public ApiErrorDTO(String rota, String mensagem, int codigoStatus, LocalDateTime dataOcorrencia) {
        this.rota = rota;
        this.mensagem = mensagem;
        this.codigoStatus = codigoStatus;
        this.dataOcorrencia = dataOcorrencia;
    }
}
