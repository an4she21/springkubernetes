# Kubernetes Advanced Features Implementation Plan

## Overview
This document outlines the plan to add Ingress, ConfigMaps, Secrets, Monitoring (Prometheus/Grafana), and Jenkins CI/CD to the Spring Boot DevOps project.

---

## 1. INGRESS CONTROLLER

### Purpose
- Replace NodePort services with Ingress for cleaner URL routing
- Enable domain-based routing (e.g., `app.local` instead of IP:port)
- Support SSL/TLS termination
- Better production-ready setup

### Implementation Steps
1. **Install Ingress Controller** (NGINX Ingress for minikube)
   ```bash
   minikube addons enable ingress
   ```

2. **Create Ingress Resource** (`ingress.yml`)
   - Route `/` → frontend service
   - Route `/api/*` → backend service
   - Configure host: `springboot-app.local` (for local dev)

3. **Update Services**
   - Change services from `NodePort` to `ClusterIP`
   - Services will be accessed only through Ingress

### Files to Create
- `ingress.yml` - Ingress resource configuration

### Benefits
- Clean URLs without port numbers
- Single entry point for all services
- Easy SSL certificate management
- Production-ready routing

---

## 2. CONFIGMAP

### Purpose
- Externalize non-sensitive configuration
- Separate config from code
- Easy configuration updates without rebuilding images

### Configuration to Move to ConfigMap
- Database connection settings (non-sensitive parts)
- Application properties (JPA settings, logging levels)
- Frontend environment variables
- API endpoints configuration

### Files to Create
- `configmap.yml` - Contains application configuration
- Update `backend-deployment.yml` to reference ConfigMap
- Update `frontend-deployment.yml` to reference ConfigMap

### Example ConfigMap Data
```yaml
spring.jpa.show-sql: "true"
spring.jpa.hibernate.ddl-auto: "update"
logging.level.com.example.demo: "INFO"
REACT_APP_API_URL: "/api"
```

---

## 3. SECRETS

### Purpose
- Securely store sensitive data (passwords, API keys)
- Encrypted at rest in Kubernetes
- Base64 encoded (not encrypted, but better than plain text)

### Secrets to Create
1. **Database Secrets** (`db-secret.yml`)
   - Root password
   - Database user password
   - Database name

2. **Application Secrets** (if needed)
   - API keys
   - JWT secrets
   - Other sensitive credentials

### Files to Create
- `db-secret.yml` - Database credentials
- Update `db-deployment.yml` to use secrets
- Update `backend-deployment.yml` to use secrets

### Security Best Practices
- Never commit secrets to git
- Use `kubectl create secret` for production
- Consider using external secret management (HashiCorp Vault, AWS Secrets Manager)

---

## 4. MONITORING (Prometheus + Grafana)

### Purpose
- Monitor application metrics (CPU, memory, requests)
- Track application performance
- Set up dashboards for visualization
- Alert on issues

### Components to Install
1. **Prometheus**
   - Metrics collection and storage
   - Scrapes metrics from pods
   - Time-series database

2. **Grafana**
   - Visualization and dashboards
   - Connects to Prometheus
   - Pre-built dashboards for Kubernetes

3. **ServiceMonitor** (if using Prometheus Operator)
   - Defines what to scrape
   - Auto-discovery of services

### Files to Create
- `monitoring/namespace.yml` - Create monitoring namespace
- `monitoring/prometheus-config.yml` - Prometheus configuration
- `monitoring/prometheus-deployment.yml` - Prometheus deployment
- `monitoring/grafana-deployment.yml` - Grafana deployment
- `monitoring/service-monitor.yml` - ServiceMonitor for app metrics
- Update deployments with Prometheus annotations

### Metrics to Monitor
- Pod CPU/Memory usage
- Request rates and latencies
- Database connection pool
- Application-specific metrics (if added)

### Access
- Prometheus UI: `http://<minikube-ip>:<port>`
- Grafana UI: `http://<minikube-ip>:<port>` (default login: admin/admin)

---

## 5. JENKINS CI/CD

### Purpose
- Automate build, test, and deployment
- Continuous Integration pipeline
- Automated Docker image building
- Automated Kubernetes deployments

