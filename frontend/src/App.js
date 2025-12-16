/**
 * MAIN APP COMPONENT
 * 
 * Root component with routing configuration
 */

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Header from './components/Header';
import Home from './pages/Home';
import Dashboard from './pages/Dashboard';
import TriggerPipeline from './pages/TriggerPipeline';
import ExecutionDetails from './pages/ExecutionDetails';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/pipeline" element={<TriggerPipeline />} />
          <Route path="/execution/:id" element={<ExecutionDetails />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
