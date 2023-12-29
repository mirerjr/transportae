package br.com.transportae.instituicao;

import java.util.List;

import br.com.transportae.endereco.EnderecoModel;
import br.com.transportae.pontoParada.PontoParadaModel;
import br.com.transportae.usuario.UsuarioModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "instituicao")
@AllArgsConstructor
@NoArgsConstructor
public class InstituicaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nome;

    @Column(length = 15, nullable = false)
    private String sigla;

    @Enumerated(EnumType.STRING)
    private TipoInstituicao tipoInstituicao;

    @OneToOne
    private EnderecoModel endereco;

    @OneToMany(mappedBy = "instituicao")
    private List<UsuarioModel> alunos;

    @OneToMany(mappedBy = "instituicao")
    private List<PontoParadaModel> pontos;
}
