import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.jsx';
import { getNotificacionesByUsuario } from '../api/notificaciones.js';

function formatFecha(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString('es-CO', {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

export default function NotificacionesPage() {
  const { user } = useAuth();
  const [notificaciones, setNotificaciones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getNotificacionesByUsuario(user.id)
      .then(setNotificaciones)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [user.id]);

  return (
    <main className="main-content page-section">
      <div className="page-header">
        <span className="page-icon">🔔</span>
        <div>
          <h2 className="page-title">Mis Notificaciones</h2>
          <p className="page-subtitle">Alertas sobre cambios en tus vuelos</p>
        </div>
      </div>

      {loading && <p className="status-msg">Cargando notificaciones...</p>}
      {error && <p className="status-msg error-msg">{error}</p>}
      {!loading && !error && notificaciones.length === 0 && (
        <div className="empty-state">
          <span style={{ fontSize: '48px' }}>🔕</span>
          <p>No tienes notificaciones por el momento.</p>
        </div>
      )}

      <div className="notif-list">
        {notificaciones.map((n) => (
          <div key={n.id} className="notif-card">
            <div className="notif-icon">🔔</div>
            <div className="notif-body">
              <p className="notif-contenido">{n.contenido}</p>
              <span className="notif-fecha">{formatFecha(n.fechaEnvio)}</span>
            </div>
          </div>
        ))}
      </div>
    </main>
  );
}
