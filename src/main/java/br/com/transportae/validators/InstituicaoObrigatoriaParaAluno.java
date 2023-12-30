package br.com.transportae.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstituicaoObrigatoriaParaAlunoValidator.class)
public @interface InstituicaoObrigatoriaParaAluno {
    String message() default "O aluno deve ser vinculado a uma Instituição";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};    
}
