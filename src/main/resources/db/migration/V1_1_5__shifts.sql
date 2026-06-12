-- V2__simple_weekly_shifts.sql
-- PostgreSQL (also works on MySQL 8+, just replace gen_random_uuid() with UUID())
CREATE TABLE shifts (
                        id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        user_id         UUID    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        shift_date      DATE    NOT NULL,
                        start_time      TIMESTAMPTZ NOT NULL,
                        end_time        TIMESTAMPTZ NOT NULL,

                        CONSTRAINT chk_end_after_start CHECK (end_time > start_time),
                        UNIQUE(user_id, shift_date)
);

CREATE INDEX idx_shifts_date ON shifts(shift_date);
CREATE INDEX idx_shifts_user_date ON shifts(user_id, shift_date);