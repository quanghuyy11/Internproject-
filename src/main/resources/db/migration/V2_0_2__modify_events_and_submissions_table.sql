ALTER TABLE events ADD is_deleted BOOLEAN DEFAULT false;
ALTER TABLE submissions ADD is_deleted BOOLEAN DEFAULT false;