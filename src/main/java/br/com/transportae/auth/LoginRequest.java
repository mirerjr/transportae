package br.com.transportae.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "Por favor, informe um email válido")
    @NotBlank(message = "O email é um campo obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;    
}
