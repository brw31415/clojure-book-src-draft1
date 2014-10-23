-- name: insert-artist<!
-- Inserts a new artist into the database.
INSERT INTO artists(name)
VALUES (:artist_name)

-- name: get-artist-by-name
-- Retrieves an artist from the database by name.
SELECT *
FROM artists
WHERE name=:artist_name
