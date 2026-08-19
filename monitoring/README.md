# Monitoring

The dashboard in `grafana-dashboard.json` expects the Prometheus datasource named `Prometheus`.

For a local Grafana container, mount these paths:

- `provisioning/datasources/prometheus.yml` to `/etc/grafana/provisioning/datasources/prometheus.yml`
- `provisioning/dashboards/dashboard.yml` to `/etc/grafana/provisioning/dashboards/dashboard.yml`
- `grafana-dashboard.json` to `/var/lib/grafana/dashboards/checkout-platform.json`

The Kubernetes monitoring manifest exposes Prometheus on port `9090` and Grafana on port `3000`.
