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

> Replace this section with your actual run instructions once the source code is available.

1. Build the Spring Boot service with Maven or Gradle.
2. Start PostgreSQL, Redis, and RabbitMQ.
3. Deploy the service locally or to Kubernetes.
4. Use `POST /checkout` with an idempotency key to create an order.

## Notes

This repository currently contains the project README and license. Add the application source, configuration, Docker manifests, and CI/CD pipeline files to complete the implementation.
