-- name: get-recently-added
-- Gets the 10 most recently added albums in the db.
SELECT art.name as artist, alb.album_id, alb.name as album_name, alb.release_date, alb.create_date
FROM artists art
INNER JOIN albums alb ON art.artist_id = alb.artist_id
ORDER BY alb.create_date DESC
LIMIT 10

-- name: get-by-artist
-- Gets the discography for a given artist.
SELECT alb.album_id, alb.name, alb.release_date
FROM albums alb
INNER JOIN artists art on alb.artist_id = art.artist_id
WHERE
  art.name = :artist
ORDER BY alb.release_date DESC
