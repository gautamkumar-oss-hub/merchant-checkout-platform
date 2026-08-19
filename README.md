# Merchant Checkout Platform

A personal checkout and order-processing service implemented with Java, Spring Boot, PostgreSQL, Redis, RabbitMQ, Docker, Kubernetes, Jenkins, and Grafana.

## Overview

This project delivers a resilient e-commerce checkout API that separates fast user-facing checkout responses from slower backend order processing.

Key capabilities:
- Checkout and cart session management with Redis-backed state
- PostgreSQL as the system of record for orders and inventory data
- Idempotency-key handling to prevent duplicate order creation under client retries
- Asynchronous order processing via RabbitMQ for inventory validation and confirmation events
- Dockerized deployment with Kubernetes manifests
- Jenkins CI/CD pipeline to build, test, and deploy services
- Grafana monitoring for order flow and queue metrics

## Architecture

The service is designed around the following components:

- Spring Boot REST API for checkout and cart operations
- Redis for fast cart session caching and temporary checkout state
- PostgreSQL for persistent order storage and transactional consistency
- RabbitMQ for decoupling checkout request handling from downstream order workflows
- Background consumers to process inventory updates and confirmation notifications asynchronously

## Features

- Fast cart-to-order conversion with immediate checkout response
- Robust duplicate prevention using idempotency keys
- Decoupled order processing to maintain responsiveness under load
- Asynchronous event consumers for inventory and confirmation handling
- Docker and Kubernetes support for containerized deployment
- Jenkins-driven CI/CD pipeline
- Grafana dashboards for monitoring order counts, queue latency, and service health

## Technology Stack

- Java
- Spring Boot
- PostgreSQL
- Redis
- RabbitMQ
- Docker
- Kubernetes
- Jenkins
- Grafana

## Usage

1. Start PostgreSQL, Redis, and RabbitMQ locally, or deploy `k8s/dependencies.yaml`.
2. Build the service with `mvn clean package` and run `java -jar target/checkout-platform-0.1.0.jar`.
3. Save a cart with `POST /carts`, retrieve it with `GET /carts/{cartId}`, and remove it with `DELETE /carts/{cartId}`.
4. Create an order with `POST /checkout` and an `Idempotency-Key`. The API returns immediately while inventory and confirmation events are processed through RabbitMQ.
5. Scrape `/actuator/prometheus` with Prometheus and import `monitoring/grafana-dashboard.json` into Grafana.

## Notes

The application uses environment variables for dependency hosts and credentials, and the test profile uses an embedded H2 database so the test suite requires no external data or services.
