package br.com.transportae.Itinerario;

import br.com.transportae.linhaTransporte.LinhaTransporteDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItinerarioDto {

    private Long id;
    private String codigoVeiculo;

    @Enumerated(EnumType.STRING)
    private TipoItinerario tipoItinerario;

    private LinhaTransporteDto linhaTransporte;

    private Long linhaTransporteId;
}
