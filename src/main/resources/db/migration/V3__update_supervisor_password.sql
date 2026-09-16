-- Atualizar a senha do supervisor para um hash BCrypt válido da senha 'admin123'
UPDATE app_user 
SET password = '$2a$10$NaUlW883KMRxjlED8wxxzeYeaZhGLwxt/3171LzF5oGEiKUBnfwCu' 
WHERE email = 'supervisor@velas.com';
