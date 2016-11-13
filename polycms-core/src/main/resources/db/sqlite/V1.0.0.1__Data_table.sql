CREATE TABLE data (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  _id TEXT,
  category VARCHAR(200),
  tags TEXT,
  data JSON
);

CREATE INDEX _id_idx ON data (_id);
CREATE INDEX _category_idx ON data (category);
CREATE INDEX _tag_idx ON data (tags);