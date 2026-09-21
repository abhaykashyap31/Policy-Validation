#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MVNW="$ROOT_DIR/mvnw"
CONFIG_REPO="file:$ROOT_DIR/infrastructure/config-repo"
PORTS=(8761 8888 8082)

port_in_use() {
    local port="$1"
    ss -ltn "sport = :$port" 2>/dev/null | awk 'NR > 1 { found=1 } END { exit !found }'
}

for port in "${PORTS[@]}"; do
    if port_in_use "$port"; then
        echo "Port $port is already in use. Stop the existing microservices before starting this stack." >&2
        exit 1
    fi
done

stop_process_group() {
    local pid="$1"

    if [[ -n "$pid" ]] && kill -0 "$pid" 2>/dev/null; then
        kill -TERM -- "-$pid" 2>/dev/null || kill -TERM "$pid" 2>/dev/null || true
        for _ in {1..50}; do
            kill -0 "$pid" 2>/dev/null || break
            sleep 0.1
        done
        kill -KILL -- "-$pid" 2>/dev/null || kill -KILL "$pid" 2>/dev/null || true
    fi
}

cleanup() {
    stop_process_group "${EUREKA_PID:-}"
    stop_process_group "${CONFIG_PID:-}"
    stop_process_group "${TRAVEL_PID:-}"
    stop_process_group "${POLICY_PID:-}"
}
trap cleanup EXIT INT TERM

setsid "$MVNW" -q -f "$ROOT_DIR/infrastructure/eureka-server/pom.xml" spring-boot:run &
EUREKA_PID=$!

until curl -fsS http://localhost:8761/eureka/apps >/dev/null 2>&1; do
    kill -0 "$EUREKA_PID" 2>/dev/null
    sleep 1
done

SPRING_CLOUD_CONFIG_SERVER_NATIVE_SEARCH_LOCATIONS="$CONFIG_REPO" \
    setsid "$MVNW" -q -f "$ROOT_DIR/infrastructure/config-server/pom.xml" spring-boot:run &
CONFIG_PID=$!

until curl -fsS http://localhost:8888/travel-planner/default >/dev/null 2>&1; do
    kill -0 "$CONFIG_PID" 2>/dev/null
    sleep 1
done

run_service() {
    local port="$1"
    local name="$2"

    SERVER_PORT="$port" \
    SPRING_APPLICATION_NAME="$name" \
    SPRING_CONFIG_IMPORT=optional:configserver:http://localhost:8888 \
    EUREKA_SERVER_URL=http://localhost:8761/eureka/ \
        setsid "$MVNW" -q spring-boot:run &
}

run_service 8082 policy-service
POLICY_PID=$!

echo "Eureka:        http://localhost:8761"
echo "Config Server: http://localhost:8888"
echo "policy-service:  http://localhost:8082"
echo "Press Ctrl+C to stop all services."

wait