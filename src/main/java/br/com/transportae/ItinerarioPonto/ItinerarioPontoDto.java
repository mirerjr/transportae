package br.com.transportae.ItinerarioPonto;

import java.util.List;

import br.com.transportae.ItinerarioPontoStatus.ItinerarioPontoStatusDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItinerarioPontoDto {
    
    private Long id;
    private Long itinerarioId;
    private Long pontoParadaId;

    List<ItinerarioPontoStatusDto> itinerarioPontoStatus;
}
