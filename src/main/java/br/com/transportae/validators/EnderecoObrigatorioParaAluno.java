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
@Constraint(validatedBy = EnderecoObrigatorioParaAlunoValidator.class)
public @interface EnderecoObrigatorioParaAluno {
    String message() default "O aluno deve ter um endereço cadastrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};    
}
