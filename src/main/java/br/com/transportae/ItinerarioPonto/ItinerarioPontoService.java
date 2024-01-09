package br.com.transportae.ItinerarioPonto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.transportae.Itinerario.ItinerarioModel;
import br.com.transportae.Itinerario.ItinerarioService;
import br.com.transportae.ItinerarioPontoStatus.ItinerarioPontoStatusDto;
import br.com.transportae.ItinerarioPontoStatus.ItinerarioPontoStatusModel;
import br.com.transportae.ItinerarioPontoStatus.ItinerarioPontoStatusService;
import br.com.transportae.pontoParada.PontoParadaModel;
import br.com.transportae.pontoParada.PontoParadaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItinerarioPontoService {

    private final ItinerarioPontoRepository itinerarioPontoRepository;
    private final ItinerarioPontoStatusService itinerarioPontoStatusService;
    private final PontoParadaService pontoParadaService;

    public List<ItinerarioPontoModel> cadastrarPontosDoItinerario(ItinerarioModel itinerario) {
        List<ItinerarioPontoModel> itinerarioPontos = new ArrayList<>();

        itinerario.getLinhaTransporte().getPontos().forEach(ponto ->{
            ItinerarioPontoModel novoItinerarioPonto = ItinerarioPontoModel.builder()
                .itinerario(itinerario)
                .pontoParada(ponto)
                .build();

            itinerarioPontos.add(novoItinerarioPonto);
        });

        return itinerarioPontoRepository.saveAll(itinerarioPontos);
    }

    public ItinerarioPontoDto converterDomainParaDto(ItinerarioPontoModel itinerarioPonto) {
        ItinerarioPontoDto itinerarioPontoDto = new ItinerarioPontoDto();
        BeanUtils.copyProperties(itinerarioPonto, itinerarioPontoDto);

        itinerarioPontoDto.setItinerarioId(itinerarioPonto.getItinerario().getId());
        itinerarioPontoDto.setPontoParadaId(itinerarioPonto.getPontoParada().getId());
        itinerarioPontoDto.setPontoParada(pontoParadaService.converterDomainParaDto(itinerarioPonto.getPontoParada()));

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

    public ItinerarioPontoStatusDto adicionarPontoStatus(Long id, ItinerarioPontoStatusDto itinerarioPontoStatusDto) {
        ItinerarioPontoModel itinerarioPonto = getItinerarioPonto(id);
        ItinerarioPontoStatusModel itinerarioPontoStatus = itinerarioPontoStatusService.cadastrarItinerarioPontoStatus(itinerarioPontoStatusDto, itinerarioPonto);

        return itinerarioPontoStatusService.converterDomainParaDto(itinerarioPontoStatus);        
    }
}
