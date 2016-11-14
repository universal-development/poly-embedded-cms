CREATE TABLE categories (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  _id TEXT,
  label VARCHAR(255),
  count INTEGER,
  data JSON
);

CREATE INDEX categories_id_idx ON categories (_id);