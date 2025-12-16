/**
 * HEADER COMPONENT - Professional Dark Theme
 * GitHub + Vercel + VS Code Inspired Navigation
 */

import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Header = () => {
  const location = useLocation();

  return (
    <header style={styles.header}>
      <div style={styles.container}>
        <div style={styles.brand}>
          <div style={styles.logoContainer}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" style={styles.logo}>
              <path d="M12 2L2 7L12 12L22 7L12 2Z" fill="url(#grad1)"/>
              <path d="M2 17L12 22L22 17" stroke="#58a6ff" strokeWidth="2" strokeLinecap="round"/>
              <path d="M2 12L12 17L22 12" stroke="#a371f7" strokeWidth="2" strokeLinecap="round"/>
              <defs>
                <linearGradient id="grad1" x1="2" y1="2" x2="22" y2="12">
                  <stop offset="0%" stopColor="#58a6ff"/>
                  <stop offset="100%" stopColor="#a371f7"/>
                </linearGradient>
              </defs>
            </svg>
          </div>
          <div style={styles.brandText}>
            <h1 style={styles.title}>CI/CD Pipeline</h1>
            <span style={styles.subtitle}>DevOps Automation Lab</span>
          </div>
        </div>
        
        <nav style={styles.nav}>
          <Link to="/" style={location.pathname === '/' ? {...styles.navLink, ...styles.navLinkActive} : styles.navLink}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style={styles.icon}>
              <path d="M3 12L5 10M5 10L12 3L19 10M5 10V20C5 20.5523 5.44772 21 6 21H9M19 10L21 12M19 10V20C19 20.5523 18.5523 21 18 21H15M9 21C9.55228 21 10 20.5523 10 20V16C10 15.4477 10.4477 15 11 15H13C13.5523 15 14 15.4477 14 16V20C14 20.5523 14.4477 21 15 21M9 21H15" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
            </svg>
            Home
          </Link>
          <Link to="/dashboard" style={location.pathname === '/dashboard' ? {...styles.navLink, ...styles.navLinkActive} : styles.navLink}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style={styles.icon}>
              <path d="M9 3H5C3.89543 3 3 3.89543 3 5V9C3 10.1046 3.89543 11 5 11H9C10.1046 11 11 10.1046 11 9V5C11 3.89543 10.1046 3 9 3Z" stroke="currentColor" strokeWidth="2"/>
              <path d="M19 3H15C13.8954 3 13 3.89543 13 5V9C13 10.1046 13.8954 11 15 11H19C20.1046 11 21 10.1046 21 9V5C21 3.89543 20.1046 3 19 3Z" stroke="currentColor" strokeWidth="2"/>
              <path d="M9 13H5C3.89543 13 3 13.8954 3 15V19C3 20.1046 3.89543 21 5 21H9C10.1046 21 11 20.1046 11 19V15C11 13.8954 10.1046 13 9 13Z" stroke="currentColor" strokeWidth="2"/>
              <path d="M19 13H15C13.8954 13 13 13.8954 13 15V19C13 20.1046 13.8954 21 15 21H19C20.1046 21 21 20.1046 21 19V15C21 13.8954 20.1046 13 19 13Z" stroke="currentColor" strokeWidth="2"/>
            </svg>
            Dashboard
          </Link>
          <Link to="/pipeline" style={location.pathname === '/pipeline' ? {...styles.navLink, ...styles.navLinkActive} : styles.navLink}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style={styles.icon}>
              <path d="M13 2L3 14H12L11 22L21 10H12L13 2Z" fill="currentColor"/>
            </svg>
            Trigger
          </Link>
        </nav>
      </div>
    </header>
  );
};

const styles = {
  header: {
    background: 'rgba(22, 27, 34, 0.95)',
    backdropFilter: 'blur(10px)',
    borderBottom: '1px solid #30363d',
    position: 'sticky',
    top: 0,
    zIndex: 1000,
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.4)'
  },
  container: {
    maxWidth: '1400px',
    margin: '0 auto',
    padding: '0 24px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    height: '64px'
  },
  brand: {
    display: 'flex',
    alignItems: 'center',
    gap: '12px'
  },
  logoContainer: {
    display: 'flex',
    alignItems: 'center'
  },
  logo: {
    filter: 'drop-shadow(0 0 8px rgba(88, 166, 255, 0.3))'
  },
  brandText: {
    display: 'flex',
    flexDirection: 'column'
  },
  title: {
    fontSize: '20px',
    fontWeight: '600',
    color: '#ffffff',
    letterSpacing: '-0.5px',
    margin: 0
  },
  subtitle: {
    fontSize: '11px',
    color: '#8b949e',
    fontWeight: '400',
    letterSpacing: '0.5px',
    textTransform: 'uppercase'
  },
  nav: {
    display: 'flex',
    gap: '8px'
  },
  navLink: {
    display: 'flex',
    alignItems: 'center',
    gap: '6px',
    padding: '8px 16px',
    color: '#8b949e',
    textDecoration: 'none',
    fontSize: '14px',
    fontWeight: '500',
    borderRadius: '6px',
    transition: 'all 0.2s ease',
    border: '1px solid transparent'
  },
  navLinkActive: {
    color: '#58a6ff',
    background: 'rgba(88, 166, 255, 0.1)',
    border: '1px solid rgba(88, 166, 255, 0.2)',
    boxShadow: '0 0 12px rgba(88, 166, 255, 0.2)'
  },
  icon: {
    flexShrink: 0
  }
};

export default Header;
