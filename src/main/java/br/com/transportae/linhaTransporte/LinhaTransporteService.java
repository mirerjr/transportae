package br.com.transportae.linhaTransporte;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.transportae.usuario.UsuarioModel;
import br.com.transportae.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Service
@RequiredArgsConstructor
public class LinhaTransporteService {

    private final ILinhaTransporteRepository linhaTransporteRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public LinhaTransporteModel cadastrarLinhaTransporte(LinhaTransporteDto linhaTransporteDto) {
        LinhaTransporteModel novaLinhaTransporte = converterDtoParaDomain(linhaTransporteDto);

        vincularMotorista(linhaTransporteDto, novaLinhaTransporte);

        return linhaTransporteRepository.save(novaLinhaTransporte);
    }

    private void vincularMotorista(LinhaTransporteDto linhaDto, LinhaTransporteModel linha) {
        if (Objects.isNull(linhaDto.getMotoristaId())) return;

        UsuarioModel motorista = usuarioService.getUsuario(linhaDto.getMotoristaId());
        Optional<LinhaTransporteModel> linhaAtribuidaMesmoTurno = linhaTransporteRepository.findByMotoristaIdAndTurno(motorista.getId(), linha.getTurno());

        if (linhaAtribuidaMesmoTurno.isPresent()) {
            String nomeLinha = linhaAtribuidaMesmoTurno.get().getNome();
            throw new IllegalArgumentException("Motorista já está atribuído na linha (" + nomeLinha + ") no mesmo turno");
        }
        
        linha.setMotorista(motorista);
    }


    public LinhaTransporteModel vincularAluno(Long linhaTransporteId, Long alunoId) {
        LinhaTransporteModel linhaTransporte = getLinhaTransporte(linhaTransporteId);
        UsuarioModel aluno = usuarioService.getUsuario(alunoId);
        
        linhaTransporte.getAlunos().add(aluno);

        return linhaTransporteRepository.save(linhaTransporte);
    }

    public LinhaTransporteModel converterDtoParaDomain(LinhaTransporteDto linhaTransporteDto) {
        return LinhaTransporteModel.builder()
            .nome(linhaTransporteDto.getNome())
            .turno(linhaTransporteDto.getTurno())
            .build();
    }

    public LinhaTransporteDto converterDomainParaDto(LinhaTransporteModel linhaTransporte) {
        LinhaTransporteDto linhaTransporteDto = new LinhaTransporteDto();
        BeanUtils.copyProperties(linhaTransporte, linhaTransporteDto);

        if (Objects.nonNull(linhaTransporte.getMotorista())) {
            linhaTransporteDto.setMotorista(usuarioService.converterDomainParaDto(linhaTransporte.getMotorista()));
        }

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

    public List<LinhaTransporteModel> getLinhasTransportePorMotorista(Long motoristaId) {
        return linhaTransporteRepository.findAllByMotoristaId(motoristaId);
    }

    public LinhaTransporteModel atualizarLinhaTransporte(Long id, LinhaTransporteDto linhaTransporteDto) {
        LinhaTransporteModel linhaTransporteAtual = getLinhaTransporte(id);

        BeanUtils.copyProperties(linhaTransporteDto, linhaTransporteAtual, "id");

        vincularMotorista(linhaTransporteDto, linhaTransporteAtual);

        return linhaTransporteRepository.save(linhaTransporteAtual);
    }

    //TODO: Enviar email de ativação para o motorista e alunos da linha
    public LinhaTransporteModel ativarLinhaTransporte(Long id) {
        LinhaTransporteModel linhaTransporte = getLinhaTransporte(id);
        linhaTransporte.setAtiva(true);
        linhaTransporte.setAtivadaEm(LocalDateTime.now());
        
        return linhaTransporteRepository.save(linhaTransporte);
    }   

    public void cadastrarLinhaTransporteMock(int quantidade) {
        Faker faker = new Faker(Locale.forLanguageTag("pt_BR"));

        for (int pos = 0; pos < quantidade; pos++) {    
            int posicaoAleatoria = faker.number()
                .numberBetween(0, Turno.values().length);
            
            Turno turno = Turno.values()[posicaoAleatoria];

            LinhaTransporteModel linhaTransporte = LinhaTransporteModel.builder()
                .nome("Linha " + pos)
                .turno(turno)
                .build();

            linhaTransporteRepository.save(linhaTransporte);
        }
    }
}
