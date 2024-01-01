package br.com.transportae.usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.transportae.endereco.EnderecoModel;
import br.com.transportae.instituicao.InstituicaoModel;
import br.com.transportae.linhaTransporte.LinhaTransporteModel;
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
@Entity(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Builder.Default
    private boolean ativo = true;

    @Builder.Default
    private boolean emailVerificado = false;
    
    @Column(length = 255, nullable = false)
    private String nome;
    
    @Column(length = 150, nullable = false)
    private String email;

    @Column(length = 30)
    private String telefone;
    
    @Column(length = 11, nullable = false)
    private String cpf;

    @Column(length = 50)
    private String matricula;

    @Column(nullable = false)
    private String senha;

    private LocalDate dataNascimento;

    private LocalDateTime dataPrimeiroAcesso;
    
    @CreationTimestamp
    private LocalDateTime dataCadasto;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;    

    @OneToOne
    private EnderecoModel endereco;

    @OneToOne
    private InstituicaoModel instituicao;

    @OneToOne
    private LinhaTransporteModel linhaVinculada;

    @OneToMany(mappedBy = "motorista")
    private List<LinhaTransporteModel> linhaConduzida;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority autoridade = new SimpleGrantedAuthority(perfil.name());
        return List.of(autoridade);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
