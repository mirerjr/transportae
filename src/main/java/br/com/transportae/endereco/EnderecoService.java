package br.com.transportae.endereco;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final IEnderecoRepository enderecoRepository;
    
    public EnderecoModel cadastrarEndereco(EnderecoDto endereco) {       
        if (isEnderecoExistente(endereco.getId())) {
            throw new EntityExistsException("Endereço já existente");
        }

        EnderecoModel novoEndereco = converterDtoParaDomain(endereco);        
        return enderecoRepository.save(novoEndereco);
    }

    public EnderecoModel atualizarEndereco(EnderecoDto endereco) {
        EnderecoModel enderecoAtual  = enderecoRepository.findById(endereco.getId())
            .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        BeanUtils.copyProperties(endereco, enderecoAtual);
        return enderecoRepository.save(enderecoAtual);
    }

    public void removerEndereco(Long id) {
        if (!isEnderecoExistente(id)) {
            throw new EntityNotFoundException("Endereço não encontrado");
        }

        enderecoRepository.deleteById(id);
    }

    //TODO: Validar endereço pela latitude e longitude
    public Boolean isEnderecoExistente(Long id) {
        return id != null && enderecoRepository.existsById(id);   
    }

    public EnderecoModel converterDtoParaDomain(EnderecoDto endereco) {
        return EnderecoModel.builder()
            .descricao(endereco.getDescricao())
            .numero(endereco.getNumero())
            .complemento(endereco.getComplemento())
            .cep(endereco.getCep())
            .cidade(endereco.getCidade())
            .latitude(endereco.getLatitude())
            .longitude(endereco.getLongitude())
            .build();
    }

    public EnderecoDto converterDomainParaDto(EnderecoModel endereco) {
        EnderecoDto enderecoDto = new EnderecoDto();
        BeanUtils.copyProperties(endereco, enderecoDto);
        
        return enderecoDto;
    }
}
