--weekly_schedule.sql
CREATE TABLE weekly_schedule (
                                 id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                 user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

                                 day_of_week   INT NOT NULL CHECK (day_of_week BETWEEN 1 AND 7), -- 1=Monday ... 7=Sunday

                                 start_time    TIME,           -- null = day off
                                 end_time    TIME,           -- null = day off

                                 is_off      BOOLEAN NOT NULL DEFAULT FALSE,  -- explicit "off" flag (easier for UI)
                                 is_half     BOOLEAN DEFAULT FALSE,
                                 created_at  TIMESTAMPTZ DEFAULT NOW(),
                                 updated_at  TIMESTAMPTZ DEFAULT NOW(),

                                 UNIQUE(user_id, day_of_week)
);

-- Indexes for speed
CREATE INDEX idx_weekly_schedule_user ON weekly_schedule(user_id);
