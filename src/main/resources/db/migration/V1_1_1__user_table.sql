CREATE TABLE eis_cafe.users (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           username VARCHAR NOT NULL,
                           name VARCHAR NULL,
                           password VARCHAR NOT NULL,
                           email VARCHAR NULL,
                           phone VARCHAR NULL,
                           is_active BOOLEAN NOT NULL DEFAULT TRUE,
                           is_deleted BOOLEAN NOT NULL DEFAULT FALSE,  -- Soft delete flag
                           failed_login_attempts INT DEFAULT 0,
                           locked_until TIMESTAMP NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           deleted_at TIMESTAMP NULL,                  -- When soft-deleted
                           created_by VARCHAR NULL,
                           updated_by VARCHAR NULL,
                           deleted_by VARCHAR NULL,                    -- Who deleted

                           CONSTRAINT user_username_key UNIQUE (username),
                           CONSTRAINT user_email_key UNIQUE (email)
);