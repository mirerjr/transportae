package br.com.transportae.endereco;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEnderecoRepository extends JpaRepository<EnderecoModel, Long> {
    
}
