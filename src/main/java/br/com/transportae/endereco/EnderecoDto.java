package br.com.transportae.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDto {

    private Long id;

    @NotBlank(message = "O endereço é um campo obrigatório")
    @Size(max = 255, message = "Por favor, insira um endereço com até 255 caracteres")
    private String descricao;

    @Size(max = 10, message = "Por favor, insira um número com até 10 caracteres")
    private String numero;

    @Size(max = 255, message = "Por favor, insira um complemento com até 255 caracteres")
    private String complemento;

    @Size(max = 9, message = "Por favor, insira um cep com até 9 dígitos")
    private String cep;

    @Size(max = 35, message = "Por favor, insira um bairro com até 35 dígitos")
    private String bairro;

    @Size(max = 35, message = "Por favor, insira uma cidade com até 35 dígitos")
    @NotBlank(message = "A cidade é um campo obrigatório")
    private String cidade;

    private Double latitude;
    private Double longitude;
}
