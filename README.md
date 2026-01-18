# Docker Setup Guide
## Quick Start


1. **Start services in detached mode (background):**
   ```bash
   docker-compose up -d --build
   ```


## Services

The application consists of three services:
- **backend**: Spring Boot API (port 8080)
- **frontend**: React application served by Nginx (port 3000)   

## Accessing the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080