package br.com.transportae.usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.transportae.usuario.exceptions.UsuarioExistenteException;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    public UsuarioModel cadastrarUsuario(UsuarioDto usuarioDto) {
        if (isUsuarioCadastrado(usuarioDto.getEmail(), usuarioDto.getCpf())) {
            throw new UsuarioExistenteException("Usuário já existente");
        }

        UsuarioModel novoUsuario = converterDtoParaDomain(usuarioDto);        
        return usuarioRepository.save(novoUsuario);
    }

    public UsuarioModel converterDtoParaDomain (UsuarioDto usuarioDto) {
        return UsuarioModel.builder()
            .matricula(usuarioDto.getMatricula())
            .dataNascimento(usuarioDto.getDataNascimento())
            .nome(usuarioDto.getNome())
            .email(usuarioDto.getEmail())
            .cpf(usuarioDto.getCpf())
            .perfil(usuarioDto.getPerfil())
            .build();
    }

    public Boolean isUsuarioCadastrado(String email, String cpf) {
        return usuarioRepository.findByEmailOrCpf(email, cpf).isPresent();
    }    
}
