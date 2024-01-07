package br.com.transportae.pontoParada;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pontos-parada")
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class PontoParadaController {

    private final PontoParadaService pontoParadaService;

    @GetMapping
    public ResponseEntity<List<PontoParadaDto>> listar(
        @RequestParam(required = false) Long linhaTransporteId,
        @RequestParam(required = false, defaultValue = "ASC") String ordenacao
    ) {
        List<PontoParadaModel> pontosParada = Objects.nonNull(linhaTransporteId)
            ? pontoParadaService.listarPontosParadaPorLinha(linhaTransporteId, ordenacao)
            : pontoParadaService.listarPontosParada();

        return ResponseEntity
            .ok(pontosParada.stream()
                .map(pontoParadaService::converterDomainParaDto)
                .toList());
    }

    @PostMapping
    public ResponseEntity<PontoParadaDto> cadastrar(@RequestBody @Valid PontoParadaDto pontoParadaDto) {
        PontoParadaModel pontoParada = pontoParadaService.cadastrarPontoParada(pontoParadaDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(pontoParadaService.converterDomainParaDto(pontoParada));
    }
}