### Jenkins Setup
1. **Jenkins Deployment**
   - Deploy Jenkins in Kubernetes
   - Persistent storage for Jenkins data
   - Service to access Jenkins UI

2. **Jenkins Pipeline** (Jenkinsfile)
   - Build Spring Boot application
   - Build Docker images
   - Push to registry (optional)
   - Deploy to Kubernetes
   - Run tests

3. **Required Plugins**
   - Kubernetes Plugin
   - Docker Pipeline Plugin
   - Git Plugin

### Files to Create
- `jenkins/jenkins-deployment.yml` - Jenkins deployment
- `jenkins/jenkins-service.yml` - Jenkins service
- `jenkins/jenkins-pvc.yml` - Persistent volume for Jenkins
- `Jenkinsfile` - Pipeline definition (in project root)
- `jenkins/jenkins-rbac.yml` - ServiceAccount and RBAC for Jenkins

### Pipeline Stages
1. **Checkout** - Get code from repository
2. **Build** - Maven build (`mvn clean package`)
3. **Test** - Run unit tests
4. **Build Docker Images** - Build backend and frontend images
5. **Deploy** - Apply Kubernetes manifests
6. **Verify** - Health checks and smoke tests

### Jenkins Access
- Jenkins UI: `http://<minikube-ip>:<port>`
- Initial admin password from pod logs
- Configure pipeline from Jenkinsfile

---

## IMPLEMENTATION ORDER

### Phase 1: Foundation (Secrets & ConfigMaps)
1. Create `db-secret.yml` with database credentials
2. Create `configmap.yml` with application config
3. Update deployments to use Secrets and ConfigMaps
4. Test that application still works

### Phase 2: Ingress
1. Enable Ingress addon in minikube
2. Create `ingress.yml`
3. Change services to ClusterIP
4. Test routing through Ingress

### Phase 3: Monitoring
1. Create monitoring namespace
2. Deploy Prometheus
3. Deploy Grafana
4. Configure ServiceMonitors
5. Set up dashboards

### Phase 4: CI/CD (Jenkins)
1. Deploy Jenkins
2. Configure Jenkins plugins
3. Create Jenkinsfile
4. Set up pipeline
5. Test automated deployment

---

## FILE STRUCTURE (After Implementation)

```
springbootdevops/
├── backend-deployment.yml      (updated - uses Secrets/ConfigMap)
├── db-deployment.yml            (updated - uses Secrets)
├── frontend-deployment.yml      (updated - uses ConfigMap)
├── ingress.yml                  (new)
├── configmap.yml                (new)
├── db-secret.yml                (new)
├── monitoring/
│   ├── namespace.yml
│   ├── prometheus-config.yml
│   ├── prometheus-deployment.yml
│   ├── grafana-deployment.yml
│   └── service-monitor.yml
├── jenkins/
│   ├── jenkins-deployment.yml
│   ├── jenkins-service.yml
│   ├── jenkins-pvc.yml
│   └── jenkins-rbac.yml
└── Jenkinsfile                  (new - in root)
```

---

## PREREQUISITES

- Minikube running
- kubectl configured
- Docker available
- Sufficient resources (CPU/RAM) for all components
- Basic understanding of:
  - Kubernetes concepts
  - YAML syntax
  - Docker
  - CI/CD concepts

---

## TESTING CHECKLIST

After each phase, verify:
- [ ] All pods are running
- [ ] Services are accessible
- [ ] Application functionality works
- [ ] No errors in pod logs
- [ ] Resources are properly configured

---

## NOTES

- **Secrets**: In production, use proper secret management tools
- **Monitoring**: Start with basic metrics, expand as needed
- **Jenkins**: Consider using Jenkins Operator for easier management
- **Ingress**: For production, use proper domain names and SSL certificates
- **Resource Limits**: Add CPU/memory limits to all deployments

---

## ESTIMATED TIME

- Phase 1 (Secrets & ConfigMaps): 30 minutes
- Phase 2 (Ingress): 20 minutes
- Phase 3 (Monitoring): 45 minutes
- Phase 4 (Jenkins): 60 minutes

**Total: ~2.5 hours**

---

## NEXT STEPS

1. Review this plan
2. Confirm implementation order
3. Start with Phase 1 (Secrets & ConfigMaps)
4. Test after each phase
5. Proceed to next phase only after verification
