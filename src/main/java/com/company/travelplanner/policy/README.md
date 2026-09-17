# Policy Module

Owns travel policies, policy rules, validation results, violations, and rule validators.

## API

- `POST /api/policies` creates a policy with nested rules.
- `GET /api/policies/{id}` returns a policy and its rules.
- `POST /api/policy-validations/travel-request/{travelRequestId}` validates a request.
- `POST /api/policy-validations` validates using `{ "travelRequestId": 1 }`.

Supported rule types are `FLIGHT_CLASS`, `ADVANCE_BOOKING`, and `HOTEL_LIMIT`. The current request model has no hotel-specific amount, so `HOTEL_LIMIT` compares against `estimatedCost`.
