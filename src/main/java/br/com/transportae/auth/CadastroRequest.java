package br.com.transportae.auth;

import java.time.LocalDate;

import br.com.transportae.usuario.Perfil;
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
public class CadastroRequest {

    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String matricula;
    private LocalDate dataNascimento;
    
    @Enumerated(EnumType.STRING)
    private Perfil perfil;    
}
