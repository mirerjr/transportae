package br.com.transportae.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlterarSenhaRequest {

    @NotBlank(message = "Por favor, informe a senha atual")
    private String senhaAtual;

    @NotBlank(message = "Por favor, informe a nova senha")
    private String novaSenha;

    @NotBlank(message = "Por favor, confirme a nova senha")
    private String novaSenhaConfirmada;

}