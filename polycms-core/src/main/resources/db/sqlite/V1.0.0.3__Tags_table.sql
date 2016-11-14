CREATE TABLE tags (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  _id TEXT,
  label VARCHAR(255),
  count INTEGER,
  data JSON
);

CREATE INDEX tags_id_idx ON tags (_id);