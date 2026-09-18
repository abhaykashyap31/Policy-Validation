# Policy Module

Owns travel policies, policy rules, validation results, violations, and rule validators.

## API

- `POST /api/policies` creates a policy with nested rules.
- `GET /api/policies/{id}` returns a policy and its rules.
- `POST /api/policy-validations/travel-request/{travelRequestId}` validates a request.
- `POST /api/policy-validations` validates using `{ "travelRequestId": 1 }`.

Supported rule types are `TRAVEL_MODE`, `TRAVEL_CLASS`, `TRAVEL_COST_LIMIT`, and `ADVANCE_BOOKING`. Travel modes are `CAB`, `FLIGHT`, and `TRAIN`; `TRAVEL_CLASS` applies to flights, while `TRAVEL_COST_LIMIT` applies to any travel mode.
