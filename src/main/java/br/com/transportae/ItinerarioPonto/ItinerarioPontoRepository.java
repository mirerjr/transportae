package br.com.transportae.ItinerarioPonto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItinerarioPontoRepository extends JpaRepository<ItinerarioPontoModel, Long> {

    List<ItinerarioPontoModel> findAllByItinerarioIdAndPontoParadaId(Long itinerarioId, Long pontoParadaId);
}
