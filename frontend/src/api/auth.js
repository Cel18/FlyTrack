import { post } from './client.js';

export const login = (correo, password) => post('/auth/login', { correo, password });
export const register = (nombre, correo, password) =>
  post('/auth/register', { nombre, correo, password, rol: 'USER' });
