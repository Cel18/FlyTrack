package com.flytrack.back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un usuario del sistema FlyTrack.
 * Puede ser un pasajero (USER) o un administrador (ADMIN).
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    /** Contraseña almacenada como hash BCrypt. */
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estadoCuenta = EstadoCuenta.ACTIVO;

    @JsonIgnore
    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notificacion> listNotificaciones = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reporte> listReportes = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY)
    private List<Vuelo> listVuelos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String password, Rol rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
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
    public List<Vuelo> getListVuelos() {
        return listVuelos;
    }

    public void setListVuelos(List<Vuelo> listVuelos) {
        this.listVuelos = listVuelos;
    }
}
