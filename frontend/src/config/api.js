// API Configuration
// In Kubernetes, nginx proxies /api/* requests to the backend service
// In development, set REACT_APP_API_URL=http://localhost:8080/api
const API_BASE_URL = process.env.REACT_APP_API_URL || '/api';

export default API_BASE_URL;
