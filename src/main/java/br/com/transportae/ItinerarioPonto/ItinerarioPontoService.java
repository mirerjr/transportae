package br.com.transportae.ItinerarioPonto;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.transportae.Itinerario.ItinerarioModel;
import br.com.transportae.Itinerario.ItinerarioService;
import br.com.transportae.ItinerarioPontoStatus.ItinerarioPontoStatusDto;
import br.com.transportae.ItinerarioPontoStatus.ItinerarioPontoStatusService;
import br.com.transportae.pontoParada.PontoParadaModel;
import br.com.transportae.pontoParada.PontoParadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItinerarioPontoService {

    private final ItinerarioPontoRepository itinerarioPontoRepository;
    private final PontoParadaService pontoParadaService;
    private final ItinerarioService itinerarioService;
    private final ItinerarioPontoStatusService itinerarioPontoStatusService;

    public ItinerarioPontoModel cadastrarItinerarioPonto(ItinerarioPontoDto itinerarioPontoDto) {
        ItinerarioModel itinerario = itinerarioService.getItinerario(itinerarioPontoDto.getItinerarioId());
        PontoParadaModel pontoParada = pontoParadaService.exibirPontoParada(itinerarioPontoDto.getPontoParadaId());
        
        ItinerarioPontoModel novoItinerarioPonto = ItinerarioPontoModel.builder()
            .itinerario(itinerario)
            .pontoParada(pontoParada)
            .build();

        return itinerarioPontoRepository.save(novoItinerarioPonto);
    }

    public ItinerarioPontoDto converterDomainParaDto(ItinerarioPontoModel itinerarioPonto) {
        ItinerarioPontoDto itinerarioPontoDto = new ItinerarioPontoDto();
        BeanUtils.copyProperties(itinerarioPonto, itinerarioPontoDto);

        itinerarioPontoDto.setItinerarioId(itinerarioPonto.getItinerario().getId());
        itinerarioPontoDto.setPontoParadaId(itinerarioPonto.getPontoParada().getId());

        List<ItinerarioPontoStatusDto> itinerarioPontoStatusDto =  itinerarioPonto.getPontoStatus().stream()
            .map(itinerarioPontoStatusService::converterDomainParaDto)
            .toList();

        itinerarioPontoDto.setItinerarioPontoStatus(itinerarioPontoStatusDto);

        return itinerarioPontoDto;
    }

    public ItinerarioPontoModel getItinerarioPonto(Long id) {
        return itinerarioPontoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ItinerarioPonto não encontrado"));
    }

    public List<ItinerarioPontoModel> listarPorItinerarioIdEPontoId(Long itinerarioId, Long pontoParadaId) {
        return itinerarioPontoRepository.findAllByItinerarioIdAndPontoParadaId(itinerarioId, pontoParadaId);
    }
}
