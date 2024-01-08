package br.com.transportae.ItinerarioPontoStatus;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.transportae.ItinerarioPonto.ItinerarioPontoModel;
import br.com.transportae.ItinerarioPonto.ItinerarioPontoService;
import br.com.transportae.usuario.UsuarioModel;
import br.com.transportae.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItinerarioPontoStatusService {

    private final ItinerarioPontoStatusRepository itinerarioPontoStatusRepository;
    private final UsuarioService usuarioService;

    public ItinerarioPontoStatusModel cadastrarItinerarioPontoStatus(ItinerarioPontoStatusDto itinerarioPontoStatusDto, ItinerarioPontoModel itinerarioPonto) {
        ItinerarioPontoStatusModel novoItinerarioPontoStatus = converterDtoParaDomain(itinerarioPontoStatusDto);

        UsuarioModel usuario = usuarioService.getUsuario(itinerarioPontoStatusDto.getUsuarioId());
        novoItinerarioPontoStatus.setUsuario(usuario);
        novoItinerarioPontoStatus.setItinerarioPonto(itinerarioPonto);

        return itinerarioPontoStatusRepository.save(novoItinerarioPontoStatus);
    }

    public ItinerarioPontoStatusModel converterDtoParaDomain(ItinerarioPontoStatusDto itinerarioPontoStatusDto) {
        ItinerarioPontoStatusModel itinerarioPontoStatus = ItinerarioPontoStatusModel.builder()
            .id(itinerarioPontoStatusDto.getId())
            .status(itinerarioPontoStatusDto.getStatus())
            .dataCadastro(itinerarioPontoStatusDto.getDataCadastro())
            .dataAtualizacao(itinerarioPontoStatusDto.getDataAtualizacao())
            .build();

        return itinerarioPontoStatus;
    }

    public ItinerarioPontoStatusDto converterDomainParaDto(ItinerarioPontoStatusModel itinerarioPontoStatus) {
        ItinerarioPontoStatusDto itinerarioPontoStatusDto = new ItinerarioPontoStatusDto();
        BeanUtils.copyProperties(itinerarioPontoStatus, itinerarioPontoStatusDto);

        return itinerarioPontoStatusDto;
    }
}
