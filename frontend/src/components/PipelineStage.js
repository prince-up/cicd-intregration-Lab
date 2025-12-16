/**
 * PIPELINE STAGE COMPONENT - VS Code Theme
 * Professional stage indicators with progress visualization
 */

import React from 'react';

const PipelineStage = ({ name, status, icon }) => {
  const getStageColor = () => {
    switch (status?.toUpperCase()) {
      case 'SUCCESS':
        return { main: '#3fb950', bg: 'rgba(63, 185, 80, 0.15)', border: 'rgba(63, 185, 80, 0.4)' };
      case 'FAILED':
      case 'FAILURE':
        return { main: '#f85149', bg: 'rgba(248, 81, 73, 0.15)', border: 'rgba(248, 81, 73, 0.4)' };
      case 'RUNNING':
        return { main: '#58a6ff', bg: 'rgba(88, 166, 255, 0.15)', border: 'rgba(88, 166, 255, 0.4)' };
      default:
        return { main: '#6e7681', bg: 'rgba(110, 118, 129, 0.1)', border: 'rgba(110, 118, 129, 0.3)' };
    }
  };

  const getStageIcon = () => {
    if (icon) return icon;
    
    switch (status?.toUpperCase()) {
      case 'SUCCESS':
        return (
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M5 13L9 17L19 7" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        );
      case 'FAILED':
      case 'FAILURE':
        return (
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M6 6L18 18M6 18L18 6" stroke="currentColor" strokeWidth="3" strokeLinecap="round"/>
          </svg>
        );
      case 'RUNNING':
        return (
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" className="spinner">
            <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="3" opacity="0.25"/>
            <path d="M12 2a10 10 0 0 1 10 10" stroke="currentColor" strokeWidth="3" strokeLinecap="round"/>
          </svg>
        );
      default:
        return (
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="2"/>
          </svg>
        );
    }
  };

  const colors = getStageColor();

  return (
    <>
      <style>{`
        @keyframes spin {
          from { transform: rotate(0deg); }
          to { transform: rotate(360deg); }
        }
        .spinner {
          animation: spin 1s linear infinite;
        }
      `}</style>
      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: '16px',
        padding: '16px 20px',
        borderRadius: '8px',
        marginBottom: '12px',
        position: 'relative',
        overflow: 'hidden',
        background: colors.bg,
        border: `1px solid ${colors.border}`,
        boxShadow: status?.toUpperCase() === 'RUNNING' ? `0 0 16px ${colors.border}` : 'none'
      }}>
        <div style={{
          width: '44px',
          height: '44px',
          borderRadius: '50%',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          background: 'rgba(22, 27, 34, 0.5)',
          border: `2px solid ${colors.main}`,
          color: colors.main,
          flexShrink: 0
        }}>
          {getStageIcon()}
        </div>
        <div style={{ flex: 1 }}>
          <div style={{
            fontSize: '15px',
            fontWeight: '600',
            color: '#c9d1d9',
            marginBottom: '4px',
            letterSpacing: '-0.2px'
          }}>
            {name}
          </div>
          <div style={{
            fontSize: '12px',
            fontWeight: '500',
            textTransform: 'uppercase',
            letterSpacing: '0.5px',
            color: colors.main
          }}>
            {status || 'PENDING'}
          </div>
        </div>
        <div style={{
          position: 'absolute',
          left: 0,
          top: 0,
          bottom: 0,
          width: '4px',
          background: colors.main,
          opacity: status ? 1 : 0.3
        }} />
      </div>
    </>
  );
};

export default PipelineStage;
