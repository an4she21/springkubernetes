# Phase 1: Secrets & ConfigMaps - Deployment Guide

## What Was Changed

### New Files Created
1. **db-secret.yml** - Contains all database credentials and connection strings
2. **configmap.yml** - Contains application configuration (non-sensitive)

### Updated Files
1. **db-deployment.yml** - Now uses Secrets for database credentials
2. **backend-deployment.yml** - Now uses Secrets for DB connection and ConfigMap for app config
3. **frontend-deployment.yml** - Now uses ConfigMap for environment variables

## Deployment Steps

### 1. Apply Secrets and ConfigMap First
```bash
cd C:\Users\anas\Desktop\Kubernetes\springbootdevops

# Apply the secret (must be first)
kubectl apply -f db-secret.yml

# Apply the configmap
kubectl apply -f configmap.yml
```

### 2. Verify Secrets and ConfigMap
```bash
# Check secrets
kubectl get secrets
kubectl describe secret db-secret

# Check configmaps
kubectl get configmaps
kubectl describe configmap app-config
```

### 3. Update Deployments
```bash
# Update database deployment
kubectl apply -f db-deployment.yml

# Update backend deployment
kubectl apply -f backend-deployment.yml

# Update frontend deployment
kubectl apply -f frontend-deployment.yml
```

### 4. Verify Pods Are Running
```bash
# Watch pod status
kubectl get pods -w

# Check if pods are using secrets/configmaps correctly
kubectl describe pod <pod-name>
```

### 5. Test Application
```bash
# Get service URLs
minikube service springboot-frontend --url
minikube service springboot-backend --url

# Test backend API
curl http://<minikube-ip>:<port>/api/proprietaires
```

## Verification Checklist

- [ ] Secrets created successfully
- [ ] ConfigMap created successfully
- [ ] Database pod restarted and running
- [ ] Backend pods restarted and running
- [ ] Frontend pods restarted and running
- [ ] Application is accessible
- [ ] Database connection works
- [ ] No errors in pod logs

## Troubleshooting

### If pods fail to start:
```bash
# Check pod logs
kubectl logs <pod-name>

# Check pod events
kubectl describe pod <pod-name>

# Verify secrets/configmaps exist
kubectl get secrets
kubectl get configmaps
```

### If database connection fails:
- Verify secret values are correct
- Check if secret keys match what's referenced in deployments
- Verify database pod is running

### Common Issues:
1. **Secret not found**: Make sure to apply `db-secret.yml` before deployments
2. **ConfigMap not found**: Make sure to apply `configmap.yml` before deployments
3. **Wrong key names**: Verify key names in deployments match secret/configmap keys

## Rollback (if needed)

If something goes wrong, you can rollback:

```bash
# Delete the updated deployments
kubectl delete -f backend-deployment.yml
kubectl delete -f db-deployment.yml
kubectl delete -f frontend-deployment.yml

# Revert to original files (if you have backups)
# Or manually edit to use hardcoded values again
```

## Next Steps

After Phase 1 is complete and verified:
- Proceed to Phase 2: Ingress Controller
- See IMPLEMENTATION_PLAN.md for details
