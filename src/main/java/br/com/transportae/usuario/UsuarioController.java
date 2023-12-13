package br.com.transportae.usuario;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<UsuarioModel>> listar(
        @PageableDefault(page = 0, size = 10, sort =  "id", direction = Direction.DESC) Pageable pageable,
        @RequestParam(name = "search", defaultValue = "") String pesquisa
    ){
        Page<UsuarioModel> usuarios = pesquisa.length() > 0 
            ? usuarioService.listar(pageable, pesquisa)
            : usuarioService.listar(pageable);

        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> exibir(@PathVariable Long id) throws EntityNotFoundException {
        UsuarioModel usuario = usuarioRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            
        return ResponseEntity.ok().body(usuario);
    }

    @GetMapping("/logado")
    public ResponseEntity<UsuarioDto> getUsuarioLogado(Principal principal) {
        UsuarioDto usuarioLogado = usuarioService.getUsuarioLogado(principal);
        return ResponseEntity.ok().body(usuarioLogado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDto usuario) {
        return ResponseEntity
            .ok()
            .body(usuarioService.atualizarUsuario(id, usuario));
    }

    @PatchMapping("/{id}/acesso")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> liberarAcesso(@PathVariable Long id) {
        usuarioService.liberarAcessoUsuario(id);
        return ResponseEntity.ok().build();
    }
}
