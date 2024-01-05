package br.com.transportae.linhaTransporte;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import br.com.transportae.usuario.UsuarioService;
import br.com.transportae.usuarioLinha.UsuarioLinhaModel;
import br.com.transportae.usuarioLinha.UsuarioLinhaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


// TODO: Criar endpoints para vincular alunos e pontos
@RestController
@RequestMapping("/api/v1/linhas")
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class LinhaTransporteController {

    private final LinhaTransporteService linhaTransporteService;
    private final UsuarioLinhaService usuarioLinhaService;
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LinhaTransporteDto> cadastrar(@RequestBody @Valid LinhaTransporteDto linhaTransporteDto) {
        LinhaTransporteModel linhaTransporte = linhaTransporteService.cadastrarLinhaTransporte(linhaTransporteDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(linhaTransporteService.converterDomainParaDto(linhaTransporte));
    }

    @GetMapping
    public ResponseEntity<Page<LinhaTransporteDto>> listar(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable,
        @RequestParam(name = "search", defaultValue = "") String pesquisa,
        @RequestParam(name = "turno", required = false) Turno turno
    ) {

        Page<LinhaTransporteModel> linhasTransporte = Objects.nonNull(turno)
            ? linhaTransporteService.listarLinhasTransportePorTurno(pageable, turno, pesquisa)
            : linhaTransporteService.listarLinhasTransporte(pageable, pesquisa);

        return ResponseEntity.ok(linhasTransporte
            .map(linhaTransporteService::converterDomainParaDto)
            .map(linhaDto -> {
                linhaDto.setTotalUsuarios(usuarioLinhaService.contarUsuariosPorLinha(linhaDto.getId()));
                return linhaDto;
            }));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinhaTransporteDto> exibir(@PathVariable Long id) {
        LinhaTransporteModel linhaTransporte = linhaTransporteService.getLinhaTransporte(id);
        LinhaTransporteDto linhaDto = linhaTransporteService.converterDomainParaDto(linhaTransporte);

        linhaDto.setTotalUsuarios(usuarioLinhaService.contarUsuariosPorLinha(linhaDto.getId()));
        
        return ResponseEntity.ok(linhaDto);
    }

    @GetMapping("/{id}/usuarios")
    public ResponseEntity<?> exibirUsuarios(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioLinhaService
            .listarUsuariosPorLinha(id).stream()
            .map(UsuarioLinhaModel::getUsuario)
            .map(usuarioService::converterDomainParaDto)
            .toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LinhaTransporteDto> atualizar(@PathVariable Long id, @RequestBody @Valid LinhaTransporteDto linhaTransporteDto) {
        LinhaTransporteModel linhaTransporte = linhaTransporteService.atualizarLinhaTransporte(id, linhaTransporteDto);
        return ResponseEntity.ok(linhaTransporteService.converterDomainParaDto(linhaTransporte));
    }

    @PostMapping("/{id}/ativar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LinhaTransporteDto> ativarLinhaTransporte(@PathVariable Long id) {
        LinhaTransporteModel linhaTransporte = linhaTransporteService.ativarLinhaTransporte(id);
        return ResponseEntity.ok(linhaTransporteService.converterDomainParaDto(linhaTransporte));
    }
}