package br.com.transportae.ItinerarioStatus;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import br.com.transportae.Itinerario.ItinerarioModel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "itinerario_status")
@AllArgsConstructor
@NoArgsConstructor
public class ItinerarioStatusModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoItinerarioStatus tipoItinerarioStatus;
    
    @CreationTimestamp
    private LocalDateTime data;

    @OneToOne
    private ItinerarioModel itinerario;
}
