# PowerShell script to start port-forwarding for Prometheus and Grafana
# Run this script to access monitoring services

Write-Host "Starting port-forwarding for monitoring services..." -ForegroundColor Green
Write-Host ""

# Start Prometheus port-forward in background
Write-Host "Starting Prometheus port-forward (9090 -> 9090)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward -n monitoring svc/prometheus 9090:9090" -WindowStyle Minimized

Start-Sleep -Seconds 2

# Start Grafana port-forward in background
Write-Host "Starting Grafana port-forward (3000 -> 3000)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward -n monitoring svc/grafana 3000:3000" -WindowStyle Minimized

Start-Sleep -Seconds 2

Write-Host ""
Write-Host "Port-forwarding started!" -ForegroundColor Green
Write-Host ""
Write-Host "Access URLs:" -ForegroundColor Cyan
Write-Host "  Prometheus: http://localhost:9090" -ForegroundColor White
Write-Host "  Grafana:    http://localhost:3000 (admin/admin)" -ForegroundColor White
Write-Host ""
Write-Host "Actuator Endpoints (via Ingress):" -ForegroundColor Cyan
Write-Host "  Health:      http://springboot-app.local/actuator/health" -ForegroundColor White
Write-Host "  Prometheus:  http://springboot-app.local/actuator/prometheus" -ForegroundColor White
Write-Host "  Metrics:     http://springboot-app.local/actuator/metrics" -ForegroundColor White
Write-Host ""
Write-Host "To stop port-forwarding, close the PowerShell windows that opened." -ForegroundColor Yellow
