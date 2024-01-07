package br.com.transportae.instituicao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.transportae.endereco.EnderecoDto;
import br.com.transportae.endereco.EnderecoModel;
import br.com.transportae.endereco.EnderecoService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import net.datafaker.providers.base.University;

@Service
@RequiredArgsConstructor
public class InstituicaoService {

    private final EnderecoService enderecoService;
    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoModel cadastrarInstituicao(InstituicaoDto instituicaoDto) {
        if (isInstituicaoExistente(instituicaoDto.getId())) {
            throw new EntityExistsException("Instituição já existente");
        }
        
        InstituicaoModel novaInstituicao = converterDtoParaDomain(instituicaoDto);

        if (instituicaoDto.getEndereco() != null) {
            EnderecoModel novoEndereco = enderecoService.cadastrarEndereco(instituicaoDto.getEndereco());
            novaInstituicao.setEndereco(novoEndereco);
        }

        return instituicaoRepository.save(novaInstituicao);
    }

    private boolean isInstituicaoExistente(Long id) {
        return id != null && instituicaoRepository.existsById(id);
    }
    
    public InstituicaoModel converterDtoParaDomain(InstituicaoDto instituicaoDto) {
        return InstituicaoModel.builder()
            .nome(instituicaoDto.getNome())
            .sigla(instituicaoDto.getSigla())
            .tipoInstituicao(instituicaoDto.getTipoInstituicao())
            .build();
    }

    public InstituicaoModel getInstituicao(Long id) {
        return instituicaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Instituição não encontrada"));
    }

    public InstituicaoDto converterDomainParaDto(InstituicaoModel instituicao) {
        InstituicaoDto instituicaoDto = new InstituicaoDto();
        BeanUtils.copyProperties(instituicao, instituicaoDto);

        if (Objects.nonNull(instituicao.getEndereco())) {
            instituicaoDto.setEndereco(enderecoService.converterDomainParaDto(instituicao.getEndereco()));
        }

        return instituicaoDto;
    }

    public Page<InstituicaoDto> listarInstituicoes(Pageable pageable, String pesquisa) {
        Page<InstituicaoModel> instituicoes = pesquisa.length() > 0 
            ? instituicaoRepository.findByNomeContainingIgnoreCaseOrSiglaContainingIgnoreCase(pesquisa, pesquisa, pageable)
            : instituicaoRepository.findAll(pageable);

        return instituicoes.map(this::converterDomainParaDto);
    }

    @Transactional
    public InstituicaoDto atualizarInstituicao(Long instituicaoId, InstituicaoDto instituicao) {
        InstituicaoModel instituicaoAtual  = instituicaoRepository.findById(instituicao.getId())
            .orElseThrow(() -> new EntityNotFoundException("Instituição não encontrada"));
        
        BeanUtils.copyProperties(instituicao, instituicaoAtual);
        instituicaoAtual.setId(instituicaoId);

        EnderecoModel enderecoAtualizado = enderecoService.atualizarEndereco(instituicao.getEndereco());

        instituicaoAtual.setEndereco(enderecoAtualizado);

        InstituicaoModel instituicaoAtualizada = instituicaoRepository.save(instituicaoAtual);
        return converterDomainParaDto(instituicaoAtualizada);
    }

    public List<InstituicaoModel> cadastrarInstituicaoMock(int quantidade) {
        Faker faker = new Faker(Locale.forLanguageTag("pt_BR"));
        List<InstituicaoModel> instituicoes = new ArrayList<>();
        
        for (int pos = 0; pos < quantidade; pos++) {
            University instituicaoMock = faker.university();

            String nome = instituicaoMock.name();
            String sigla = gerarSiglaMock(nome);

            InstituicaoModel instituicao = InstituicaoModel.builder()
                .nome(nome)
                .sigla(sigla)
                .endereco(enderecoService.getEnderecoMock(pos))
                .tipoInstituicao(TipoInstituicao.INSTITUTO_TECNICO_SUPERIOR)
                .build();

            InstituicaoModel instituicaoCadastrada = instituicaoRepository.save(instituicao);
            instituicoes.add(instituicaoCadastrada);
        }

        return instituicoes;
    }

    private String gerarSiglaMock(String nome) {
        List<String> iniciais = List.of(nome.split(" ")).stream()
            .map(String::toUpperCase)
            .map(palavra -> palavra.substring(0, 1))
            .toList();

        return String.join("", iniciais);
    }   
}
