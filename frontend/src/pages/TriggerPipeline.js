/**
 * TRIGGER PIPELINE PAGE
 * 
 * Form to trigger a new CI/CD pipeline
 */

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { triggerPipeline } from '../services/api';

const TriggerPipeline = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    studentName: '',
    repositoryUrl: '',
    branchName: 'main',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      // Extract owner and repo from URL
      const urlParts = formData.repositoryUrl.replace('.git', '').split('/');
      const owner = urlParts[urlParts.length - 2];
      const repo = urlParts[urlParts.length - 1];
      
      // Fetch latest commit SHA from GitHub
      let commitHash = null;
      try {
        const githubResponse = await fetch(
          `https://api.github.com/repos/${owner}/${repo}/commits/${formData.branchName}`
        );
        if (githubResponse.ok) {
          const commitData = await githubResponse.json();
          commitHash = commitData.sha;
          console.log('Fetched commit SHA:', commitHash);
        }
      } catch (err) {
        console.warn('Could not fetch commit SHA:', err);
      }
      
      // Trigger pipeline with commit hash
      const response = await triggerPipeline({
        ...formData,
        commitHash: commitHash
      });
      setSuccess(true);
      
      // Redirect to execution details after 2 seconds
      setTimeout(() => {
        navigate(`/execution/${response.id}`);
      }, 2000);
    } catch (err) {
      setError('Failed to trigger pipeline. Make sure the backend is running.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1 style={{ marginBottom: '8px', color: '#1f2937' }}>
          Trigger New Pipeline
        </h1>
        <p style={{ color: '#6b7280', marginBottom: '24px' }}>
          Start a new CI/CD pipeline build by providing your project details
        </p>

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        {success && (
          <div className="alert alert-success">
            ✓ Pipeline triggered successfully! Redirecting to execution details...
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="studentName">
              Student Name <span style={{ color: '#ef4444' }}>*</span>
            </label>
            <input
              type="text"
              id="studentName"
              name="studentName"
              value={formData.studentName}
              onChange={handleChange}
              placeholder="Enter your name"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="repositoryUrl">
              GitHub Repository URL <span style={{ color: '#ef4444' }}>*</span>
            </label>
            <input
              type="url"
              id="repositoryUrl"
              name="repositoryUrl"
              value={formData.repositoryUrl}
              onChange={handleChange}
              placeholder="https://github.com/username/repository"
              required
            />
            <small style={{ color: '#6b7280', display: 'block', marginTop: '4px' }}>
              Example: https://github.com/student/my-project
            </small>
          </div>

          <div className="form-group">
            <label htmlFor="branchName">
              Branch Name <span style={{ color: '#ef4444' }}>*</span>
            </label>
            <input
              type="text"
              id="branchName"
              name="branchName"
              value={formData.branchName}
              onChange={handleChange}
              placeholder="main"
              required
            />
            <small style={{ color: '#6b7280', display: 'block', marginTop: '4px' }}>
              Usually "main" or "master"
            </small>
          </div>

          <div style={{ display: 'flex', gap: '12px', marginTop: '24px' }}>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Triggering...' : '🚀 Trigger Pipeline'}
            </button>
            <button
              type="button"
              className="btn"
              style={{ background: '#e5e7eb', color: '#374151' }}
              onClick={() => navigate('/dashboard')}
              disabled={loading}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>

      <div className="card">
        <h3 style={{ marginBottom: '12px', color: '#1f2937' }}>
          What Happens Next? 🔄
        </h3>
        <div style={{ lineHeight: '1.8', color: '#4b5563' }}>
          <p style={{ marginBottom: '12px' }}>
            After triggering the pipeline, the following stages will execute automatically:
          </p>
          <ol style={{ paddingLeft: '20px' }}>
            <li style={{ marginBottom: '8px' }}>
              <strong>Checkout:</strong> Jenkins clones your GitHub repository
            </li>
            <li style={{ marginBottom: '8px' }}>
              <strong>Build:</strong> Maven compiles your Java source code
            </li>
            <li style={{ marginBottom: '8px' }}>
              <strong>Test:</strong> JUnit runs all automated tests
            </li>
            <li style={{ marginBottom: '8px' }}>
              <strong>Package:</strong> Maven creates an executable JAR file
            </li>
            <li style={{ marginBottom: '8px' }}>
              <strong>Deploy:</strong> Application is deployed to the server
            </li>
          </ol>
          <p style={{ marginTop: '16px', padding: '12px', background: '#dbeafe', borderRadius: '6px' }}>
            💡 <strong>Tip:</strong> You can monitor the pipeline status in real-time on the dashboard!
          </p>
        </div>
      </div>
    </div>
  );
};

export default TriggerPipeline;
