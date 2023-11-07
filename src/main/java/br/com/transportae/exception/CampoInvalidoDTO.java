package br.com.transportae.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampoInvalidoDto {

    private String campo;
    private String mensagem;
    
}
