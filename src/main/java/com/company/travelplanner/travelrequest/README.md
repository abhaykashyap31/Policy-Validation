# Travel Request Module

Creates travel requests belonging to employees and maintains their workflow status.

## API

- `POST /api/travel-requests/employee/{employeeId}` creates a draft request.
- `GET /api/travel-requests/{id}` returns the current request status.

Requests are checked for required fields, valid date order, and non-negative estimated cost before persistence.
