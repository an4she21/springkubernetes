# Phase 2: Ingress Controller - Deployment Guide

## What Was Changed

### New Files Created
1. **ingress.yml** - Ingress resource with routing rules

### Updated Files
1. **backend-deployment.yml** - Service changed from NodePort to ClusterIP
2. **frontend-deployment.yml** - Service changed from NodePort to ClusterIP

## Deployment Status

✅ Ingress addon enabled in minikube
✅ Ingress resource created
✅ Services updated to ClusterIP
✅ Ingress is ready at: `192.168.49.2`

## Accessing the Application via Ingress

### Option 1: Using Host Header (Recommended)

1. **Add host entry to hosts file** (Windows):
   ```powershell
   # Run PowerShell as Administrator
   Add-Content -Path C:\Windows\System32\drivers\etc\hosts -Value "`n192.168.49.2 springboot-app.local"
   ```

2. **Access the application**:
   - Frontend: `http://springboot-app.local`
   - Backend API: `http://springboot-app.local/api/proprietaires`
   - Swagger UI: `http://springboot-app.local/api/swagger-ui.html`

### Option 2: Using minikube tunnel (Alternative)

1. **Start minikube tunnel** (in a separate terminal):
   ```bash
   minikube tunnel
   ```
   Keep this running in the background.

2. **Access via localhost**:
   - Frontend: `http://localhost`
   - Backend API: `http://localhost/api/proprietaires`

### Option 3: Direct IP Access (with Host Header)

```bash
# Using curl with host header
curl -H "Host: springboot-app.local" http://192.168.49.2/
curl -H "Host: springboot-app.local" http://192.168.49.2/api/proprietaires
```

## Testing the Ingress

### 1. Verify Ingress Status
```bash
kubectl get ingress
kubectl describe ingress springboot-ingress
```

### 2. Check Ingress Controller Pods
```bash
kubectl get pods -n ingress-nginx
```

### 3. Test Frontend
```bash
# With host header
curl -H "Host: springboot-app.local" http://192.168.49.2/

# Or add to hosts file and access via browser
# http://springboot-app.local
```

### 4. Test Backend API
```bash
# Test API endpoint
curl -H "Host: springboot-app.local" http://192.168.49.2/api/proprietaires

# Test Swagger UI (in browser)
# http://springboot-app.local/api/swagger-ui.html
```

## Ingress Routing Rules

The ingress routes traffic as follows:
- **`/`** → Frontend service (port 80)
- **`/api/*`** → Backend service (port 8080)

## Benefits of Using Ingress

✅ **Clean URLs** - No port numbers needed
✅ **Single Entry Point** - One URL for all services
✅ **Path-based Routing** - Automatic routing based on paths
✅ **Production Ready** - Standard Kubernetes pattern
✅ **SSL/TLS Ready** - Easy to add certificates later

## Troubleshooting

### Ingress not accessible?

1. **Check ingress controller is running**:
   ```bash
   kubectl get pods -n ingress-nginx
   ```

2. **Check ingress status**:
   ```bash
   kubectl describe ingress springboot-ingress
   ```

3. **Check ingress controller logs**:
   ```bash
   kubectl logs -n ingress-nginx -l app.kubernetes.io/component=controller
   ```

4. **Verify services are ClusterIP**:
   ```bash
   kubectl get services
   # Should show ClusterIP, not NodePort
   ```

### Can't access via hostname?

- Make sure hosts file entry is correct
- Try using `minikube tunnel` instead
- Use curl with `-H "Host: springboot-app.local"` header

### Backend API not working?

- Verify backend pods are running: `kubectl get pods`
- Check backend logs: `kubectl logs -l app=springboot-backend`
- Test backend service directly: `kubectl port-forward svc/springboot-backend 8080:8080`

## Next Steps

After Phase 2 is complete and verified:
- Proceed to Phase 3: Monitoring (Prometheus + Grafana)
- See IMPLEMENTATION_PLAN.md for details
