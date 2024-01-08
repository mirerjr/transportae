package br.com.transportae.Itinerario;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.transportae.ItinerarioStatus.ItinerarioStatusDto;
import br.com.transportae.ItinerarioStatus.ItinerarioStatusService;
import br.com.transportae.linhaTransporte.LinhaTransporteModel;
import br.com.transportae.linhaTransporte.LinhaTransporteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItinerarioService {

    private final ItinerarioRepository itinerarioRepository;
    private final LinhaTransporteService linhaTransporteService;
    private final ItinerarioStatusService itinerarioStatusService;

    @Transactional
    public ItinerarioModel cadastrarItinerario(ItinerarioDto itinerarioDto) {
        ItinerarioModel novoItinerario = converterDtoParaDomain(itinerarioDto);

        vincularLinhaTransporte(itinerarioDto, novoItinerario);

        return itinerarioRepository.save(novoItinerario);
    }

    private void vincularLinhaTransporte(ItinerarioDto itinerarioDto, ItinerarioModel novoItinerario) {
        if (Objects.isNull(itinerarioDto.getLinhaTransporteId())) return;

        LinhaTransporteModel linhaTransporte = linhaTransporteService.getLinhaTransporte(itinerarioDto.getLinhaTransporteId());
        
        novoItinerario.setLinhaTransporte(linhaTransporte);
    }

    public ItinerarioModel converterDtoParaDomain(ItinerarioDto itinerarioDto) {
        return ItinerarioModel.builder()
            .id(itinerarioDto.getId())
            .codigoVeiculo(itinerarioDto.getCodigoVeiculo())
            .build();
    }

    public ItinerarioDto converterDomainParaDto(ItinerarioModel itinerario) {
        ItinerarioDto itinerarioDto = new ItinerarioDto();
        BeanUtils.copyProperties(itinerario, itinerarioDto);

        // TODO: Buscar o último status em ItinerarioStatusService

        return itinerarioDto;
    }

    public ItinerarioModel getItinerario(Long id) {
        return itinerarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Itinerario não encontrado"));
    }

    public Page<ItinerarioModel> listarItinerariosPorLinha(Pageable pagina, Long linhaTransporteId, String pesquisa) {
        Page<ItinerarioModel> itinerarios = pesquisa.length() > 0 
            ? itinerarioRepository.findAllByLinhaTransporteIdAndCodigoVeiculoContainingIgnoreCase(linhaTransporteId, pesquisa, pagina)
            : itinerarioRepository.findAllByLinhaTransporteId(linhaTransporteId, pagina);
        
        return itinerarios;
    }

    public Page<ItinerarioModel> listarItinerarios(Pageable pagina, String pesquisa) {
        Page<ItinerarioModel> itinerarios = pesquisa.length() > 0 
            ? itinerarioRepository.findAllByCodigoVeiculoContainingIgnoreCase(pesquisa, pagina)
            : itinerarioRepository.findAll(pagina);

        return itinerarios;
    }

    public List<ItinerarioStatusDto> listarItinerarioStatusPorItinerario(Long id) {
        return itinerarioStatusService.listarItinerarioStatusPorItinerario(id).stream()
            .map(itinerarioStatusService::converterDomainParaDto)
            .toList();
    }

    
}
