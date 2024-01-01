package br.com.transportae.linhaTransporte;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILinhaTransporteRepository extends JpaRepository<LinhaTransporteModel, Long> {

    Optional<LinhaTransporteModel> findById(Long id);
    
    List<LinhaTransporteModel> findAllByMotoristaId(Long id);
    
    Page<LinhaTransporteModel> findAll(Pageable pagina);
    Page<LinhaTransporteModel> findAllByTurno(Turno turno, Pageable pageable);
    Page<LinhaTransporteModel> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<LinhaTransporteModel> findAllByTurnoAndNomeContainingIgnoreCase(Turno turno, String nome, Pageable pageable);

    Optional<LinhaTransporteModel> findByMotoristaIdAndTurno(Long id, Turno turno);
}
