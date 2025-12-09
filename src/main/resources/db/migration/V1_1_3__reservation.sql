-- src/main/resources/db/migration/V5__reservations.sql
CREATE TABLE rms.reservations (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  user_id UUID NOT NULL REFERENCES rms.users(id) ON DELETE RESTRICT,
                                  table_number INT NOT NULL CHECK (table_number >= 1),
                                  guest_count INT NOT NULL CHECK (guest_count >= 1 AND guest_count <= 20),
                                  reservation_date DATE NOT NULL,
                                  reservation_time TIME NOT NULL,
                                  status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                                      CHECK (status IN ('PENDING', 'CONFIRMED', 'SEATED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')),
                                  customer_name VARCHAR(100) NOT NULL,
                                  customer_phone VARCHAR(30),
                                  customer_email VARCHAR(100),
                                  special_requests TEXT,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  created_by UUID REFERENCES rms.users(id),
                                  notes TEXT
);

-- Indexes for performance
CREATE INDEX idx_res_user_id ON rms.reservations(user_id);
CREATE INDEX idx_res_date ON rms.reservations(reservation_date);
CREATE INDEX idx_res_time ON rms.reservations(reservation_time);
CREATE INDEX idx_res_status ON rms.reservations(status);
CREATE INDEX idx_res_table ON rms.reservations(table_number);

-- Unique: one table at one time
CREATE UNIQUE INDEX uq_res_table_datetime
    ON rms.reservations(table_number, reservation_date, reservation_time);