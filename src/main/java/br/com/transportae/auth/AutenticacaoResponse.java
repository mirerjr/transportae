package br.com.transportae.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutenticacaoResponse {

    private String token;

    @Builder.Default
    private boolean isPrimeiroAcesso = false;
}
