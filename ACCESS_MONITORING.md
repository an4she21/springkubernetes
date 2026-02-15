# Accessing Monitoring Services (Prometheus & Grafana)

## Issue
On Windows with minikube, NodePort services are not directly accessible from the host machine. You need to use port-forwarding or minikube tunnel.

## Solution 1: Port Forwarding (Recommended for Quick Access)

### Access Prometheus
```bash
# In a terminal, run:
kubectl port-forward -n monitoring svc/prometheus 9090:9090

# Then access: http://localhost:9090
```

### Access Grafana
```bash
# In another terminal, run:
kubectl port-forward -n monitoring svc/grafana 3000:3000

# Then access: http://localhost:3000
# Login: admin/admin
```

## Solution 2: Minikube Tunnel (For Persistent Access)

### Start Minikube Tunnel
```bash
# Run in a separate terminal (keep it running):
minikube tunnel

# This will make NodePort services accessible at:
# Prometheus: http://localhost:30090
# Grafana: http://localhost:30300
```

**Note:** Keep the tunnel running in the background. Press Ctrl+C to stop it.

## Solution 3: Access via Ingress (Future Enhancement)

You can also add Prometheus and Grafana to the Ingress for unified access:
- `http://springboot-app.local/prometheus`
- `http://springboot-app.local/grafana`

## Quick Access Commands

### Prometheus
```bash
# Start port-forward
kubectl port-forward -n monitoring svc/prometheus 9090:9090

# Access: http://localhost:9090
```

### Grafana
```bash
# Start port-forward
kubectl port-forward -n monitoring svc/grafana 3000:3000

# Access: http://localhost:3000
# Default credentials: admin/admin
```

## Actuator Endpoints

Actuator endpoints are now accessible via Ingress:
- Health: `http://springboot-app.local/actuator/health`
- Prometheus: `http://springboot-app.local/actuator/prometheus`
- Metrics: `http://springboot-app.local/actuator/metrics`
- Info: `http://springboot-app.local/actuator/info`

## Verify Services Are Running

```bash
# Check pods
kubectl get pods -n monitoring

# Check services
kubectl get svc -n monitoring

# Check if port-forward is working
netstat -an | findstr "9090"
netstat -an | findstr "3000"
```
