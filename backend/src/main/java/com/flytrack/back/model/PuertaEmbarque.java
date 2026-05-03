package com.flytrack.back.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Puerta de embarque asignada a un vuelo específico.
 */
@Entity
@Table(name = "puertas_embarque")
public class PuertaEmbarque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPuerta;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String terminal;

    private LocalDateTime fechaCierre;

    /** Vuelo al que está asignada esta puerta. PuertaEmbarque posee la FK. */
    @OneToOne
    @JoinColumn(name = "vuelo_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vuelo vuelo;

    public PuertaEmbarque() {
    }

    public PuertaEmbarque(String codigo, String terminal, LocalDateTime fechaCierre, Vuelo vuelo) {
        this.codigo = codigo;
        this.terminal = terminal;
        this.fechaCierre = fechaCierre;
        this.vuelo = vuelo;
    }

    // Getters y Setters

    public Long getIdPuerta() {
        return idPuerta;
    }

    public void setIdPuerta(Long idPuerta) {
        this.idPuerta = idPuerta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }
}
