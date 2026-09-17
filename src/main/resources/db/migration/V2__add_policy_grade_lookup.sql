ALTER TABLE travel_policy
    ADD COLUMN IF NOT EXISTS grade VARCHAR(100);

CREATE INDEX IF NOT EXISTS idx_travel_policy_grade_active
    ON travel_policy(grade, active);
