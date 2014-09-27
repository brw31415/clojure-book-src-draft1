-- name: insert-user<!
-- Inserts a new user into the Users table
-- Expects :username, :email, and :password
INSERT INTO users (username, email, pass)
VALUES (:username, :email, :password)
