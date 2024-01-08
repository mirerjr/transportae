package br.com.transportae.Itinerario;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/itinerarios")
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class ItinerarioController {

    private final ItinerarioService itinerarioService;

    @PostMapping
    @PreAuthorize("hasAuthority('MOTORISTA')")
    public ItinerarioDto cadastrar(ItinerarioDto itinerarioDto) {
        ItinerarioModel itinerario = itinerarioService.cadastrarItinerario(itinerarioDto);
        return itinerarioService.converterDomainParaDto(itinerario);
    }

    @GetMapping
    public ResponseEntity<Page<ItinerarioDto>> listar(
        @PageableDefault(page = 0, size = 10, sort = "dataCadastro", direction = Direction.DESC) Pageable pageable,
        @RequestParam(name = "search", defaultValue = "") String pesquisa,
        @RequestParam(name = "linhaTrasnporteId", required = false) Long linhaTransporteId
    ) {

        Page<ItinerarioModel> itinerarios = Objects.nonNull(linhaTransporteId)
            ? itinerarioService.listarItinerariosPorLinha(pageable, linhaTransporteId, pesquisa)
            : itinerarioService.listarItinerarios(pageable, pesquisa);

        return ResponseEntity.ok(itinerarios.map(itinerarioService::converterDomainParaDto));
    }

    @GetMapping("/{id}")
    public ItinerarioDto exibir(@PathVariable Long id) {
        ItinerarioModel itinerario = itinerarioService.getItinerario(id);
        return itinerarioService.converterDomainParaDto(itinerario);
    }
}
