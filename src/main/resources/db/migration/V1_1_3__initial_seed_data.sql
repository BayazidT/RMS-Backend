-- USER permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         CROSS JOIN permissions p
WHERE r.code = 'USER'
  AND p.code IN ('post:create', 'post:read', 'post:update:own', 'post:delete:own',
                 'comment:create', 'comment:delete:own')
    ON CONFLICT (role_id, permission_id) DO NOTHING;

-- MODERATOR permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         CROSS JOIN permissions p
WHERE r.code = 'MODERATOR'
  AND (p.code LIKE '%:delete:any%'
    OR p.code IN ('user:ban', 'user:verify', 'post:pin'))
    ON CONFLICT (role_id, permission_id) DO NOTHING;

-- ADMIN gets everything
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         CROSS JOIN permissions p
WHERE r.code = 'ADMIN'
    ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Example admin user (idempotent version)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin') THEN
        INSERT INTO users (username, name, email, password, is_active)
        VALUES ('admin', 'System Admin', 'admin@trbtree.com', '$2b$12$...hashed...', true);
END IF;
END $$;

-- Assign ADMIN role (also idempotent)
INSERT INTO user_roles (user_id, role_id, assigned_by)
SELECT u.id, r.id, u.id
FROM users u
         JOIN roles r ON r.code = 'ADMIN'
WHERE u.username = 'admin'
    ON CONFLICT (user_id, role_id) DO NOTHING;