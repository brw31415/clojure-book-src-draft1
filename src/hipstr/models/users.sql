-- name: insert-user<!
-- Inserts a new user into the Users table
-- Expects :username, :email, and :password
INSERT INTO users (username, email, pass)
VALUES (:username, :email, :password)

-- name: get-user-by-username
-- Fetches a user from the DB based on username.
SELECT *
FROM users
WHERE username=:username
