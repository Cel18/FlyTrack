import { get, post, put, del } from './client.js';

// ── Vuelos ──────────────────────────────────────────────────────────────────
export const getVuelos        = ()           => get('/vuelos');
export const createVuelo      = (data)       => post('/vuelos', data);
export const updateVuelo      = (id, data)   => put(`/vuelos/${id}`, data);
export const deleteVuelo      = (id)         => del(`/vuelos/${id}`);

// ── Usuarios ─────────────────────────────────────────────────────────────────
export const getUsuarios      = ()           => get('/usuarios');
export const deleteUsuario    = (id)         => del(`/usuarios/${id}`);

// ── Puertas de Embarque ───────────────────────────────────────────────────────
export const getPuertas       = ()           => get('/puertas');
export const createPuerta     = (data)       => post('/puertas', data);
export const updatePuerta     = (id, data)   => put(`/puertas/${id}`, data);
export const deletePuerta     = (id)         => del(`/puertas/${id}`);

// ── Reportes ──────────────────────────────────────────────────────────────────
export const getReportes      = ()           => get('/reportes');
export const updateEstadoReporte = (id, estado) => put(`/reportes/${id}/estado`, { estado });
export const deleteReporte    = (id)         => del(`/reportes/${id}`);
