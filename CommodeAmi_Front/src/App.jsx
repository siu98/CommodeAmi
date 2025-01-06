import { useState, React } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import Navigation from './components/Navigation';
import Counter from './features/Counter';
import { useAuth } from './hooks/useAuth';
import LoginPage from './views/user/LoginPage'

import './App.css'

function App() {
  const { isLoggedIn, logout } = useAuth();

  return (
      <Router>
        <Navigation isLoggedIn={isLoggedIn} handleLogout={logout} />
        <Routes>
          <Route path="/"  />
          <Route path="/login" element={<LoginPage />} />
          {/* <Route path="/dashboard" element={isLoggedIn ? <Dashboard /> : <Login />} /> */}
          {/* <Route path="/counter" element={<Counter />} /> */}
        </Routes>
      </Router>
  );
}

export default App
