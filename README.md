# Movieflix
#  MovieFlix – Netflix-like Streaming App

MovieFlix is a Netflix-inspired video streaming platform built using a modern microservices architecture. It allows users to register, browse movies, stream content online, and manage subscriptions with secure payment integration.

##  Features

-  User authentication (JWT)
- Stream and watch movies online
-  Offline video support
-  Stripe-integrated payment system
-  Email notifications for subscriptions and updates
-  Admin panel to manage content
-  Microservices-based architecture
-  Monitoring and health checks with Prometheus and Grafana
-  Service discovery with Eureka
-  Redis caching for performance

##  Microservice Architecture

```plaintext
 ┌──────────────┐    ┌──────────────┐
 │  API Gateway │───▶│  User Service│
 └─────┬────────┘    └──────────────┘
       │                 │
       ▼                 ▼
┌──────────────┐    ┌──────────────┐
│Video Service │    │Playback Svc  │
└─────┬────────┘    └────┬─────────┘
      ▼                 ▼
┌──────────────┐    ┌──────────────┐
│Payment Svc   │    │Notification  │
└──────────────┘    └──────────────┘
