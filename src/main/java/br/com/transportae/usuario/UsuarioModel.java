package br.com.transportae.usuario;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.transportae.usuariolog.UsuarioLogModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity(name = "usuario")
public class UsuarioModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(columnDefinition = "BIGINT")
    private BigInteger id;

    private Boolean ativo = true;
    
    @Column(length = 255, nullable = false)
    private String nome;
    
    @Column(length = 150, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String login;
    
    @Column(length = 11, nullable = false)
    private String cpf;

    @Column(length = 50)
    private String matricula;

    @Column(nullable = false)
    private String senha;

    private LocalDate dataNascimento;
    
    @CreationTimestamp
    private LocalDateTime dataCadasto;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioLogModel> alteracoesRecebidas;

    @OneToMany(mappedBy = "usuarioResponsavel")
    private List<UsuarioLogModel> alteracoesRealizadas;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority autoridade = new SimpleGrantedAuthority(perfil.name());
        return List.of(autoridade);
    }

    @Override
    public String getUsername() {
        return login;
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
