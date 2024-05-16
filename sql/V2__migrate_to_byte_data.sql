ALTER TABLE entries ADD COLUMN content_temp BYTEA;
UPDATE entries SET content_temp = content::BYTEA;
ALTER TABLE entries DROP COLUMN content;
ALTER TABLE entries RENAME COLUMN content_temp TO content;