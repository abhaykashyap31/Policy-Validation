# Travel Planner

Spring Boot service for employee travel requests and policy validation.

## Modules

- `employee`: employee master data used to select travel policies. The employee `grade` is required for policy selection during validation. Employee codes and emails are unique in PostgreSQL.
- `travelrequest`: proposed employee trips and workflow status. Requests are checked for required fields, valid date order, and non-negative estimated cost before persistence.
- `policy`: travel policies, rules, validators, and validation results. Supported rule types are `FLIGHT_CLASS`, `ADVANCE_BOOKING`, and `HOTEL_LIMIT`. The current request model has no hotel-specific amount, so `HOTEL_LIMIT` compares against `estimatedCost`.
- `booking`: reserved for booking workflows
- `approval`: reserved for approval workflows

## Endpoints

### Employee

- `POST /api/employees` — create an employee
- `GET /api/employees` — list all employees
- `GET /api/employees/{id}` — get employee by id
- `GET /api/employees/code/{employeeCode}` — get employee by code
- `PUT /api/employees/{id}` — update an employee
- `DELETE /api/employees/{id}` — delete an employee

### Travel Request

- `POST /api/travel-requests/employee/{employeeId}` — create a draft request
- `GET /api/travel-requests/{id}` — get request status

### Policy

- `POST /api/policies` — create a policy with nested rules
- `GET /api/policies/{id}` — get a policy and its rules
- `POST /api/policy-validations` — validate using `{ "travelRequestId": 1 }`
- `POST /api/policy-validations/travel-request/{travelRequestId}` — validate a request by path id

The runtime database is PostgreSQL. Tests use H2 so the Spring context can start without an external database.

## Local PostgreSQL setup

Create the database with a PostgreSQL administrator account:

```sql
CREATE DATABASE travel_planner;
```

The application connects to `localhost:5432/travel_planner` as `postgres` by default. Override the connection when your local installation uses different credentials:

```bash
export DB_USERNAME=your_postgres_user
export DB_PASSWORD='REPLACE_WITH_YOUR_REAL_POSTGRES_PASSWORD'
export DB_URL=jdbc:postgresql://localhost:5432/travel_planner

psql -h localhost -U "$DB_USERNAME" -d travel_planner -c 'select 1;'
./mvnw spring-boot:run
```

Replace the password placeholder with the real PostgreSQL password; do not use the placeholder literally. Keep the Spring Boot terminal running while sending Postman requests. The API is ready when the startup logs show that port `8080` is listening.

Flyway applies `src/main/resources/db/migration/V1__create_travel_planner_schema.sql` automatically on startup. Since the tables may already exist, Flyway baselines the existing database at version 1; Hibernate then validates the schema rather than creating or altering tables.

## End-to-end validation example

Create an employee:

```bash
curl -X POST http://localhost:8080/api/employees \
	-H 'Content-Type: application/json' \
	-d '{"employeeCode":"EMP-1001","name":"Asha Rao","email":"asha@example.com","grade":"G5","designation":"Engineer","departmentId":10}'
```

Create a policy and its rules:

```bash
curl -X POST http://localhost:8080/api/policies \
	-H 'Content-Type: application/json' \
	-d '{"name":"G5 Travel Policy","description":"Standard G5 travel limits","policyType":"EMPLOYEE_GRADE","version":1,"grade":"G5","rules":[{"ruleCode":"FLIGHT-G5","ruleType":"FLIGHT_CLASS","ruleValue":"ECONOMY","severity":"ERROR","description":"G5 flights must be economy"},{"ruleCode":"ADVANCE-G5","ruleType":"ADVANCE_BOOKING","ruleValue":"7","severity":"WARNING","description":"Book at least seven days ahead"}]}'
```

Create a travel request using the returned employee id:

```bash
curl -X POST http://localhost:8080/api/travel-requests/employee/1 \
	-H 'Content-Type: application/json' \
	-d '{"purpose":"Client meeting","sourceCity":"Pune","destinationCity":"Delhi","departureDate":"2026-10-15","returnDate":"2026-10-18","travelClass":"BUSINESS","estimatedCost":25000}'
```

Validate it using the returned travel request id:

```bash
curl -X POST http://localhost:8080/api/policy-validations/travel-request/1
```

## Postman

Import `postman/TravelPlanner.postman_collection.json` into Postman and run the requests in order. The collection stores the employee, policy, and travel request IDs automatically and verifies the request status after validation.
