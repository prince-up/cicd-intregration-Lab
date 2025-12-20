/**
 * API SERVICE
 * 
 * This module handles all HTTP requests to the backend API.
 * Uses axios for HTTP communication.
 */

import axios from 'axios';

// Backend API base URL
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:9090/api/pipeline';

// Create axios instance with default configuration
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Log the API URL being used to help with debugging
console.log('🔌 API Service Initialized');
console.log('📍 Target Backend URL:', API_BASE_URL);

// Add request interceptor for debugging
api.interceptors.request.use(request => {
  console.log('🚀 Starting Request:', request.method.toUpperCase(), request.url);
  return request;
});

/**
 * Trigger a new CI/CD pipeline
 * 
 * @param {Object} pipelineData - Pipeline request data
 * @returns {Promise} Pipeline response
 */
export const triggerPipeline = async (pipelineData) => {
  try {
    const response = await api.post('/trigger', pipelineData);
    return response.data;
  } catch (error) {
    console.error('❌ Error triggering pipeline:', error);
    if (error.response) {
      // The request was made and the server responded with a status code
      // that falls out of the range of 2xx
      console.error('Data:', error.response.data);
      console.error('Status:', error.response.status);
    } else if (error.request) {
      // The request was made but no response was received
      console.error('No response received:', error.request);
    } else {
      // Something happened in setting up the request that triggered an Error
      console.error('Error Message:', error.message);
    }
    throw error;
  }
};

/**
 * Get all pipeline executions
 * 
 * @returns {Promise} List of pipeline executions
 */
export const getAllExecutions = async () => {
  try {
    const response = await api.get('/executions');
    return response.data;
  } catch (error) {
    console.error('Error fetching executions:', error);
    throw error;
  }
};

/**
 * Get pipeline execution by ID
 * 
 * @param {number} id - Execution ID
 * @returns {Promise} Pipeline execution details
 */
export const getExecutionById = async (id) => {
  try {
    const response = await api.get(`/executions/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching execution:', error);
    throw error;
  }
};

/**
 * Get executions by student name
 * 
 * @param {string} studentName - Student name
 * @returns {Promise} List of student's executions
 */
export const getExecutionsByStudent = async (studentName) => {
  try {
    const response = await api.get(`/student/${studentName}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching student executions:', error);
    throw error;
  }
};

/**
 * Get test results for an execution
 * 
 * @param {number} executionId - Execution ID
 * @returns {Promise} List of test results
 */
export const getTestResults = async (executionId) => {
  try {
    const response = await api.get(`/executions/${executionId}/tests`);
    return response.data;
  } catch (error) {
    console.error('Error fetching test results:', error);
    throw error;
  }
};

/**
 * Health check
 * 
 * @returns {Promise} Health status
 */
export const healthCheck = async () => {
  try {
    const response = await api.get('/health');
    return response.data;
  } catch (error) {
    console.error('Error checking health:', error);
    throw error;
  }
};

export default api;
