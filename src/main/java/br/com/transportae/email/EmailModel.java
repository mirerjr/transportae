package br.com.transportae.email;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity(name = "email")
public class EmailModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String remetente;
    private String destinatario;
    private String assunto;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @CreationTimestamp
    private LocalDateTime dataEnvio;    
}
