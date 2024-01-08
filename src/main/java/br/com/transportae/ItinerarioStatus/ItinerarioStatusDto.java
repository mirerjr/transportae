package br.com.transportae.ItinerarioStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItinerarioStatusDto {

    private Long id;
    private Long itinerarioId;
    private String mensagem;
    private TipoItinerarioStatus tipoItinerarioStatus;
}
