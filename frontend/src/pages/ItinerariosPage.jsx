import { useEffect, useState } from 'react';
import { getVuelos, getMisVuelos, suscribirVuelo, desuscribirVuelo } from '../api/vuelos.js';
import { useAuth } from '../context/AuthContext.jsx';

const ESTADO_BADGE = {
  PUNTUAL: { label: 'Puntual', cls: 'badge-green' },
  RETRASADO: { label: 'Retrasado', cls: 'badge-yellow' },
  CANCELADO: { label: 'Cancelado', cls: 'badge-red' },
};

function formatFecha(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString('es-CO', {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

export default function ItinerariosPage() {
  const { user } = useAuth();
  const [vuelos, setVuelos] = useState([]);
  const [suscritos, setSuscritos] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [accion, setAccion] = useState({});

  useEffect(() => {
    const fetchData = async () => {
      try {
        const todos = await getVuelos();
        setVuelos(todos);

        if (user?.id) {
          const mis = await getMisVuelos(user.id);
          setSuscritos(new Set(mis.map((v) => v.idVuelo)));
        }
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [user]);

  const toggleSuscripcion = async (vueloId) => {
    if (!user) return;
    setAccion((prev) => ({ ...prev, [vueloId]: true }));
    try {
      if (suscritos.has(vueloId)) {
        await desuscribirVuelo(vueloId, user.id);
        setSuscritos((prev) => { const s = new Set(prev); s.delete(vueloId); return s; });
      } else {
        await suscribirVuelo(vueloId, user.id);
        setSuscritos((prev) => new Set(prev).add(vueloId));
      }
    } catch (err) {
      alert(err.message);
    } finally {
      setAccion((prev) => ({ ...prev, [vueloId]: false }));
    }
  };

  return (
    <main className="main-content page-section">
      <div className="page-header">
        <span className="page-icon">📅</span>
        <div>
          <h2 className="page-title">Itinerarios de Vuelos</h2>
          <p className="page-subtitle">Consulta el estado actual de todos los vuelos</p>
        </div>
      </div>

      {loading && <p className="status-msg">Cargando vuelos...</p>}
      {error && <p className="status-msg error-msg">{error}</p>}
      {!loading && !error && vuelos.length === 0 && (
        <p className="status-msg">No hay vuelos disponibles.</p>
      )}

      {!loading && vuelos.length > 0 && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>Vuelo</th>
                <th>Origen</th>
                <th>Destino</th>
                <th>Salida</th>
                <th>Llegada</th>
                <th>Estado</th>
                {user && <th>Suscripción</th>}
              </tr>
            </thead>
            <tbody>
              {vuelos.map((v) => {
                const badge = ESTADO_BADGE[v.estadoVuelo] ?? { label: v.estadoVuelo, cls: '' };
                const suscrito = suscritos.has(v.idVuelo);
                const cargando = accion[v.idVuelo];
                return (
                  <tr key={v.idVuelo}>
                    <td className="vuelo-desc">{v.descripcion}</td>
                    <td><strong>{v.origen}</strong></td>
                    <td><strong>{v.destino}</strong></td>
                    <td>{formatFecha(v.horaPartida)}</td>
                    <td>{formatFecha(v.horaLlegada)}</td>
                    <td><span className={`badge ${badge.cls}`}>{badge.label}</span></td>
                    {user && (
                      <td>
                        <button
                          className={`btn-suscripcion ${suscrito ? 'suscrito' : ''}`}
                          onClick={() => toggleSuscripcion(v.idVuelo)}
                          disabled={cargando}
                        >
                          {cargando ? '...' : suscrito ? '✓ Suscrito' : 'Suscribirse'}
                        </button>
                      </td>
                    )}
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </main>
  );
}
