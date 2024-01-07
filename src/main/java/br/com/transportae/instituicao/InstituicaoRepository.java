package br.com.transportae.instituicao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InstituicaoRepository extends JpaRepository<InstituicaoModel, Long> {

    Page<InstituicaoModel> findByNomeContainingIgnoreCaseOrSiglaContainingIgnoreCase(String nome, String sigla, Pageable pageable);
}
