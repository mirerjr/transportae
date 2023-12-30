package br.com.transportae.validators;

import br.com.transportae.usuario.UsuarioDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstituicaoObrigatoriaParaAlunoValidator implements ConstraintValidator<InstituicaoObrigatoriaParaAluno, UsuarioDto> {

    @Override
    public void initialize(InstituicaoObrigatoriaParaAluno constraintAnnotation) {
    }

    @Override
    public boolean isValid(UsuarioDto usuarioDto, ConstraintValidatorContext context) {
        boolean isAluno = usuarioDto .getPerfil().name().equals("ALUNO");        
        boolean hasInstituicaoId = usuarioDto.getInstituicaoId() != null;
        
        return !isAluno || hasInstituicaoId;
    }
}
