package br.com.transportae.usuario;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestBody UsuarioModel usuarioModel) {
        Optional<UsuarioModel> usuarioEncontrado = usuarioRepository.findByCpf(usuarioModel.getCpf());

        if (usuarioEncontrado.isPresent()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", "Usuário já existente"));
        }

        UsuarioModel usuarioCadastrado = usuarioRepository.save(usuarioModel);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuarioCadastrado);
    }
}
