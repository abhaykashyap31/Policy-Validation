# Employee Module

Owns employee master data used to select travel policies.

## API

- `POST /api/employees` creates an employee.
- `GET /api/employees/{id}` returns an employee.

The employee `grade` is required for policy selection during validation. Employee codes and emails are unique in PostgreSQL.
