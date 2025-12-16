/**
 * HOME PAGE - GitHub/Vercel/VS Code Professional Dark Theme
 * Modern landing page with glassmorphism effects
 */

import React from 'react';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();

  return (
    <div style={styles.container}>
      {/* Hero Section */}
      <div style={styles.hero}>
        <div style={styles.heroGlow}></div>
        <div style={styles.heroContent}>
          <div style={styles.badge}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="#3fb950">
              <path d="M12 2L2 7L12 12L22 7L12 2Z"/>
            </svg>
            <span>Production Ready</span>
          </div>
          <h1 style={styles.heroTitle}>
            CI/CD Pipeline<br/>
            <span style={styles.heroGradient}>Automation Platform</span>
          </h1>
          <p style={styles.heroDescription}>
            Build, test, and deploy your applications with confidence using industry-standard
            DevOps practices. Powered by GitHub, Jenkins, and modern cloud infrastructure.
          </p>
          <div style={styles.heroButtons}>
            <button style={styles.primaryButton} onClick={() => navigate('/pipeline')}>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M13 2L3 14H12L11 22L21 10H12L13 2Z" fill="currentColor"/>
              </svg>
              Trigger Pipeline
            </button>
            <button style={styles.secondaryButton} onClick={() => navigate('/dashboard')}>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M9 3H5C3.89543 3 3 3.89543 3 5V9C3 10.1046 3.89543 11 5 11H9C10.1046 11 11 10.1046 11 9V5C11 3.89543 10.1046 3 9 3Z" stroke="currentColor" strokeWidth="2"/>
                <path d="M19 3H15C13.8954 3 13 3.89543 13 5V9C13 10.1046 13.8954 11 15 11H19C20.1046 11 21 10.1046 21 9V5C21 3.89543 20.1046 3 19 3Z" stroke="currentColor" strokeWidth="2"/>
              </svg>
              View Dashboard
            </button>
          </div>
          <div style={styles.stats}>
            <div style={styles.stat}>
              <div style={styles.statValue}>99.9%</div>
              <div style={styles.statLabel}>Uptime</div>
            </div>
            <div style={styles.stat}>
              <div style={styles.statValue}>&lt;2min</div>
              <div style={styles.statLabel}>Build Time</div>
            </div>
            <div style={styles.stat}>
              <div style={styles.statValue}>24/7</div>
              <div style={styles.statLabel}>Monitoring</div>
            </div>
          </div>
        </div>
      </div>

      {/* Features Grid */}
      <div style={styles.features}>
        <div style={styles.featureCard}>
          <div style={{...styles.featureIcon, background: 'rgba(88, 166, 255, 0.15)', color: '#58a6ff'}}>
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none">
              <path d="M12 0C5.37 0 0 5.37 0 12C0 17.31 3.435 21.795 8.205 23.385C8.805 23.49 9.03 23.13 9.03 22.815C9.03 22.53 9.015 21.585 9.015 20.58C6 21.135 5.22 19.845 4.98 19.17C4.845 18.825 4.26 17.76 3.75 17.475C3.33 17.25 2.73 16.695 3.735 16.68C4.68 16.665 5.355 17.55 5.58 17.91C6.66 19.725 8.385 19.215 9.075 18.9C9.18 18.12 9.495 17.595 9.84 17.295C7.17 16.995 4.38 15.96 4.38 11.37C4.38 10.065 4.845 8.985 5.61 8.145C5.49 7.845 5.07 6.615 5.73 4.965C5.73 4.965 6.735 4.65 9.03 6.195C9.99 5.925 11.01 5.79 12.03 5.79C13.05 5.79 14.07 5.925 15.03 6.195C17.325 4.635 18.33 4.965 18.33 4.965C18.99 6.615 18.57 7.845 18.45 8.145C19.215 8.985 19.68 10.05 19.68 11.37C19.68 15.975 16.875 16.995 14.205 17.295C14.64 17.67 15.015 18.39 15.015 19.515C15.015 21.12 15 22.41 15 22.815C15 23.13 15.225 23.505 15.825 23.385C20.565 21.795 24 17.295 24 12C24 5.37 18.63 0 12 0Z" fill="currentColor"/>
            </svg>
          </div>
          <h3 style={styles.featureTitle}>GitHub Integration</h3>
          <p style={styles.featureDescription}>
            Seamless integration with GitHub repositories. Automatic webhook triggers on every push, pull request, or tag.
          </p>
          <div style={styles.featureTags}>
            <span style={styles.tag}>Webhooks</span>
            <span style={styles.tag}>Auto-trigger</span>
          </div>
        </div>

        <div style={styles.featureCard}>
          <div style={{...styles.featureIcon, background: 'rgba(210, 153, 34, 0.15)', color: '#d29922'}}>
            <svg width="28" height="28" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm-.5 3h.5v9H12.5V3zm-.5 12h1v1h-1v-1z"/>
            </svg>
          </div>
          <h3 style={styles.featureTitle}>Jenkins Pipeline</h3>
          <p style={styles.featureDescription}>
            Powerful Jenkins automation with multi-stage pipelines. Build, test, package, and deploy in sequence.
          </p>
          <div style={styles.featureTags}>
            <span style={styles.tag}>CI/CD</span>
            <span style={styles.tag}>Multi-stage</span>
          </div>
        </div>

        <div style={styles.featureCard}>
          <div style={{...styles.featureIcon, background: 'rgba(0, 0, 0, 1)', color: '#ffffff', border: '1px solid #30363d'}}>
            <svg width="28" height="28" viewBox="0 0 24 24" fill="currentColor">
              <path d="M24 22.525H0l12-21.05 12 21.05z"/>
            </svg>
          </div>
          <h3 style={styles.featureTitle}>Vercel-like Deployment</h3>
          <p style={styles.featureDescription}>
            Lightning-fast deployments with zero-downtime. Automatic rollbacks on failure with complete audit logs.
          </p>
          <div style={styles.featureTags}>
            <span style={styles.tag}>Zero-downtime</span>
            <span style={styles.tag}>Auto-rollback</span>
          </div>
        </div>
      </div>

      {/* Pipeline Flow */}
      <div style={styles.flowSection}>
        <h2 style={styles.sectionTitle}>Pipeline Workflow</h2>
        <p style={styles.sectionDescription}>
          Automated end-to-end deployment process from code commit to production
        </p>
        <div style={styles.flowContainer}>
          {[
            { icon: '📝', title: 'Code Push', desc: 'Push to GitHub main branch', color: '#58a6ff' },
            { icon: '🔔', title: 'Webhook Trigger', desc: 'Jenkins receives notification', color: '#a371f7' },
            { icon: '🏗️', title: 'Build & Test', desc: 'Maven compiles & runs JUnit', color: '#3fb950' },
            { icon: '📦', title: 'Package', desc: 'Create deployable artifact', color: '#d29922' },
            { icon: '🚀', title: 'Deploy', desc: 'Deploy to production server', color: '#f85149' }
          ].map((step, idx) => (
            <React.Fragment key={idx}>
              <div style={styles.flowStep}>
                <div style={{...styles.flowIcon, borderColor: step.color, color: step.color}}>
                  {step.icon}
                </div>
                <div style={styles.flowTitle}>{step.title}</div>
                <div style={styles.flowDesc}>{step.desc}</div>
              </div>
              {idx < 4 && (
                <div style={styles.flowArrow}>
                  <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
                    <path d="M5 12H19M19 12L12 5M19 12L12 19" stroke="#30363d" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                  </svg>
                </div>
              )}
            </React.Fragment>
          ))}
        </div>
      </div>

      {/* Tech Stack */}
      <div style={styles.techSection}>
        <h2 style={styles.sectionTitle}>Powered By Modern Tech Stack</h2>
        <div style={styles.techGrid}>
          {[
            { name: 'React', desc: 'Frontend UI', color: '#58a6ff' },
            { name: 'Spring Boot', desc: 'Backend API', color: '#3fb950' },
            { name: 'Jenkins', desc: 'CI/CD Server', color: '#d29922' },
            { name: 'Maven', desc: 'Build Tool', color: '#f85149' },
            { name: 'JUnit', desc: 'Testing', color: '#a371f7' },
            { name: 'H2 Database', desc: 'Data Storage', color: '#58a6ff' }
          ].map((tech, idx) => (
            <div key={idx} style={styles.techCard}>
              <div style={{...styles.techDot, background: tech.color}}></div>
              <div>
                <div style={styles.techName}>{tech.name}</div>
                <div style={styles.techDesc}>{tech.desc}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: '1400px',
    margin: '0 auto',
    padding: '40px 24px',
    position: 'relative',
    zIndex: 1
  },
  hero: {
    position: 'relative',
    textAlign: 'center',
    padding: '80px 0 60px',
    marginBottom: '80px'
  },
  heroGlow: {
    position: 'absolute',
    top: '-100px',
    left: '50%',
    transform: 'translateX(-50%)',
    width: '600px',
    height: '600px',
    background: 'radial-gradient(circle, rgba(88, 166, 255, 0.1) 0%, transparent 70%)',
    pointerEvents: 'none',
    zIndex: 0
  },
  heroContent: {
    position: 'relative',
    zIndex: 1
  },
  badge: {
    display: 'inline-flex',
    alignItems: 'center',
    gap: '8px',
    padding: '6px 14px',
    background: 'rgba(63, 185, 80, 0.15)',
    border: '1px solid rgba(63, 185, 80, 0.4)',
    borderRadius: '20px',
    color: '#3fb950',
    fontSize: '13px',
    fontWeight: '600',
    marginBottom: '24px'
  },
  heroTitle: {
    fontSize: '64px',
    fontWeight: '700',
    lineHeight: '1.1',
    marginBottom: '24px',
    color: '#ffffff',
    letterSpacing: '-2px'
  },
  heroGradient: {
    background: 'linear-gradient(135deg, #58a6ff 0%, #a371f7 100%)',
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
    backgroundClip: 'text'
  },
  heroDescription: {
    fontSize: '20px',
    lineHeight: '1.6',
    color: '#8b949e',
    maxWidth: '700px',
    margin: '0 auto 40px',
    fontWeight: '400'
  },
  heroButtons: {
    display: 'flex',
    gap: '16px',
    justifyContent: 'center',
    marginBottom: '60px'
  },
  primaryButton: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    padding: '14px 28px',
    background: '#58a6ff',
    color: '#000000',
    border: 'none',
    borderRadius: '8px',
    fontSize: '16px',
    fontWeight: '600',
    cursor: 'pointer',
    transition: 'all 0.2s ease',
    boxShadow: '0 0 20px rgba(88, 166, 255, 0.3)'
  },
  secondaryButton: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
    padding: '14px 28px',
    background: 'transparent',
    color: '#c9d1d9',
    border: '1px solid #30363d',
    borderRadius: '8px',
    fontSize: '16px',
    fontWeight: '600',
    cursor: 'pointer',
    transition: 'all 0.2s ease'
  },
  stats: {
    display: 'flex',
    gap: '60px',
    justifyContent: 'center'
  },
  stat: {
    textAlign: 'center'
  },
  statValue: {
    fontSize: '36px',
    fontWeight: '700',
    color: '#ffffff',
    marginBottom: '8px'
  },
  statLabel: {
    fontSize: '14px',
    color: '#8b949e',
    textTransform: 'uppercase',
    letterSpacing: '1px'
  },
  features: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))',
    gap: '24px',
    marginBottom: '100px'
  },
  featureCard: {
    background: 'rgba(22, 27, 34, 0.6)',
    border: '1px solid #30363d',
    borderRadius: '12px',
    padding: '32px',
    transition: 'all 0.3s ease',
    cursor: 'default'
  },
  featureIcon: {
    width: '64px',
    height: '64px',
    borderRadius: '12px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: '20px'
  },
  featureTitle: {
    fontSize: '20px',
    fontWeight: '600',
    color: '#ffffff',
    marginBottom: '12px'
  },
  featureDescription: {
    fontSize: '15px',
    lineHeight: '1.6',
    color: '#8b949e',
    marginBottom: '16px'
  },
  featureTags: {
    display: 'flex',
    gap: '8px',
    flexWrap: 'wrap'
  },
  tag: {
    padding: '4px 10px',
    background: 'rgba(88, 166, 255, 0.1)',
    border: '1px solid rgba(88, 166, 255, 0.3)',
    borderRadius: '4px',
    fontSize: '12px',
    color: '#58a6ff',
    fontWeight: '500'
  },
  flowSection: {
    marginBottom: '100px'
  },
  sectionTitle: {
    fontSize: '36px',
    fontWeight: '700',
    color: '#ffffff',
    textAlign: 'center',
    marginBottom: '12px'
  },
  sectionDescription: {
    fontSize: '18px',
    color: '#8b949e',
    textAlign: 'center',
    marginBottom: '60px'
  },
  flowContainer: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexWrap: 'wrap',
    gap: '20px'
  },
  flowStep: {
    textAlign: 'center',
    flex: '0 0 150px'
  },
  flowIcon: {
    width: '72px',
    height: '72px',
    margin: '0 auto 16px',
    borderRadius: '50%',
    background: 'rgba(22, 27, 34, 0.8)',
    border: '2px solid',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '32px'
  },
  flowTitle: {
    fontSize: '16px',
    fontWeight: '600',
    color: '#c9d1d9',
    marginBottom: '8px'
  },
  flowDesc: {
    fontSize: '13px',
    color: '#6e7681',
    lineHeight: '1.4'
  },
  flowArrow: {
    flex: '0 0 auto'
  },
  techSection: {
    background: 'rgba(22, 27, 34, 0.4)',
    border: '1px solid #30363d',
    borderRadius: '16px',
    padding: '60px 40px'
  },
  techGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
    gap: '20px'
  },
  techCard: {
    display: 'flex',
    alignItems: 'center',
    gap: '12px',
    padding: '16px',
    background: 'rgba(13, 17, 23, 0.5)',
    border: '1px solid #21262d',
    borderRadius: '8px'
  },
  techDot: {
    width: '10px',
    height: '10px',
    borderRadius: '50%',
    flexShrink: 0
  },
  techName: {
    fontSize: '15px',
    fontWeight: '600',
    color: '#c9d1d9',
    marginBottom: '2px'
  },
  techDesc: {
    fontSize: '13px',
    color: '#6e7681'
  }
};

export default Home;
