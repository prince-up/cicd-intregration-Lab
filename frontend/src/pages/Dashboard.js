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

  // Fetch executions on component mount
  useEffect(() => {
    fetchExecutions();
    
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
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <div>
            <h1 style={{ margin: 0, color: '#1f2937' }}>Pipeline Dashboard</h1>
            <p style={{ margin: '8px 0 0 0', color: '#6b7280' }}>
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
                  <th>Student Name</th>
                  <th>Repository</th>
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
                    <td style={{ maxWidth: '200px', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                      {execution.repositoryUrl}
                    </td>
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
                              ({execution.testsFailed} failed)
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
                        style={{ padding: '6px 12px', fontSize: '14px' }}
                        onClick={() => navigate(`/execution/${execution.id}`)}
                      >
                        View Details
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="card">
        <h3 style={{ marginBottom: '12px', color: '#1f2937' }}>
          Pipeline Statistics
        </h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '16px' }}>
          <div style={{ textAlign: 'center', padding: '16px', background: '#f9fafb', borderRadius: '8px' }}>
            <div style={{ fontSize: '32px', fontWeight: 'bold', color: '#667eea' }}>
              {executions.length}
            </div>
            <div style={{ color: '#6b7280', marginTop: '4px' }}>Total Builds</div>
          </div>
          <div style={{ textAlign: 'center', padding: '16px', background: '#f9fafb', borderRadius: '8px' }}>
            <div style={{ fontSize: '32px', fontWeight: 'bold', color: '#10b981' }}>
              {executions.filter(e => e.status === 'SUCCESS').length}
            </div>
            <div style={{ color: '#6b7280', marginTop: '4px' }}>Successful</div>
          </div>
          <div style={{ textAlign: 'center', padding: '16px', background: '#f9fafb', borderRadius: '8px' }}>
            <div style={{ fontSize: '32px', fontWeight: 'bold', color: '#ef4444' }}>
              {executions.filter(e => e.status === 'FAILED').length}
            </div>
            <div style={{ color: '#6b7280', marginTop: '4px' }}>Failed</div>
          </div>
          <div style={{ textAlign: 'center', padding: '16px', background: '#f9fafb', borderRadius: '8px' }}>
            <div style={{ fontSize: '32px', fontWeight: 'bold', color: '#3b82f6' }}>
              {executions.filter(e => e.status === 'RUNNING').length}
            </div>
            <div style={{ color: '#6b7280', marginTop: '4px' }}>Running</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
