# Phase 3: Monitoring (Prometheus + Grafana) - Deployment Guide

## What Was Created

### New Files Created
1. **monitoring/namespace.yml** - Monitoring namespace
2. **monitoring/prometheus-config.yml** - Prometheus configuration
3. **monitoring/prometheus-deployment.yml** - Prometheus deployment, service, RBAC
4. **monitoring/grafana-deployment.yml** - Grafana deployment and service
5. **monitoring/service-monitor.yml** - ServiceMonitor for application metrics (optional)

### Updated Files
1. **backend-deployment.yml** - Added Prometheus annotations for metrics scraping

## Deployment Status

✅ Monitoring namespace created
✅ Prometheus deployed
✅ Grafana deployed
✅ Backend pods annotated for Prometheus scraping

## Access URLs

### Prometheus
- **URL**: `http://192.168.49.2:30090`
- **Direct**: `http://<minikube-ip>:30090`
- **Via minikube**: `minikube service prometheus -n monitoring --url`

### Grafana
- **URL**: `http://192.168.49.2:30300`
- **Direct**: `http://<minikube-ip>:30300`
- **Via minikube**: `minikube service grafana -n monitoring --url`
- **Default Login**:
  - Username: `admin`
  - Password: `admin`

## Verification Steps

### 1. Check Pod Status
```bash
kubectl get pods -n monitoring
```

Both Prometheus and Grafana should show `1/1 Running`.

### 2. Check Services
```bash
kubectl get svc -n monitoring
```

### 3. Access Prometheus UI
1. Open browser: `http://192.168.49.2:30090`
2. Go to **Status > Targets** to see scraped endpoints
3. Go to **Graph** to query metrics

### 4. Access Grafana UI
1. Open browser: `http://192.168.49.2:30300`
2. Login with `admin/admin`
3. Go to **Configuration > Data Sources**
4. Verify Prometheus is configured (should be auto-configured)
5. Go to **Dashboards** to create/view dashboards

## Available Metrics

### Kubernetes Metrics
- Node metrics (CPU, memory, disk)
- Pod metrics (CPU, memory)
- Container metrics (via cAdvisor)

### Application Metrics
- Spring Boot backend pods (if Actuator is enabled)
- HTTP request metrics
- JVM metrics

## Creating Grafana Dashboards

### Quick Start Dashboard
1. Login to Grafana
2. Go to **Dashboards > New Dashboard**
3. Add panels with Prometheus queries:
   - CPU Usage: `rate(container_cpu_usage_seconds_total[5m])`
   - Memory Usage: `container_memory_usage_bytes`
   - HTTP Requests: `http_requests_total`

### Import Pre-built Dashboards
1. Go to **Dashboards > Import**
2. Use dashboard IDs:
   - Kubernetes Cluster: `7249`
   - Kubernetes Pods: `6417`
   - Spring Boot: `11378`

## Prometheus Queries

### Useful Queries

**Pod CPU Usage:**
```promql
rate(container_cpu_usage_seconds_total{namespace="default"}[5m])
```

**Pod Memory Usage:**
```promql
container_memory_usage_bytes{namespace="default"}
```

**HTTP Request Rate:**
```promql
rate(http_requests_total[5m])
```

**Backend Pod Count:**
```promql
count(kube_pod_info{app="springboot-backend"})
```

## Troubleshooting

### Prometheus not scraping targets?

1. **Check Prometheus targets**:
   ```bash
   # Access Prometheus UI > Status > Targets
   # Or check logs
   kubectl logs -n monitoring -l app=prometheus
   ```

2. **Verify pod annotations**:
   ```bash
   kubectl describe pod <pod-name> | grep prometheus
   ```

3. **Check Prometheus config**:
   ```bash
   kubectl get configmap prometheus-config -n monitoring -o yaml
   ```

### Grafana can't connect to Prometheus?

1. **Verify Prometheus service**:
   ```bash
   kubectl get svc prometheus -n monitoring
   ```

2. **Check Grafana datasource config**:
   ```bash
   kubectl get configmap grafana-datasources -n monitoring -o yaml
   ```

3. **Test connection from Grafana pod**:
   ```bash
   kubectl exec -n monitoring -it <grafana-pod> -- wget -O- http://prometheus.monitoring.svc.cluster.local:9090/api/v1/status/config
   ```

### Pods not showing metrics?

1. **Verify annotations are present**:
   ```bash
   kubectl get deployment springboot-backend -o yaml | grep prometheus
   ```

2. **Check if Spring Boot Actuator is enabled** (for application metrics):
   - Add Actuator dependency to `pom.xml`
   - Enable Prometheus endpoint in `application.properties`

## Adding Spring Boot Actuator (Optional)

To get application-specific metrics:

1. **Add to pom.xml**:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-registry-prometheus</artifactId>
   </dependency>
   ```

2. **Add to application.properties**:
   ```properties
   management.endpoints.web.exposure.include=prometheus,health,info
   management.metrics.export.prometheus.enabled=true
   ```

3. **Rebuild and redeploy**:
   ```bash
   docker build -t springboot-backend:latest .
   kubectl rollout restart deployment/springboot-backend
   ```

## Next Steps

After Phase 3 is complete and verified:
- Proceed to Phase 4: Jenkins CI/CD
- See IMPLEMENTATION_PLAN.md for details

## Resource Usage

- **Prometheus**: ~512Mi memory, ~250m CPU
- **Grafana**: ~256Mi memory, ~100m CPU
- **Total**: ~768Mi memory, ~350m CPU

Make sure your minikube has sufficient resources allocated.
