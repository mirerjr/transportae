package br.com.transportae.pontoParada;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PontoParadaRepository extends JpaRepository<PontoParadaModel, Long> {

    @Query("SELECT p FROM ponto_parada p WHERE p.linhaTransporte.id = :linhaId ORDER BY p.horarioPrevistoIda ASC")
    List<PontoParadaModel> findByLinhaTransporteIdOrdemCrescente(Long linhaId);

    @Query("SELECT p FROM ponto_parada p WHERE p.linhaTransporte.id = :linhaId ORDER BY p.horarioPrevistoVolta ASC")
    List<PontoParadaModel> findByLinhaTransporteIdOrdemDecrescente(Long linhaId);
}
