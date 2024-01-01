package br.com.transportae.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IUsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    
    Optional<UsuarioModel> findById(Long id);
    Optional<UsuarioModel> findByCpf(String cpf);
    Optional<UsuarioModel> findByEmail(String email);
    Optional<UsuarioModel> findByPerfil(Perfil perfil);

    @Query("SELECT u FROM usuario u WHERE u.email = :email OR u.cpf = :cpf")
    Optional<UsuarioModel> findByEmailOrCpf(String email, String cpf);

    Page<UsuarioModel> findAll(Pageable pagina);

    Page<UsuarioModel> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCpfContaining (String nome, String email, String cpf, Pageable pageable);
    List<UsuarioModel> findAllByPerfil(Perfil motorista);
}
