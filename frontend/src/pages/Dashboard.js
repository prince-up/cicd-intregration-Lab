/**
 * DASHBOARD PAGE
 * 
 * Displays all pipeline executions with status
 */

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllExecutions } from '../services/api';
import StatusBadge from '../components/StatusBadge';

const Dashboard = () => {
  const navigate = useNavigate();
  const [executions, setExecutions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  /* GitHub Tracker Integration */
  const [commits, setCommits] = useState([]);
  const [repoUrl, setRepoUrl] = useState('https://github.com/prince-up/cicd-intregration-Lab.git'); // Default

  // Fetch executions on component mount
  useEffect(() => {
    fetchExecutions();
    fetchCommits();

    // Auto-refresh every 5 seconds
    const interval = setInterval(fetchExecutions, 5000);

    return () => clearInterval(interval);
  }, []);

  const fetchExecutions = async () => {
    try {
      const data = await getAllExecutions();
      setExecutions(data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch executions. Is the backend running?');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchCommits = async () => {
    try {
      const API_KEY = process.env.REACT_APP_API_URL || 'http://localhost:9090/api/pipeline';
      const response = await fetch(`${API_KEY}/github/commits?repoUrl=${repoUrl}`);
      if (response.ok) {
        const data = await response.json();
        setCommits(data);
      }
    } catch (err) {
      console.error("Failed to fetch commits", err);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString();
  };

  const formatDuration = (seconds) => {
    if (!seconds) return 'N/A';
    if (seconds < 60) return `${seconds}s`;
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes}m ${secs}s`;
  };

  if (loading) {
    return (
      <div className="container">
        <div className="loading">
          <div className="spinner"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="card glass-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <div>
            <h1 className="gradient-text" style={{ margin: 0, fontSize: '2rem', fontWeight: 'bold' }}>Pipeline Dashboard</h1>
            <p style={{ margin: '8px 0 0 0', color: 'var(--text-secondary)' }}>
              Monitor all CI/CD pipeline executions
            </p>
          </div>
          <button
            className="btn btn-primary"
            onClick={() => navigate('/pipeline')}
          >
            + New Pipeline
          </button>
        </div>

        {/* GitHub Tracker Stats */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '24px', marginBottom: '32px' }}>
          {/* Commits Card */}
          <div className="card glass-card" style={{ margin: 0 }}>
            <h3 style={{ marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path fillRule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" clipRule="evenodd" />
              </svg>
              Recent Commits
            </h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', maxHeight: '300px', overflowY: 'auto' }}>
              {commits.length > 0 ? (
                commits.map((commit, idx) => (
                  <div key={idx} style={{
                    padding: '12px',
                    background: 'rgba(255,255,255,0.03)',
                    borderRadius: '8px',
                    border: '1px solid rgba(255,255,255,0.05)'
                  }}>
                    <div style={{ fontWeight: 600, marginBottom: '4px', fontSize: '14px' }}>
                      {commit.commit.message}
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px', color: 'var(--text-secondary)' }}>
                      <span>{commit.commit.author.name}</span>
                      <span>{new Date(commit.commit.author.date).toLocaleDateString()}</span>
                    </div>
                  </div>
                ))
              ) : (
                <div style={{ color: 'var(--text-secondary)', textAlign: 'center', padding: '20px' }}>Loading commits...</div>
              )}
            </div>
            <div style={{ marginTop: '16px', fontSize: '12px', color: 'var(--text-secondary)' }}>
              Tracking: <span style={{ color: 'var(--accent-primary)' }}>prince-up/cicd-intregration-Lab</span>
            </div>
          </div>

          <div className="card glass-card" style={{ margin: 0 }}>
            <h3 style={{ marginBottom: '16px' }}>Pipeline Statistics</h3>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '12px' }}>
              <div style={{ padding: '16px', background: 'rgba(16, 185, 129, 0.1)', borderRadius: '12px', textAlign: 'center' }}>
                <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#10b981' }}>
                  {executions.filter(e => e.status === 'SUCCESS').length}
                </div>
                <div style={{ fontSize: '13px', color: '#10b981' }}>Success</div>
              </div>
              <div style={{ padding: '16px', background: 'rgba(239, 68, 68, 0.1)', borderRadius: '12px', textAlign: 'center' }}>
                <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#ef4444' }}>
                  {executions.filter(e => e.status === 'FAILED').length}
                </div>
                <div style={{ fontSize: '13px', color: '#ef4444' }}>Failed</div>
              </div>
              <div style={{ padding: '16px', background: 'rgba(59, 130, 246, 0.1)', borderRadius: '12px', textAlign: 'center' }}>
                <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#3b82f6' }}>
                  {executions.filter(e => e.status === 'RUNNING').length}
                </div>
                <div style={{ fontSize: '13px', color: '#3b82f6' }}>Running</div>
              </div>
              <div style={{ padding: '16px', background: 'rgba(255, 255, 255, 0.05)', borderRadius: '12px', textAlign: 'center' }}>
                <div style={{ fontSize: '24px', fontWeight: 'bold', color: 'var(--text-primary)' }}>
                  {executions.length}
                </div>
                <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>Total</div>
              </div>
            </div>
          </div>
        </div>

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        {!error && executions.length === 0 && (
          <div className="alert alert-info">
            No pipeline executions yet. Trigger your first pipeline to get started!
          </div>
        )}

        {!error && executions.length > 0 && (
          <div style={{ overflowX: 'auto' }}>
            <table>
              <thead>
                <tr>
                  <th>Build #</th>
                  <th>Student</th>
                  <th>Branch</th>
                  <th>Status</th>
                  <th>Stage</th>
                  <th>Tests</th>
                  <th>Duration</th>
                  <th>Started At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {executions.map((execution) => (
                  <tr key={execution.id}>
                    <td>
                      <strong>#{execution.buildNumber || execution.id}</strong>
                    </td>
                    <td>{execution.studentName}</td>
                    <td>
                      <code>{execution.branchName || 'main'}</code>
                    </td>
                    <td>
                      <StatusBadge status={execution.status} />
                    </td>
                    <td>{execution.currentStage || 'N/A'}</td>
                    <td>
                      {execution.totalTests !== null ? (
                        <span>
                          {execution.testsPassed || 0}/{execution.totalTests || 0}
                          {execution.testsFailed > 0 && (
                            <span style={{ color: '#ef4444', marginLeft: '4px' }}>
                              ({execution.testsFailed})
                            </span>
                          )}
                        </span>
                      ) : (
                        'N/A'
                      )}
                    </td>
                    <td>{formatDuration(execution.duration)}</td>
                    <td>{formatDate(execution.startedAt)}</td>
                    <td>
                      <button
                        className="btn btn-primary"
                        style={{ padding: '6px 16px', fontSize: '13px' }}
                        onClick={() => navigate(`/execution/${execution.id}`)}
                      >
                        Details
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
