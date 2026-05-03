import { get } from './client.js';

export const getNotificacionesByUsuario = (id) => get(`/notificaciones/usuario/${id}`);
