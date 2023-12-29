package br.com.transportae.usuario;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import br.com.transportae.endereco.EnderecoDto;
import br.com.transportae.instituicao.InstituicaoDto;
import br.com.transportae.validators.EnderecoObrigatorioParaAluno;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EnderecoObrigatorioParaAluno
public class UsuarioDto {

    private Long id;
    private String matricula;
    private String telefone;

    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @NotBlank(message = "O nome é um campo obrigatório")
    private String nome;

    @Email(message = "Por favor, informe um email válido")
    @NotBlank(message = "O email é um campo obrigatório")
    private String email;

    @Length(min = 11, max = 11, message = "Por favor, insira um CPF válido com 11 dígitos")
    @NotBlank(message = "O CPF é um campo obrigatório")
    private String cpf;
    
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @Valid
    private EnderecoDto endereco;

    @Valid
    private InstituicaoDto instituicao;
}
