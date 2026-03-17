-- ════════════════════════════════════════════════════════════════════════════════
-- 2. auth_providers – for OAuth / social logins
-- ════════════════════════════════════════════════════════════════════════════════
CREATE TABLE auth_providers (
                                id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                provider    VARCHAR(50) NOT NULL,          -- 'google', 'github', 'apple', 'discord', ...
                                provider_id VARCHAR(255) NOT NULL,         -- sub / id from provider
                                email       VARCHAR(255),
                                name VARCHAR(100),
                                avatar_url  TEXT,
                                access_token TEXT,
                                refresh_token TEXT,
                                expires_at  TIMESTAMPTZ,
                                created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                UNIQUE (provider, provider_id)
);

CREATE INDEX idx_auth_providers_user ON auth_providers(user_id);

-- ════════════════════════════════════════════════════════════════════════════════
-- 3. refresh_tokens – session management
-- ════════════════════════════════════════════════════════════════════════════════
CREATE TABLE refresh_tokens (
                                id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                token           TEXT NOT NULL UNIQUE,
                                expires_at      TIMESTAMPTZ NOT NULL,
                                revoked_at      TIMESTAMPTZ,
                                created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                created_from_ip VARCHAR(45),
                                created_from_ua TEXT,
                                device_id       VARCHAR(100)

);

-- ════════════════════════════════════════════════════════════════════════════════
-- 4. roles
-- ════════════════════════════════════════════════════════════════════════════════
CREATE TABLE roles (
                       id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       code        VARCHAR(50)     NOT NULL UNIQUE,        -- used in code
                       name        VARCHAR(100)    NOT NULL,
                       description TEXT,
                       is_system   BOOLEAN         NOT NULL DEFAULT FALSE, -- protect built-in roles
                       created_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

-- ════════════════════════════════════════════════════════════════════════════════
-- 5. permissions
-- ════════════════════════════════════════════════════════════════════════════════
CREATE TABLE permissions (
                             id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             code        VARCHAR(120)    NOT NULL UNIQUE,        -- 'post:delete:any', 'user:ban'
                             name        VARCHAR(150)    NOT NULL,
                             description TEXT,
                             module      VARCHAR(50),                            -- 'POST', 'USER', 'COMMENT', 'MODERATION'
                             created_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

-- ════════════════════════════════════════════════════════════════════════════════
-- 6. role_permissions
-- ════════════════════════════════════════════════════════════════════════════════
CREATE TABLE role_permissions (
                                  role_id       UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                                  permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
                                  PRIMARY KEY (role_id, permission_id)
);

-- ════════════════════════════════════════════════════════════════════════════════
-- 7. user_roles
-- ════════════════════════════════════════════════════════════════════════════════
CREATE TABLE user_roles (
                            user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id     UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                            assigned_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                            assigned_by UUID REFERENCES users(id),
                            expires_at  TIMESTAMPTZ,
                            PRIMARY KEY (user_id, role_id)
);

-- ════════════════════════════════════════════════════════════════════════════════
-- Seed data – realistic defaults for a social app in 2026
-- ════════════════════════════════════════════════════════════════════════════════

-- Roles
INSERT INTO roles (code, name, description, is_system) VALUES
                                                           ('USER',          'Regular User',         'Standard registered user',               true),
                                                           ('VERIFIED',      'Verified Account',     'Verified creator / public figure',       true),
                                                           ('MODERATOR',     'Moderator',            'Content & user moderation',              true),
                                                           ('ADMIN',         'Administrator',        'Full system access',                     true),
                                                           ('PREMIUM',       'Premium Member',       'Paid features & badge',                  false);

-- Permissions (most common ones for social app)
INSERT INTO permissions (code, name, module, description) VALUES
                                                              -- Post related
                                                              ('post:create',          'Create posts',                  'POST',     'Can create new posts'),
                                                              ('post:read',            'Read posts',                    'POST',     'View posts (basic)'),
                                                              ('post:update:own',      'Edit own posts',                'POST',     'Edit posts they created'),
                                                              ('post:delete:own',      'Delete own posts',              'POST',     'Delete own posts'),
                                                              ('post:delete:any',      'Delete any post',               'MODERATION','Moderate content – delete others'),
                                                              ('post:pin',             'Pin posts',                     'POST',     'Pin posts to profile/feed'),

                                                              -- Comment related
                                                              ('comment:create',       'Create comments',               'COMMENT',  'Post comments'),
                                                              ('comment:delete:own',   'Delete own comments',           'COMMENT',  'Delete own comments'),
                                                              ('comment:delete:any',   'Delete any comment',            'MODERATION','Moderate comments'),

                                                              -- User & profile
                                                              ('user:read:private',    'View private profiles',         'USER',     'See hidden/private profiles'),
                                                              ('user:ban',             'Ban/suspend users',             'MODERATION','Ban or suspend accounts'),
                                                              ('user:verify',          'Verify accounts',               'MODERATION','Grant verified badge'),

                                                              -- Admin / system
                                                              ('admin:settings',       'Change system settings',        'ADMIN',    'Access admin panel settings'),
                                                              ('admin:roles',          'Manage roles & permissions',    'ADMIN',    'Assign roles, create permissions');

--