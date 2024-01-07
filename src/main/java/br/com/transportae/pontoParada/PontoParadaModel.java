package br.com.transportae.pontoParada;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.transportae.endereco.EnderecoModel;
import br.com.transportae.instituicao.InstituicaoModel;
import br.com.transportae.linhaTransporte.LinhaTransporteModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "ponto_parada")
@AllArgsConstructor
@NoArgsConstructor
public class PontoParadaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String nome;

    @Column
    private LocalTime horarioPrevisto;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @OneToOne
    private EnderecoModel endereco;

    @OneToOne
    private InstituicaoModel instituicao;

    @ManyToOne
    private LinhaTransporteModel linhaTransporte;
}
