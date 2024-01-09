package br.com.transportae.pontoParada;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.transportae.endereco.EnderecoModel;
import br.com.transportae.endereco.EnderecoService;
import br.com.transportae.instituicao.InstituicaoModel;
import br.com.transportae.instituicao.InstituicaoService;
import br.com.transportae.linhaTransporte.LinhaTransporteModel;
import br.com.transportae.linhaTransporte.LinhaTransporteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;

@Service
@RequiredArgsConstructor
public class PontoParadaService {

    private final PontoParadaRepository pontoParadaRepository;
    private final EnderecoService enderecoService;
    private final InstituicaoService instituicaoService;
    private final LinhaTransporteService linhaTransporteService;

    @Transactional
    public PontoParadaModel cadastrarPontoParada(PontoParadaDto pontoParadaDto)  {
        PontoParadaModel pontoParadaModel = converterDtoParaDomain(pontoParadaDto);

        vincularEndereco(pontoParadaDto, pontoParadaModel);
        vincularInstituicao(pontoParadaDto, pontoParadaModel);
        vincularLinhaTransporte(pontoParadaDto, pontoParadaModel);

        return pontoParadaRepository.save(pontoParadaModel);
    }

    public List<PontoParadaModel> listarPontosParada() {
        return pontoParadaRepository.findAll();
    }

    public List<PontoParadaModel> listarPontosParadaPorLinha(Long linhaTransporteId, String ordenacao) {
        return ordenacao.equals("ASC")
            ? pontoParadaRepository.findByLinhaTransporteIdOrdemCrescente(linhaTransporteId)
            : pontoParadaRepository.findByLinhaTransporteIdOrdemDecrescente(linhaTransporteId);
    }

    public PontoParadaModel exibirPontoParada(Long id) {
        return pontoParadaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Ponto de parada não encontrado"));
    }

    private void vincularEndereco(PontoParadaDto pontoParadaDto, PontoParadaModel pontoParadaModel) {
        if (Objects.isNull(pontoParadaDto.getEndereco())) return;

        EnderecoModel endereco = enderecoService.cadastrarEndereco(pontoParadaDto.getEndereco());
        pontoParadaModel.setEndereco(endereco);
    }

    private void vincularInstituicao(PontoParadaDto pontoParadaDto, PontoParadaModel pontoParadaModel) {
        if (Objects.isNull(pontoParadaDto.getInstituicaoId())) return;

        InstituicaoModel instituicao = instituicaoService.getInstituicao(pontoParadaDto.getInstituicaoId());
        pontoParadaModel.setInstituicao(instituicao);
    }

    private void vincularLinhaTransporte(PontoParadaDto pontoParadaDto, PontoParadaModel pontoParadaModel) {
        if (Objects.isNull(pontoParadaDto.getLinhaTransporteId())) return;

        LinhaTransporteModel linhaTransporte = linhaTransporteService.getLinhaTransporte(pontoParadaDto.getLinhaTransporteId());
        pontoParadaModel.setLinhaTransporte(linhaTransporte);
    }

    public PontoParadaModel converterDtoParaDomain(PontoParadaDto pontoParadaDto) {
        return PontoParadaModel.builder()
            .nome(pontoParadaDto.getNome())
            .horarioPrevistoIda(pontoParadaDto.getHorarioPrevistoIda())
            .horarioPrevistoVolta(pontoParadaDto.getHorarioPrevistoVolta())
            .build();
    }

    public PontoParadaDto converterDomainParaDto(PontoParadaModel pontoParadaModel) {
        PontoParadaDto pontoParadaDto = new PontoParadaDto();
        BeanUtils.copyProperties(pontoParadaModel, pontoParadaDto);

        if (Objects.nonNull(pontoParadaModel.getEndereco())) {
            pontoParadaDto.setEndereco(enderecoService.converterDomainParaDto(pontoParadaModel.getEndereco()));
        }

        if (Objects.nonNull(pontoParadaModel.getInstituicao())) {
            pontoParadaDto.setInstituicao(instituicaoService.converterDomainParaDto(pontoParadaModel.getInstituicao()));
        }

        return pontoParadaDto;
    }

    public List<PontoParadaModel> cadastrarPontoParadaMock(int quantidade) {
        Faker faker = new Faker(Locale.forLanguageTag("pt_BR"));
        List<PontoParadaModel> pontosParada = new ArrayList<>();

        for (int pos = 0; pos < quantidade; pos++) {
            LocalTime horarioPrevistoIda = faker.date()
                .future(25 + pos, TimeUnit.HOURS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime();

            LocalTime horarioPrevistoVolta = faker.date()
                .future(50, TimeUnit.HOURS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime();

            String nomePonto = faker.address().streetName() + " " + faker.address().streetSuffix();

            Address enderecoFaker = faker.address();

            EnderecoModel endereco = EnderecoModel.builder()
                .descricao(nomePonto)
                .numero("123")
                .bairro(enderecoFaker.streetName())
                .cidade(enderecoFaker.city())
                .cep(enderecoFaker.zipCode())
                .latitude(Double.parseDouble(enderecoFaker.latitude()))
                .longitude(Double.parseDouble(enderecoFaker.longitude()))
                .build();

            EnderecoModel enderecoSalvo = enderecoService.salvarEndereco(endereco);

            PontoParadaModel pontoParada = PontoParadaModel.builder()
                .nome(nomePonto)
                .horarioPrevistoIda(horarioPrevistoIda)
                .horarioPrevistoVolta(horarioPrevistoVolta)
                .endereco(enderecoSalvo)
                .build();

            PontoParadaModel pontoCadastrado = pontoParadaRepository.save(pontoParada);
            pontosParada.add(pontoCadastrado);
        }

        return pontosParada;
    }

    public PontoParadaModel vincularLinhaTransporte(PontoParadaModel pontoParada, LinhaTransporteModel linha) {
        pontoParada.setLinhaTransporte(linha);
        return pontoParadaRepository.save(pontoParada);
    }

    public PontoParadaModel getPrimeiroPontoIda(LinhaTransporteModel linhaTransporte) {
        return pontoParadaRepository.findFirstByLinhaTransporteIdOrderByHorarioPrevistoIdaAsc(linhaTransporte.getId())
            .orElseThrow(() -> new EntityNotFoundException("Ponto de parada não encontrado"));
    }

    public PontoParadaModel getPrimeiroPontoVolta(LinhaTransporteModel linhaTransporte) {
        return pontoParadaRepository.findFirstByLinhaTransporteIdOrderByHorarioPrevistoIdaAsc(linhaTransporte.getId())
            .orElseThrow(() -> new EntityNotFoundException("Ponto de parada não encontrado"));
    }
}
