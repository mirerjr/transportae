package br.com.transportae.usuario;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IUsuarioRepository extends JpaRepository<UsuarioModel, BigInteger> {
    Optional<UsuarioModel> findById(BigInteger id);
    Optional<UsuarioModel> findByCpf(String cpf);
}
