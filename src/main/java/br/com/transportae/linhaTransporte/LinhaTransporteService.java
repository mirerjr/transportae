package br.com.transportae.linhaTransporte;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Service
@RequiredArgsConstructor
public class LinhaTransporteService {

    private final LinhaTransporteRepository linhaTransporteRepository;

    @Transactional
    public LinhaTransporteModel cadastrarLinhaTransporte(LinhaTransporteDto linhaTransporteDto) {
        LinhaTransporteModel novaLinhaTransporte = converterDtoParaDomain(linhaTransporteDto);
        return linhaTransporteRepository.save(novaLinhaTransporte);
    }


    public LinhaTransporteModel converterDtoParaDomain(LinhaTransporteDto linhaTransporteDto) {
        return LinhaTransporteModel.builder()
            .id(linhaTransporteDto.getId())
            .nome(linhaTransporteDto.getNome())
            .turno(linhaTransporteDto.getTurno())
            .totalAssentos(linhaTransporteDto.getTotalAssentos())
            .codigoVeiculo(linhaTransporteDto.getCodigoVeiculo())
            .build();
    }

    public LinhaTransporteDto converterDomainParaDto(LinhaTransporteModel linhaTransporte) {
        LinhaTransporteDto linhaTransporteDto = new LinhaTransporteDto();
        BeanUtils.copyProperties(linhaTransporte, linhaTransporteDto);

        return linhaTransporteDto;
    }

    public List<LinhaTransporteModel> getLinhasTransporte() {
        return linhaTransporteRepository.findAll();
    }

    public LinhaTransporteModel getLinhaTransporte(Long id) {
        return linhaTransporteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Linha de transporte não encontrada"));
    }

    public Page<LinhaTransporteModel> listarLinhasTransporte(Pageable pagina, String pesquisa) {
        Page<LinhaTransporteModel> linhasTransporte = pesquisa.length() > 0 
            ? linhaTransporteRepository.findAllByNomeContainingIgnoreCase(pesquisa, pagina)
            : linhaTransporteRepository.findAll(pagina);

        return linhasTransporte;
    }

    public Page<LinhaTransporteModel> listarLinhasTransportePorTurno(Pageable pagina, Turno turno, String pesquisa) {
        Page<LinhaTransporteModel> linhasTransporte = pesquisa.length() > 0 
            ? linhaTransporteRepository.findAllByTurnoAndNomeContainingIgnoreCase(turno, pesquisa, pagina)
            : linhaTransporteRepository.findAllByTurno(turno, pagina);

        return linhasTransporte;
    }

    public LinhaTransporteModel atualizarLinhaTransporte(Long id, LinhaTransporteDto linhaTransporteDto) {
        LinhaTransporteModel linhaTransporteAtual = getLinhaTransporte(id);

        BeanUtils.copyProperties(linhaTransporteDto, linhaTransporteAtual, "id");

        return linhaTransporteRepository.save(linhaTransporteAtual);
    }

    //TODO: Enviar email de ativação para o motorista e alunos da linha
    public LinhaTransporteModel ativarLinhaTransporte(Long id) {
        LinhaTransporteModel linhaTransporte = getLinhaTransporte(id);
        linhaTransporte.setAtiva(true);
        linhaTransporte.setAtivadaEm(LocalDateTime.now());
        
        return linhaTransporteRepository.save(linhaTransporte);
    }   

    public List<LinhaTransporteModel> cadastrarLinhaTransporteMock(int quantidade) {
        Faker faker = new Faker(Locale.forLanguageTag("pt_BR"));
        List<LinhaTransporteModel> linhasTransporte = new ArrayList<>();

        for (int pos = 0; pos < quantidade; pos++) {    
            int posicaoAleatoria = faker.number()
                .numberBetween(0, Turno.values().length);
            
            Turno turno = Turno.values()[posicaoAleatoria];

            LinhaTransporteModel linhaTransporte = LinhaTransporteModel.builder()
                .nome("Linha " + pos)
                .turno(turno)
                .totalAssentos((short) faker.number().numberBetween(10, 50))
                .codigoVeiculo(faker.code().ean8())
                .build();

            LinhaTransporteModel linhaCadastrada = linhaTransporteRepository.save(linhaTransporte);
            linhasTransporte.add(linhaCadastrada);
        }

        return linhasTransporte;
    }
}
