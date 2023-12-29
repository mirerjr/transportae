package br.com.transportae.instituicao;

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

@Service
@RequiredArgsConstructor
public class InstituicaoService {

    private final EnderecoService enderecoService;
    private final IInstituicaoRepository instituicaoRepository;

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
    
    private InstituicaoModel converterDtoParaDomain(InstituicaoDto instituicaoDto) {
        return InstituicaoModel.builder()
            .nome(instituicaoDto.getNome())
            .sigla(instituicaoDto.getSigla())
            .tipoInstituicao(instituicaoDto.getTipoInstituicao())
            .build();
    }

    public InstituicaoDto exibirInstituicao(Long id) {
        InstituicaoModel instituicao = instituicaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Instituição não encontrada"));

        return converterDomainParaDto(instituicao);
    }

    private InstituicaoDto converterDomainParaDto(InstituicaoModel instituicao) {
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

    public void cadastrarInstituicaoMock(int quantidade) {
        for (int pos = 0; pos < quantidade; pos++) {
            InstituicaoModel instituicao = InstituicaoModel.builder()
                .nome("Instituição " + pos)
                .sigla("IFS " + pos)
                .endereco(enderecoService.getEnderecoMock(pos))
                .tipoInstituicao(TipoInstituicao.INSTITUTO_TECNICO_SUPERIOR)
                .build();

            instituicaoRepository.save(instituicao);
        }
    }
}
