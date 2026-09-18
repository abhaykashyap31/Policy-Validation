ALTER TABLE travel_request ADD COLUMN travel_mode VARCHAR(20);
UPDATE travel_request SET travel_mode = 'FLIGHT' WHERE travel_mode IS NULL;
ALTER TABLE travel_request ALTER COLUMN travel_mode SET NOT NULL;