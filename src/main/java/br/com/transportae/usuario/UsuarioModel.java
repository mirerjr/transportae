package br.com.transportae.usuario;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import br.com.transportae.usuariolog.UsuarioLogModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity(name = "usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(columnDefinition = "BIGINT")
    private BigInteger id;

    private Boolean ativo = true;
    private Boolean admin = false;
    
    @Column(length = 255)
    private String nome;
    
    @Column(length = 150)
    private String email;

    @Column(length = 100)
    private String login;
    
    @Column(length = 11)
    private String cpf;

    @Column(length = 50)
    private String matricula;

    private String senha;

    private LocalDate dataNascimento;
    
    @CreationTimestamp
    private LocalDateTime dataCadasto;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioLogModel> alteracoesRecebidas;

    @OneToMany(mappedBy = "usuarioResponsavel")
    private List<UsuarioLogModel> alteracoesRealizadas;
}
