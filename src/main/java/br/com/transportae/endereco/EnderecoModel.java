package br.com.transportae.endereco;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "endereco")
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 255)
    private String descricao;

    @Column(length = 10)
    private String numero;

    @Column(length = 255)
    private String complemento;

    @Column(length = 9)
    private String cep;

    @Column(length = 35)
    private String cidade;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}
