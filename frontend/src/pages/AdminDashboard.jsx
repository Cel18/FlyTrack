import { useState, useEffect, useCallback } from 'react';
import {
  getVuelos, createVuelo, updateVuelo, deleteVuelo,
  getUsuarios, deleteUsuario,
  getPuertas, createPuerta, updatePuerta, deletePuerta,
  getReportes, updateEstadoReporte, deleteReporte,
} from '../api/admin.js';

// ─── pequeñas utilidades ───────────────────────────────────────────────────
function fmt(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString('es-CO', { dateStyle: 'medium', timeStyle: 'short' });
}
function toInputDT(iso) {
  if (!iso) return '';
  return new Date(iso).toISOString().slice(0, 16);
}

// ─── componentes de estado ─────────────────────────────────────────────────
function Loading() { return <p className="status-msg">Cargando...</p>; }
function Err({ msg }) { return <p className="status-msg error-msg">{msg}</p>; }

// ═══════════════════════════════════════════════════════════════════════════
// SECCIÓN: VUELOS
// ═══════════════════════════════════════════════════════════════════════════
function VuelosTab() {
  const [vuelos, setVuelos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError]   = useState('');
  const [editing, setEditing] = useState(null); // null=oculto, {}=nuevo, {id,...}=editar

  const load = useCallback(() => {
    setLoading(true);
    getVuelos().then(setVuelos).catch(e => setError(e.message)).finally(() => setLoading(false));
  }, []);
  useEffect(() => { load(); }, [load]);

  const handleDelete = async (id) => {
    if (!confirm('¿Eliminar este vuelo?')) return;
    await deleteVuelo(id);
    load();
  };

  const handleSave = async (form) => {
    const payload = {
      origen: form.origen, destino: form.destino, descripcion: form.descripcion,
      horaPartida: form.horaPartida, horaLlegada: form.horaLlegada,
      estadoVuelo: form.estadoVuelo,
    };
    if (form.id) await updateVuelo(form.id, payload);
    else          await createVuelo(payload);
    setEditing(null);
    load();
  };

  return (
    <div>
      <div className="admin-section-header">
        <h3 className="admin-section-title">✈️ Vuelos</h3>
        <button className="btn btn-primary" onClick={() => setEditing({})}>+ Nuevo Vuelo</button>
      </div>

      {editing !== null && (
        <VueloForm initial={editing} onSave={handleSave} onCancel={() => setEditing(null)} />
      )}

      {loading && <Loading />}
      {error   && <Err msg={error} />}
      {!loading && !error && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead><tr>
              <th>Descripción</th><th>Origen</th><th>Destino</th>
              <th>Salida</th><th>Llegada</th><th>Estado</th><th>Acciones</th>
            </tr></thead>
            <tbody>
              {vuelos.map(v => (
                <tr key={v.idVuelo}>
                  <td>{v.descripcion}</td>
                  <td><strong>{v.origen}</strong></td>
                  <td><strong>{v.destino}</strong></td>
                  <td>{fmt(v.horaPartida)}</td>
                  <td>{fmt(v.horaLlegada)}</td>
                  <td><span className={`badge ${ESTADO_CLS[v.estadoVuelo] ?? ''}`}>{v.estadoVuelo}</span></td>
                  <td className="admin-actions">
                    <button className="btn-icon btn-edit" onClick={() =>
                      setEditing({ id: v.idVuelo, ...v, horaPartida: toInputDT(v.horaPartida), horaLlegada: toInputDT(v.horaLlegada) })
                    }>✏️</button>
                    <button className="btn-icon btn-del" onClick={() => handleDelete(v.idVuelo)}>🗑️</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

const ESTADO_CLS = { PUNTUAL: 'badge-green', RETRASADO: 'badge-yellow', CANCELADO: 'badge-red' };

function VueloForm({ initial, onSave, onCancel }) {
  const [f, setF] = useState({
    origen: '', destino: '', descripcion: '',
    horaPartida: '', horaLlegada: '', estadoVuelo: 'PUNTUAL', ...initial,
  });
  const set = k => e => setF(p => ({ ...p, [k]: e.target.value }));
  const submit = e => { e.preventDefault(); onSave(f); };
  return (
    <form className="admin-form-card" onSubmit={submit}>
      <h4 className="admin-form-title">{f.id ? 'Editar Vuelo' : 'Nuevo Vuelo'}</h4>
      <div className="admin-form-grid">
        <div className="form-group">
          <label>Origen</label>
          <input value={f.origen} onChange={set('origen')} required placeholder="Bogotá" />
        </div>
        <div className="form-group">
          <label>Destino</label>
          <input value={f.destino} onChange={set('destino')} required placeholder="Medellín" />
        </div>
        <div className="form-group">
          <label>Descripción / Código</label>
          <input value={f.descripcion} onChange={set('descripcion')} placeholder="AV201 · Avianca" />
        </div>
        <div className="form-group">
          <label>Estado</label>
          <select value={f.estadoVuelo} onChange={set('estadoVuelo')}>
            <option value="PUNTUAL">Puntual</option>
            <option value="RETRASADO">Retrasado</option>
            <option value="CANCELADO">Cancelado</option>
          </select>
        </div>
        <div className="form-group">
          <label>Hora de Salida</label>
          <input type="datetime-local" value={f.horaPartida} onChange={set('horaPartida')} required />
        </div>
        <div className="form-group">
          <label>Hora de Llegada</label>
          <input type="datetime-local" value={f.horaLlegada} onChange={set('horaLlegada')} required />
        </div>
      </div>
      <div className="admin-form-actions">
        <button type="submit" className="btn btn-primary">Guardar</button>
        <button type="button" className="btn btn-secondary" onClick={onCancel}>Cancelar</button>
      </div>
    </form>
  );
}

// ═══════════════════════════════════════════════════════════════════════════
// SECCIÓN: USUARIOS
// ═══════════════════════════════════════════════════════════════════════════
function UsuariosTab() {
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState('');

  const load = useCallback(() => {
    setLoading(true);
    getUsuarios().then(setUsuarios).catch(e => setError(e.message)).finally(() => setLoading(false));
  }, []);
  useEffect(() => { load(); }, [load]);

  const handleDelete = async (id) => {
    if (!confirm('¿Eliminar este usuario?')) return;
    await deleteUsuario(id);
    load();
  };

  return (
    <div>
      <div className="admin-section-header">
        <h3 className="admin-section-title">👤 Usuarios Registrados</h3>
      </div>
      {loading && <Loading />}
      {error   && <Err msg={error} />}
      {!loading && !error && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead><tr><th>ID</th><th>Nombre</th><th>Correo</th><th>Rol</th><th>Estado</th><th>Acciones</th></tr></thead>
            <tbody>
              {usuarios.map(u => (
                <tr key={u.idUsuario}>
                  <td>{u.idUsuario}</td>
                  <td>{u.nombre}</td>
                  <td>{u.correo}</td>
                  <td><span className={`badge ${u.rol === 'ADMIN' ? 'badge-blue' : 'badge-green'}`}>{u.rol}</span></td>
                  <td>{u.estadoCuenta}</td>
                  <td className="admin-actions">
                    <button className="btn-icon btn-del" onClick={() => handleDelete(u.idUsuario)}>🗑️</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

// ═══════════════════════════════════════════════════════════════════════════
// SECCIÓN: PUERTAS DE EMBARQUE
// ═══════════════════════════════════════════════════════════════════════════
function PuertasTab() {
  const [puertas,  setPuertas]  = useState([]);
  const [vuelos,   setVuelos]   = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [error,    setError]    = useState('');
  const [editing,  setEditing]  = useState(null);

  const load = useCallback(() => {
    setLoading(true);
    Promise.all([getPuertas(), getVuelos()])
      .then(([p, v]) => { setPuertas(p); setVuelos(v); })
      .catch(e => setError(e.message))
      .finally(() => setLoading(false));
  }, []);
  useEffect(() => { load(); }, [load]);

  const handleDelete = async (id) => {
    if (!confirm('¿Eliminar esta puerta?')) return;
    await deletePuerta(id);
    load();
  };

  const handleSave = async (form) => {
    const payload = { codigo: form.codigo, terminal: form.terminal, fechaCierre: form.fechaCierre || null, vueloId: Number(form.vueloId) };
    if (form.id) await updatePuerta(form.id, payload);
    else          await createPuerta(payload);
    setEditing(null);
    load();
  };

  return (
    <div>
      <div className="admin-section-header">
        <h3 className="admin-section-title">🚪 Puertas de Embarque</h3>
        <button className="btn btn-primary" onClick={() => setEditing({})}>+ Nueva Puerta</button>
      </div>

      {editing !== null && (
        <PuertaForm initial={editing} vuelos={vuelos} onSave={handleSave} onCancel={() => setEditing(null)} />
      )}

      {loading && <Loading />}
      {error   && <Err msg={error} />}
      {!loading && !error && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead><tr><th>Código</th><th>Terminal</th><th>Cierre</th><th>Vuelo</th><th>Acciones</th></tr></thead>
            <tbody>
              {puertas.map(p => (
                <tr key={p.idPuerta}>
                  <td><strong>{p.codigo}</strong></td>
                  <td>{p.terminal}</td>
                  <td>{fmt(p.fechaCierre)}</td>
                  <td>{p.vuelo ? `${p.vuelo.origen} → ${p.vuelo.destino}` : '—'}</td>
                  <td className="admin-actions">
                    <button className="btn-icon btn-edit" onClick={() =>
                      setEditing({ id: p.idPuerta, ...p, vueloId: p.vuelo?.idVuelo, fechaCierre: toInputDT(p.fechaCierre) })
                    }>✏️</button>
                    <button className="btn-icon btn-del" onClick={() => handleDelete(p.idPuerta)}>🗑️</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

function PuertaForm({ initial, vuelos, onSave, onCancel }) {
  const [f, setF] = useState({ codigo: '', terminal: '', fechaCierre: '', vueloId: '', ...initial });
  const set = k => e => setF(p => ({ ...p, [k]: e.target.value }));
  const submit = e => { e.preventDefault(); onSave(f); };
  return (
    <form className="admin-form-card" onSubmit={submit}>
      <h4 className="admin-form-title">{f.id ? 'Editar Puerta' : 'Nueva Puerta'}</h4>
      <div className="admin-form-grid">
        <div className="form-group">
          <label>Código</label>
          <input value={f.codigo} onChange={set('codigo')} required placeholder="A12" />
        </div>
        <div className="form-group">
          <label>Terminal</label>
          <input value={f.terminal} onChange={set('terminal')} required placeholder="Terminal Nacional" />
        </div>
        <div className="form-group">
          <label>Vuelo asignado</label>
          <select value={f.vueloId} onChange={set('vueloId')} required>
            <option value="">-- Seleccionar --</option>
            {vuelos.map(v => <option key={v.idVuelo} value={v.idVuelo}>{v.descripcion} ({v.origen} → {v.destino})</option>)}
          </select>
        </div>
        <div className="form-group">
          <label>Cierre de Puerta</label>
          <input type="datetime-local" value={f.fechaCierre} onChange={set('fechaCierre')} />
        </div>
      </div>
      <div className="admin-form-actions">
        <button type="submit" className="btn btn-primary">Guardar</button>
        <button type="button" className="btn btn-secondary" onClick={onCancel}>Cancelar</button>
      </div>
    </form>
  );
}

// ═══════════════════════════════════════════════════════════════════════════
// SECCIÓN: REPORTES
// ═══════════════════════════════════════════════════════════════════════════
function ReportesTab() {
  const [reportes, setReportes] = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [error,    setError]    = useState('');

  const load = useCallback(() => {
    setLoading(true);
    getReportes().then(setReportes).catch(e => setError(e.message)).finally(() => setLoading(false));
  }, []);
  useEffect(() => { load(); }, [load]);

  const handleEstado = async (id, estado) => {
    await updateEstadoReporte(id, estado);
    load();
  };

  const handleDelete = async (id) => {
    if (!confirm('¿Eliminar este reporte?')) return;
    await deleteReporte(id);
    load();
  };

  return (
    <div>
      <div className="admin-section-header">
        <h3 className="admin-section-title">🧳 Reportes de Equipaje</h3>
      </div>
      {loading && <Loading />}
      {error   && <Err msg={error} />}
      {!loading && !error && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead><tr><th>ID</th><th>Usuario</th><th>Vuelo</th><th>Descripción</th><th>Estado</th><th>Acciones</th></tr></thead>
            <tbody>
              {reportes.map(r => (
                <tr key={r.idReporte}>
                  <td>{r.idReporte}</td>
                  <td>{r.usuario?.nombre ?? '—'}</td>
                  <td>{r.vuelo ? `${r.vuelo.origen} → ${r.vuelo.destino}` : '—'}</td>
                  <td className="vuelo-desc">{r.descripcion}</td>
                  <td>
                    <select
                      className="inline-select"
                      value={r.estadoReporte ?? ''}
                      onChange={e => handleEstado(r.idReporte, e.target.value)}
                    >
                      <option value="PENDIENTE">Pendiente</option>
                      <option value="EN_PROCESO">En proceso</option>
                      <option value="RESUELTO">Resuelto</option>
                    </select>
                  </td>
                  <td className="admin-actions">
                    <button className="btn-icon btn-del" onClick={() => handleDelete(r.idReporte)}>🗑️</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

// ═══════════════════════════════════════════════════════════════════════════
// DASHBOARD PRINCIPAL
// ═══════════════════════════════════════════════════════════════════════════
const TABS = [
  { id: 'vuelos',   label: '✈️ Vuelos' },
  { id: 'usuarios', label: '👤 Usuarios' },
  { id: 'puertas',  label: '🚪 Puertas' },
  { id: 'reportes', label: '🧳 Reportes' },
];

export default function AdminDashboard() {
  const [active, setActive] = useState('vuelos');

  return (
    <main className="main-content page-section">
      <div className="page-header">
        <span className="page-icon">🛠️</span>
        <div>
          <h2 className="page-title">Panel de Administración</h2>
          <p className="page-subtitle">Gestión completa del sistema FlyTrack</p>
        </div>
      </div>

      <div className="admin-tabs">
        {TABS.map(t => (
          <button
            key={t.id}
            className={`admin-tab-btn ${active === t.id ? 'active' : ''}`}
            onClick={() => setActive(t.id)}
          >
            {t.label}
          </button>
        ))}
      </div>

      <div className="admin-tab-content">
        {active === 'vuelos'   && <VuelosTab />}
        {active === 'usuarios' && <UsuariosTab />}
        {active === 'puertas'  && <PuertasTab />}
        {active === 'reportes' && <ReportesTab />}
      </div>
    </main>
  );
}
