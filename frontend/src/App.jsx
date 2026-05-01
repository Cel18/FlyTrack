import { useState } from 'react'

function App() {
  return (
    <main className="container">
      <div className="card">
        <div className="header">
          <h1>✈️ FlyTrack Monolith is Live!</h1>
          <p>React 18 + Vite Frontend + Spring Boot Backend</p>
        </div>
        
        <div className="content">
          <p>Si estás viendo esta página, significa que:</p>
          <ul>
            <li>✅ El frontend React se compiló correctamente.</li>
            <li>✅ Gradle copió los estáticos al backend.</li>
            <li>✅ Spring Boot está sirviendo la interfaz correctamente.</li>
          </ul>
          
          <div className="status-box">
            <span className="pulse"></span>
            <span className="status-text">Sistema Operativo</span>
          </div>
        </div>
      </div>
    </main>
  )
}

export default App
