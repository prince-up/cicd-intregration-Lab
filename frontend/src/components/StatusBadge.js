/**
 * STATUS BADGE COMPONENT - GitHub/VS Code Theme
 * Professional status indicators with glow effects
 */

import React from 'react';

const StatusBadge = ({ status }) => {
  const getStatusStyle = () => {
    const baseStyle = {
      padding: '6px 14px',
      borderRadius: '6px',
      fontSize: '12px',
      fontWeight: '600',
      display: 'inline-flex',
      alignItems: 'center',
      gap: '6px',
      border: '1px solid',
      textTransform: 'uppercase',
      letterSpacing: '0.5px'
    };

    switch (status?.toUpperCase()) {
      case 'SUCCESS':
        return { 
          ...baseStyle, 
          background: 'rgba(63, 185, 80, 0.15)', 
          color: '#3fb950',
          borderColor: 'rgba(63, 185, 80, 0.4)',
          boxShadow: '0 0 12px rgba(63, 185, 80, 0.2)'
        };
      case 'FAILED':
      case 'FAILURE':
        return { 
          ...baseStyle, 
          background: 'rgba(248, 81, 73, 0.15)', 
          color: '#f85149',
          borderColor: 'rgba(248, 81, 73, 0.4)',
          boxShadow: '0 0 12px rgba(248, 81, 73, 0.2)'
        };
      case 'RUNNING':
        return { 
          ...baseStyle, 
          background: 'rgba(88, 166, 255, 0.15)', 
          color: '#58a6ff',
          borderColor: 'rgba(88, 166, 255, 0.4)',
          boxShadow: '0 0 12px rgba(88, 166, 255, 0.2)',
          animation: 'pulse 2s ease-in-out infinite'
        };
      case 'PENDING':
        return { 
          ...baseStyle, 
          background: 'rgba(163, 113, 247, 0.15)', 
          color: '#a371f7',
          borderColor: 'rgba(163, 113, 247, 0.4)'
        };
      default:
        return { 
          ...baseStyle, 
          background: 'rgba(139, 148, 158, 0.15)', 
          color: '#8b949e',
          borderColor: 'rgba(139, 148, 158, 0.4)'
        };
    }
  };

  const getIcon = () => {
    switch (status?.toUpperCase()) {
      case 'SUCCESS':
        return '✓';
      case 'FAILED':
      case 'FAILURE':
        return '✗';
      case 'RUNNING':
        return '⟳';
      case 'PENDING':
        return '○';
      default:
        return '◉';
    }
  };

  return (
    <>
      <style>{`
        @keyframes pulse {
          0%, 100% { opacity: 1; }
          50% { opacity: 0.6; }
        }
      `}</style>
      <span style={getStatusStyle()}>
        <span>{getIcon()}</span>
        {status || 'UNKNOWN'}
      </span>
    </>
  );
};

export default StatusBadge;
