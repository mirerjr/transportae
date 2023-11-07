package br.com.transportae.usuario;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IUsuarioRepository extends JpaRepository<UsuarioModel, BigInteger> {
    
    Optional<UsuarioModel> findById(BigInteger id);
    Optional<UsuarioModel> findByCpf(String cpf);
    Optional<UsuarioModel> findByEmail(String email);

    @Query("SELECT u FROM usuario u WHERE u.email = :email OR u.cpf = :cpf")
    Optional<UsuarioModel> findByEmailOrCpf(String email, String cpf);
}
