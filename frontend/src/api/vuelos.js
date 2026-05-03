import { get, post, del } from './client.js';

export const getVuelos = () => get('/vuelos');
export const getMisVuelos = (usuarioId) => get(`/vuelos/usuario/${usuarioId}`);
export const suscribirVuelo = (vueloId, usuarioId) => post(`/vuelos/${vueloId}/usuarios/${usuarioId}`);
export const desuscribirVuelo = (vueloId, usuarioId) => del(`/vuelos/${vueloId}/usuarios/${usuarioId}`);
