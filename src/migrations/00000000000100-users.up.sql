CREATE TABLE users
(user_id     SERIAL      NOT NULL PRIMARY KEY,
 username    VARCHAR(30) NOT NULL,
 email       VARCHAR(60),
 pass        VARCHAR(100),
 create_date TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 update_date TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 CONSTRAINT username_idx UNIQUE(username));
--;;
-- create a function which simply sets the update_date column to the current date/time.
CREATE OR REPLACE FUNCTION update_update_date()
RETURNS TRIGGER AS $$
BEGIN
  NEW.update_date = now() AT TIME ZONE 'utc';
  RETURN NEW;
END
$$ language 'plpgsql';
--;;
-- create an update trigger which updates our update_date column by calling the above function
CREATE TRIGGER update_user_update_date BEFORE UPDATE
ON users FOR EACH ROW EXECUTE PROCEDURE
update_update_date();
