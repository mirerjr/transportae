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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// TODO: Criar endpoints para vincular alunos e pontos
@RestController
@RequestMapping("/api/v1/linhas")
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class LinhaTransporteController {

    private final LinhaTransporteService linhaTransporteService;

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
            .map(linhaTransporteService::converterDomainParaDto));
    }

    @GetMapping("/motoristas/{id}")
    public ResponseEntity<List<LinhaTransporteDto>> listarPorMotorista(@PathVariable Long id) {
        List<LinhaTransporteModel> linhasTransporte = linhaTransporteService.getLinhasTransportePorMotorista(id);

        return ResponseEntity.ok(linhasTransporte.stream()
            .map(linhaTransporteService::converterDomainParaDto)
            .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinhaTransporteDto> exibir(@PathVariable Long id) {
        LinhaTransporteModel linhaTransporte = linhaTransporteService.getLinhaTransporte(id);
        return ResponseEntity.ok(linhaTransporteService.converterDomainParaDto(linhaTransporte));
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