// API Configuration
// In Docker, the backend is accessible via the service name 'backend'
// In development, use localhost
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export default API_BASE_URL;
