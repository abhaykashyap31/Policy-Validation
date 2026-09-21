# Microservice Infrastructure

This directory contains the supporting services for local microservice deployment:

- `eureka-server`: service registry on port `8761`.
- `config-server`: Spring Cloud Config Server on port `8888`, using the native repository in `config-repo`.
- `config-repo`: external configuration files keyed by Spring application name.

Docker is optional. Start the complete local topology with Maven from the repository root:

```bash
./scripts/run-local-microservices.sh
```

This starts Eureka, Config Server, `travel-planner` on port `8081`, and a second instance named `policy-service` on port `8082`.

Alternatively, start the containerized topology:

```bash
docker compose up --build
```

The travel-planner application registers as `travel-planner` and reads its configuration from the Config Server. Override `CONFIG_SERVER_URL` and `EUREKA_SERVER_URL` for another environment.

## Service-to-service calls

Use the injected `NamedServiceClient` with the Eureka application name:

```java
namedServiceClient.get("policy-service", "/api/policies/1", PolicyResponse.class);
```

The load-balanced client resolves `policy-service` through Eureka. Do not use an IP address, `localhost`, or a fixed remote port in application code. A second service can run on the same host with a different `SERVER_PORT`; its `spring.application.name` must be unique.