package br.com.transportae.usuario;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/v1/usuarios")
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class UsuarioController {

    
    private final IUsuarioRepository usuarioRepository;    
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> cadastrar(@Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuarioService.cadastrarUsuario(usuarioDto));
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        List<UsuarioModel> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> exibir(@PathVariable Long id) throws EntityNotFoundException {
        UsuarioModel usuario = usuarioRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            
        return ResponseEntity.ok().body(usuario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody UsuarioModel usuarioModel) {
        Optional<UsuarioModel> usuarioExistente = usuarioRepository.findById(id);

        if (usuarioExistente.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Usuário não encontrado"));
        }

        usuarioModel.setId(id);

        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuarioModel);
        
        return ResponseEntity.ok().body(usuarioAtualizado);
    }

    @PatchMapping("/senha")
    public ResponseEntity<Object> alterarSenha(
        @Valid @RequestBody AlterarSenhaRequest request,
        Principal principal
    ) {
        usuarioService.alterarSenha(request, principal);
        return ResponseEntity.ok().build();
    }
}
