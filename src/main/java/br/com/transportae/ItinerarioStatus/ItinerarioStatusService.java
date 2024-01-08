package br.com.transportae.ItinerarioStatus;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItinerarioStatusService {

    private final ItinerarioStatusRepository itinerarioStatusRepository;

    public ItinerarioStatusModel converterDtoParaDomain(ItinerarioStatusDto itinerarioStatusDto) {
        return ItinerarioStatusModel.builder()
            .id(itinerarioStatusDto.getId())
            .mensagem(itinerarioStatusDto.getMensagem())
            .tipoItinerarioStatus(itinerarioStatusDto.getTipoItinerarioStatus())
            .build();
    }

    public ItinerarioStatusDto converterDomainParaDto(ItinerarioStatusModel itinerarioStatusModel) {
        ItinerarioStatusDto itinerarioStatusDto = new ItinerarioStatusDto();
        BeanUtils.copyProperties(itinerarioStatusModel, itinerarioStatusDto);

        return itinerarioStatusDto;
    }

    public List<ItinerarioStatusModel> listarItinerarioStatusPorItinerario(Long itinerarioId) {
        return itinerarioStatusRepository.findAllByItinerarioId(itinerarioId);
    }
    
}
