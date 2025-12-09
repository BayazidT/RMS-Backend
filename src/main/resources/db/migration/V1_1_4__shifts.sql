-- V2__simple_weekly_shifts.sql
-- PostgreSQL (also works on MySQL 8+, just replace gen_random_uuid() with UUID())

CREATE TABLE employee_weekly_schedule (
                                          schedule_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          user_id         UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                          day_of_week     SMALLINT    NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
                                          start_time      TIME        NOT NULL,   -- e.g. '09:00:00'
                                          end_time        TIME        NOT NULL,   -- e.g. '17:00:00'
                                          is_off          BOOLEAN     NOT NULL DEFAULT FALSE,  -- true = day off

                                          CONSTRAINT chk_end_after_start CHECK (end_time > start_time OR is_off),
                                          UNIQUE(user_id, day_of_week)
);

CREATE TABLE shifts (
                        shift_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        user_id         UUID    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        shift_date      DATE    NOT NULL,
                        start_time      TIMESTAMPTZ NOT NULL,
                        end_time        TIMESTAMPTZ NOT NULL,

                        CONSTRAINT chk_end_after_start CHECK (end_time > start_time),
                        UNIQUE(user_id, shift_date)
);

CREATE INDEX idx_shifts_date ON shifts(shift_date);
CREATE INDEX idx_shifts_user_date ON shifts(user_id, shift_date);