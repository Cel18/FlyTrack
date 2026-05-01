import React from 'react';

function App() {
  return (
    <div className="app-container">
      {/* Navbar */}
      <nav className="navbar">
        <div className="navbar-logo">
          <span className="logo-icon">✈️</span>
          <span className="logo-text">FlyTrack</span>
        </div>
        <ul className="navbar-links">
          <li><a href="#itinerarios">Itinerarios</a></li>
          <li><a href="#notificaciones">Notificaciones</a></li>
          <li><a href="#equipaje">Equipaje</a></li>
        </ul>
        <button className="navbar-btn">Iniciar Sesión</button>
      </nav>

      {/* Main Content */}
      <main className="main-content">
        <section className="hero-section">
          <div className="hero-badge">🚀 Práctica DevOps 2026</div>
          <h1 className="hero-title">AeroPuerto Smart</h1>
          <p className="hero-subtitle">
            Gestión inteligente de itinerarios, notificaciones en tiempo real y 
            rastreo de equipaje impulsado por CI/CD.
          </p>
          <div className="hero-actions">
            <button className="btn btn-primary">Ver Vuelos</button>
            <button className="btn btn-secondary">Reportar Equipaje</button>
          </div>
        </section>

        <section className="features-section">
          <div className="feature-card">
            <div className="feature-icon">📅</div>
            <h3>Itinerarios</h3>
            <p>Consulta en tiempo real la programación de los vuelos nacionales e internacionales.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🔔</div>
            <h3>Notificaciones</h3>
            <p>Recibe alertas automáticas en tu dispositivo sobre cambios en tu puerta de embarque.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🧳</div>
            <h3>Equipaje</h3>
            <p>Reporta y rastrea inconvenientes con tus maletas de manera rápida y segura.</p>
          </div>
        </section>
      </main>

      {/* Footer */}
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
    </div>
  );
}

export default App;
