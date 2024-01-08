package br.com.transportae.ItinerarioStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItinerarioStatusRepository extends JpaRepository<ItinerarioStatusModel, Long> {

    List<ItinerarioStatusModel> findAllByItinerarioId(Long itinerarioId);
}
