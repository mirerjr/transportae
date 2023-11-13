package br.com.transportae.email;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmailRepository extends JpaRepository<EmailModel, Long> {
        
}
