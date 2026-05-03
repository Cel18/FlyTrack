import { get, post } from './client.js';

export const getReportesByUsuario = (id) => get(`/reportes/usuario/${id}`);
export const createReporte = (descripcion, usuarioId, vueloId) =>
  post('/reportes', { descripcion, usuarioId, vueloId });
