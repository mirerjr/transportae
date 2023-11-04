package br.com.transportae.exception;

import java.time.LocalDateTime;

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
    
}
