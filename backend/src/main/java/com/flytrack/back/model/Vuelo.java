package com.flytrack.back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un vuelo en el sistema FlyTrack.
 * Contiene información de itinerario, estado y pasajeros asociados.
 */
@Entity
@Table(name = "vuelos")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVuelo;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime horaPartida;

    @Column(nullable = false)
    private LocalDateTime horaLlegada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVuelo estadoVuelo = EstadoVuelo.PUNTUAL;

    /** Relación 1:1 con la puerta de embarque asignada a este vuelo. */
    @JsonIgnore
    @OneToOne(mappedBy = "vuelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PuertaEmbarque puertaEmbarque;

    @JsonIgnore
    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notificacion> listNotificaciones = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reporte> listReportes = new ArrayList<>();

    /**
     * Pasajeros registrados en este vuelo.
     * Vuelo es el dueño de la tabla intermedia.
     */
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "vuelo_usuario", joinColumns = @JoinColumn(name = "vuelo_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuarios = new ArrayList<>();

    public Vuelo() {
    }

    public Vuelo(String origen, String destino, String descripcion,
            LocalDateTime horaPartida, LocalDateTime horaLlegada) {
        this.origen = origen;
        this.destino = destino;
        this.descripcion = descripcion;
        this.horaPartida = horaPartida;
        this.horaLlegada = horaLlegada;
    }

    // Getters y Setters

    public Long getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Long idVuelo) {
        this.idVuelo = idVuelo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getHoraPartida() {
        return horaPartida;
    }

    public void setHoraPartida(LocalDateTime horaPartida) {
        this.horaPartida = horaPartida;
    }

    public LocalDateTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalDateTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public EstadoVuelo getEstadoVuelo() {
        return estadoVuelo;
    }

    public void setEstadoVuelo(EstadoVuelo estadoVuelo) {
        this.estadoVuelo = estadoVuelo;
    }

    @JsonIgnore
    public PuertaEmbarque getPuertaEmbarque() {
        return puertaEmbarque;
    }

    public void setPuertaEmbarque(PuertaEmbarque puertaEmbarque) {
        this.puertaEmbarque = puertaEmbarque;
    }

    @JsonIgnore
    public List<Notificacion> getListNotificaciones() {
        return listNotificaciones;
    }

    public void setListNotificaciones(List<Notificacion> listNotificaciones) {
        this.listNotificaciones = listNotificaciones;
    }

    @JsonIgnore
    public List<Reporte> getListReportes() {
        return listReportes;
    }

    public void setListReportes(List<Reporte> listReportes) {
        this.listReportes = listReportes;
    }

    @JsonIgnore
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
