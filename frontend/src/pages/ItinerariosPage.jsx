import { useEffect, useState } from 'react';
import { getVuelos } from '../api/vuelos.js';

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
  const [vuelos, setVuelos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getVuelos()
      .then(setVuelos)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

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
              </tr>
            </thead>
            <tbody>
              {vuelos.map((v) => {
                const badge = ESTADO_BADGE[v.estadoVuelo] ?? { label: v.estadoVuelo, cls: '' };
                return (
                  <tr key={v.idVuelo}>
                    <td className="vuelo-desc">{v.descripcion}</td>
                    <td><strong>{v.origen}</strong></td>
                    <td><strong>{v.destino}</strong></td>
                    <td>{formatFecha(v.horaPartida)}</td>
                    <td>{formatFecha(v.horaLlegada)}</td>
                    <td><span className={`badge ${badge.cls}`}>{badge.label}</span></td>
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
