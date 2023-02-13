CREATE TABLE IF NOT EXISTS users (
         email VARCHAR(255) PRIMARY KEY NOT NULL,
         username VARCHAR(30) CONSTRAINT user_username_not_null_unique NOT NULL UNIQUE,
         password VARCHAR(255) CONSTRAINT user_password_not_null NOT NULL,
         role VARCHAR(50) CONSTRAINT user_role_check CHECK(role IN ('ADMIN','USER'))
);
