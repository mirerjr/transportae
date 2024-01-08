package br.com.transportae.Itinerario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItinerarioRepository extends JpaRepository<ItinerarioModel, Long> {

    Page<ItinerarioModel> findAllByCodigoVeiculoContainingIgnoreCase(String pesquisa, Pageable pagina);

    Page<ItinerarioModel> findAllByLinhaTransporteId(Long linhaTransporteId, Pageable pagina);
    Page<ItinerarioModel> findAllByLinhaTransporteIdAndCodigoVeiculoContainingIgnoreCase(Long linhaTransporteId, String pesquisa, Pageable pagina);
}
