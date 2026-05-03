import { BrowserRouter, Routes, Route, Link, Navigate, useNavigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext.jsx';
import LoginPage from './pages/LoginPage.jsx';
import ItinerariosPage from './pages/ItinerariosPage.jsx';
import NotificacionesPage from './pages/NotificacionesPage.jsx';
import ReportesPage from './pages/ReportesPage.jsx';
import AdminDashboard from './pages/AdminDashboard.jsx';
import './index.css';

function Navbar() {
  const { user, signOut } = useAuth();
  const navigate = useNavigate();

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo" style={{ textDecoration: 'none' }}>
        <span className="logo-icon">✈️</span>
        <span className="logo-text">FlyTrack</span>
      </Link>
      <ul className="navbar-links">
        <li><Link to="/itinerarios">Itinerarios</Link></li>
        {user && <li><Link to="/notificaciones">Notificaciones</Link></li>}
        {user && <li><Link to="/reportes">Equipaje</Link></li>}
        {user?.rol === 'ADMIN' && (
          <li><Link to="/admin" className="navbar-admin-link">⚙️ Admin</Link></li>
        )}
      </ul>
      {user ? (
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <span style={{ fontSize: '14px', color: 'var(--text-muted)' }}>Hola, {user.nombre}</span>
          <button className="navbar-btn" onClick={() => { signOut(); navigate('/'); }}>
            Cerrar Sesión
          </button>
        </div>
      ) : (
        <Link to="/login">
          <button className="navbar-btn">Iniciar Sesión</button>
        </Link>
      )}
    </nav>
  );
}

function HomePage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  return (
    <main className="main-content">
      <section className="hero-section">
        <div className="hero-badge">🚀 Práctica DevOps 2026</div>
        <h1 className="hero-title">AeroPuerto Smart</h1>
        <p className="hero-subtitle">
          Gestión inteligente de itinerarios, notificaciones en tiempo real y
          rastreo de equipaje impulsado por CI/CD.
        </p>
        <div className="hero-actions">
          <button className="btn btn-primary" onClick={() => navigate('/itinerarios')}>
            Ver Vuelos
          </button>
          <button
            className="btn btn-secondary"
            onClick={() => navigate(user ? '/reportes' : '/login')}
          >
            Reportar Equipaje
          </button>
        </div>
      </section>

      <section className="features-section">
        <div className="feature-card" onClick={() => navigate('/itinerarios')} style={{ cursor: 'pointer' }}>
          <div className="feature-icon">📅</div>
          <h3>Itinerarios</h3>
          <p>Consulta en tiempo real la programación de los vuelos nacionales e internacionales.</p>
        </div>
        <div
          className="feature-card"
          onClick={() => navigate(user ? '/notificaciones' : '/login')}
          style={{ cursor: 'pointer' }}
        >
          <div className="feature-icon">🔔</div>
          <h3>Notificaciones</h3>
          <p>Recibe alertas automáticas en tu dispositivo sobre cambios en tu puerta de embarque.</p>
        </div>
        <div
          className="feature-card"
          onClick={() => navigate(user ? '/reportes' : '/login')}
          style={{ cursor: 'pointer' }}
        >
          <div className="feature-icon">🧳</div>
          <h3>Equipaje</h3>
          <p>Reporta y rastrea inconvenientes con tus maletas de manera rápida y segura.</p>
        </div>
      </section>
    </main>
  );
}

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-brand">
          <span className="logo-icon">✈️</span>
          <span>FlyTrack by AeroPuerto Smart</span>
        </div>
        <p className="footer-text">Implementado con React, Spring Boot, Docker y AWS EC2.</p>
      </div>
      <div className="footer-bottom">
        <p>&copy; 2026 AeroPuerto Smart. Todos los derechos reservados.</p>
      </div>
    </footer>
  );
}

/** Protege rutas que requieren cualquier usuario autenticado */
function ProtectedRoute({ children }) {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" replace />;
}

/** Protege rutas exclusivas de ADMIN */
function AdminRoute({ children }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.rol !== 'ADMIN') return <Navigate to="/" replace />;
  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <div className="app-container">
          <Navbar />
          <Routes>
            <Route path="/"             element={<HomePage />} />
            <Route path="/login"        element={<LoginPage />} />
            <Route path="/itinerarios"  element={<ItinerariosPage />} />
            <Route path="/notificaciones" element={
              <ProtectedRoute><NotificacionesPage /></ProtectedRoute>
            } />
            <Route path="/reportes" element={
              <ProtectedRoute><ReportesPage /></ProtectedRoute>
            } />
            <Route path="/admin" element={
              <AdminRoute><AdminDashboard /></AdminRoute>
            } />
          </Routes>
          <Footer />
        </div>
      </AuthProvider>
    </BrowserRouter>
  );
}
