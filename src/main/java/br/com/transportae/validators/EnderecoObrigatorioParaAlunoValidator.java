package br.com.transportae.validators;

import br.com.transportae.usuario.UsuarioDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnderecoObrigatorioParaAlunoValidator implements ConstraintValidator<EnderecoObrigatorioParaAluno, UsuarioDto> {

    @Override
    public void initialize(EnderecoObrigatorioParaAluno constraintAnnotation) {
    }

    @Override
    public boolean isValid(UsuarioDto usuarioDto, ConstraintValidatorContext context) {
        boolean isAluno = usuarioDto.getPerfil().name().equals("ALUNO");
        return !isAluno || (isAluno && usuarioDto.getEndereco() != null);
    }
    
}
