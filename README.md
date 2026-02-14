
# Kubernetes Setup Guide (Minikube)

## Prerequisites
 
- Minikube installed
- kubectl installed
- Docker available

## Quick Start
 
1. Start Minikube:
    ```bash
   minikube start
    ```
 
2. Use Minikube Docker daemon so local images are available inside cluster:
   ```bash
   eval $(minikube docker-env)
   ```
 
3. Build backend and frontend images:
   ```bash
   docker build -t springboot-backend:latest .
   docker build -t springboot-frontend:latest ./frontend
   ```
 

4. Apply Kubernetes manifests:
   ```bash
   kubectl apply -f db-deployment.yml
   kubectl apply -f backend-deployment.yml
   kubectl apply -f frontend-deployment.yml
   ```
 
 ## Accessing the Application
 

- Frontend URL:
  ```bash
  minikube service springboot-frontend --url
  # Exposed as service port 3000 (targeting container port 80)
  ```

- Backend URL:
  ```bash
  minikube service springboot-backend --url
  ```

