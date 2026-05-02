import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext.jsx';
import { getReportesByUsuario, createReporte } from '../api/reportes.js';
import { getVuelos } from '../api/vuelos.js';

const ESTADO_BADGE = {
  PENDIENTE: { label: 'Pendiente', cls: 'badge-yellow' },
  EN_PROGRESO: { label: 'En progreso', cls: 'badge-blue' },
  FINALIZADO: { label: 'Finalizado', cls: 'badge-green' },
};

export default function ReportesPage() {
  const { user } = useAuth();
  const [reportes, setReportes] = useState([]);
  const [vuelos, setVuelos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ descripcion: '', vueloId: '' });
  const [sending, setSending] = useState(false);
  const [formError, setFormError] = useState('');

  const cargarReportes = () =>
    getReportesByUsuario(user.id).then(setReportes).catch((err) => setError(err.message));

  useEffect(() => {
    Promise.all([cargarReportes(), getVuelos().then(setVuelos)])
      .finally(() => setLoading(false));
  }, [user.id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.vueloId) { setFormError('Selecciona un vuelo.'); return; }
    setFormError('');
    setSending(true);
    try {
      await createReporte(form.descripcion, user.id, Number(form.vueloId));
      setForm({ descripcion: '', vueloId: '' });
      await cargarReportes();
    } catch (err) {
      setFormError(err.message);
    } finally {
      setSending(false);
    }
  };

  return (
    <main className="main-content page-section">
      <div className="page-header">
        <span className="page-icon">🧳</span>
        <div>
          <h2 className="page-title">Reportes de Equipaje</h2>
          <p className="page-subtitle">Reporta y rastrea inconvenientes con tu equipaje</p>
        </div>
      </div>

      <div className="report-layout">
        <section className="report-form-card">
          <h3>Nuevo Reporte</h3>
          <form onSubmit={handleSubmit} className="auth-form">
            <div className="form-group">
              <label>Vuelo</label>
              <select
                value={form.vueloId}
                onChange={(e) => setForm({ ...form, vueloId: e.target.value })}
                required
              >
                <option value="">Selecciona un vuelo...</option>
                {vuelos.map((v) => (
                  <option key={v.idVuelo} value={v.idVuelo}>
                    {v.origen} → {v.destino} — {v.descripcion}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Descripción del problema</label>
              <textarea
                value={form.descripcion}
                onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
                placeholder="Describe el inconveniente con tu equipaje..."
                rows={4}
                required
              />
            </div>
            {formError && <p className="form-error">{formError}</p>}
            <button type="submit" className="btn btn-primary btn-full" disabled={sending}>
              {sending ? 'Enviando...' : 'Enviar Reporte'}
            </button>
          </form>
        </section>

        <section>
          <h3>Mis Reportes</h3>
          {loading && <p className="status-msg">Cargando reportes...</p>}
          {error && <p className="status-msg error-msg">{error}</p>}
          {!loading && reportes.length === 0 && (
            <div className="empty-state">
              <span style={{ fontSize: '48px' }}>📭</span>
              <p>No tienes reportes activos.</p>
            </div>
          )}
          <div className="notif-list">
            {reportes.map((r) => {
              const badge = ESTADO_BADGE[r.estadoReporte] ?? { label: r.estadoReporte, cls: '' };
              return (
                <div key={r.id} className="notif-card">
                  <div className="notif-icon">🧳</div>
                  <div className="notif-body">
                    <p className="notif-contenido">{r.descripcion}</p>
                    <span className={`badge ${badge.cls}`}>{badge.label}</span>
                  </div>
                </div>
              );
            })}
          </div>
        </section>
      </div>
    </main>
  );
}
