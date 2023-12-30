package br.com.transportae.instituicao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/instituicoes")
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class InstituicaoController {

    private final InstituicaoService instituicaoService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid InstituicaoDto instituicaoDto) {
        InstituicaoModel instituicao = instituicaoService.cadastrarInstituicao(instituicaoDto);
        return ResponseEntity.ok(instituicao);
    }

    @GetMapping
    public ResponseEntity<?> listar(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable,
        @RequestParam(name = "search", defaultValue = "") String pesquisa
    ) {
        return ResponseEntity.ok(instituicaoService.listarInstituicoes(pageable, pesquisa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> exibir(@PathVariable Long id) {
        InstituicaoModel instituicao = instituicaoService.getInstituicao(id);
        return ResponseEntity.ok(instituicaoService.converterDomainParaDto(instituicao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid InstituicaoDto instituicaoDto
    ) {
        return ResponseEntity.ok(instituicaoService.atualizarInstituicao(id, instituicaoDto));
    }
}