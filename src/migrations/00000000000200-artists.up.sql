CREATE TABLE artists
(artist_id     SERIAL       NOT NULL,
 name          VARCHAR(255) NOT NULL,
 create_date   TIMESTAMP    NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 update_date   TIMESTAMP    NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 CONSTRAINT pk_artists PRIMARY KEY (artist_id),
 CONSTRAINT artist_name UNIQUE(name));
--;;
-- create an update trigger which updates our update_date column by calling the above function
CREATE TRIGGER update_artist_update_date BEFORE UPDATE
ON artists FOR EACH ROW EXECUTE PROCEDURE
update_update_date();
--;;
INSERT INTO artists (name) VALUES ('The Arthur Digby Sellers Band')
--;;
INSERT INTO artists (name) VALUES ('Fort Knox Harrington')
--;;
INSERT INTO artists (name) VALUES ('Hungus')
--;;
INSERT INTO artists (name) VALUES ('Smokey Fouler')
--;;
INSERT INTO artists (name) VALUES ('Brant')
