/**
 * EXECUTION DETAILS PAGE
 * 
 * Displays detailed information about a specific pipeline execution
 */

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getExecutionById, getTestResults } from '../services/api';
import StatusBadge from '../components/StatusBadge';
import PipelineStage from '../components/PipelineStage';

const ExecutionDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [execution, setExecution] = useState(null);
  const [testResults, setTestResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchExecutionDetails();
    
    // Auto-refresh every 3 seconds if pipeline is running
    const interval = setInterval(() => {
      if (execution?.status === 'RUNNING' || execution?.status === 'PENDING') {
        fetchExecutionDetails();
      }
    }, 3000);
    
    return () => clearInterval(interval);
  }, [id]);

  const fetchExecutionDetails = async () => {
    try {
      const execData = await getExecutionById(id);
      setExecution(execData);
      
      // Fetch test results if available
      if (execData.totalTests > 0) {
        try {
          const tests = await getTestResults(id);
          setTestResults(tests);
        } catch (err) {
          console.log('No test results yet');
        }
      }
      
      setError(null);
    } catch (err) {
      setError('Failed to fetch execution details');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleString();
  };

  const formatDuration = (seconds) => {
    if (!seconds) return 'N/A';
    if (seconds < 60) return `${seconds} seconds`;
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes} minutes ${secs} seconds`;
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

  if (error || !execution) {
    return (
      <div className="container">
        <div className="alert alert-error">
          {error || 'Execution not found'}
        </div>
        <button className="btn btn-primary" onClick={() => navigate('/dashboard')}>
          Back to Dashboard
        </button>
      </div>
    );
  }

  return (
    <div className="container">
      {/* Header */}
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '16px' }}>
          <div>
            <h1 style={{ margin: 0, color: '#1f2937' }}>
              Build #{execution.buildNumber || execution.id}
            </h1>
            <p style={{ margin: '8px 0 0 0', color: '#6b7280' }}>
              Execution Details
            </p>
          </div>
          <StatusBadge status={execution.status} />
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px', marginTop: '20px' }}>
          <div>
            <strong style={{ color: '#6b7280', fontSize: '14px' }}>Student Name</strong>
            <div style={{ fontSize: '16px', marginTop: '4px' }}>{execution.studentName}</div>
          </div>
          <div>
            <strong style={{ color: '#6b7280', fontSize: '14px' }}>Repository</strong>
            <div style={{ fontSize: '16px', marginTop: '4px', wordBreak: 'break-all' }}>
              {execution.repositoryUrl}
            </div>
          </div>
          <div>
            <strong style={{ color: '#6b7280', fontSize: '14px' }}>Branch</strong>
            <div style={{ fontSize: '16px', marginTop: '4px' }}>
              <code>{execution.branchName || 'main'}</code>
            </div>
          </div>
          <div>
            <strong style={{ color: '#6b7280', fontSize: '14px' }}>Duration</strong>
            <div style={{ fontSize: '16px', marginTop: '4px' }}>
              {formatDuration(execution.duration)}
            </div>
          </div>
        </div>

        <div style={{ marginTop: '16px', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
          <div>
            <strong style={{ color: '#6b7280', fontSize: '14px' }}>Started At</strong>
            <div style={{ fontSize: '16px', marginTop: '4px' }}>{formatDate(execution.startedAt)}</div>
          </div>
          <div>
            <strong style={{ color: '#6b7280', fontSize: '14px' }}>Completed At</strong>
            <div style={{ fontSize: '16px', marginTop: '4px' }}>{formatDate(execution.completedAt)}</div>
          </div>
        </div>

        <button 
          className="btn btn-primary" 
          onClick={() => navigate('/dashboard')}
          style={{ marginTop: '20px' }}
        >
          ← Back to Dashboard
        </button>
      </div>

      {/* Pipeline Stages */}
      <div className="card">
        <h2 style={{ marginBottom: '16px', color: '#1f2937' }}>Pipeline Stages</h2>
        <PipelineStage name="Checkout" status={execution.currentStage === 'CHECKOUT' ? 'RUNNING' : 'SUCCESS'} icon="📥" />
        <PipelineStage name="Build" status={execution.buildStatus} icon="🔨" />
        <PipelineStage name="Test" status={execution.testStatus} icon="✅" />
        <PipelineStage name="Package" status={execution.currentStage === 'PACKAGE' ? 'RUNNING' : execution.buildStatus} icon="📦" />
        <PipelineStage name="Deploy" status={execution.deploymentStatus} icon="🚀" />
      </div>

      {/* Test Results */}
      {execution.totalTests > 0 && (
        <div className="card">
          <h2 style={{ marginBottom: '16px', color: '#1f2937' }}>Test Results</h2>
          
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '12px', marginBottom: '20px' }}>
            <div style={{ textAlign: 'center', padding: '12px', background: '#f9fafb', borderRadius: '8px' }}>
              <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#667eea' }}>
                {execution.totalTests}
              </div>
              <div style={{ color: '#6b7280', fontSize: '14px' }}>Total Tests</div>
            </div>
            <div style={{ textAlign: 'center', padding: '12px', background: '#d1fae5', borderRadius: '8px' }}>
              <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#10b981' }}>
                {execution.testsPassed || 0}
              </div>
              <div style={{ color: '#065f46', fontSize: '14px' }}>Passed</div>
            </div>
            <div style={{ textAlign: 'center', padding: '12px', background: '#fee2e2', borderRadius: '8px' }}>
              <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#ef4444' }}>
                {execution.testsFailed || 0}
              </div>
              <div style={{ color: '#991b1b', fontSize: '14px' }}>Failed</div>
            </div>
          </div>

          {testResults.length > 0 && (
            <div style={{ overflowX: 'auto' }}>
              <table>
                <thead>
                  <tr>
                    <th>Test Class</th>
                    <th>Test Method</th>
                    <th>Status</th>
                    <th>Duration</th>
                    <th>Error</th>
                  </tr>
                </thead>
                <tbody>
                  {testResults.map((test) => (
                    <tr key={test.id}>
                      <td><code>{test.testClass}</code></td>
                      <td><code>{test.testMethod}</code></td>
                      <td>
                        <StatusBadge status={test.status} />
                      </td>
                      <td>{test.durationMs}ms</td>
                      <td style={{ color: '#ef4444', maxWidth: '300px', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                        {test.errorMessage || '-'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {/* Error Message */}
      {execution.errorMessage && (
        <div className="card">
          <h2 style={{ marginBottom: '16px', color: '#1f2937' }}>Error Details</h2>
          <div className="alert alert-error">
            {execution.errorMessage}
          </div>
        </div>
      )}
    </div>
  );
};

export default ExecutionDetails;
