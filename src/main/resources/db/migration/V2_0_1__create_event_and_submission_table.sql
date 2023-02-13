CREATE TABLE IF NOT EXISTS  events(
       id SERIAL PRIMARY KEY ,
       jotform_url VARCHAR(255) UNIQUE,
       title VARCHAR(255) NOT NULL,
       point INTEGER CONSTRAINT valid_point CHECK (point > 0 AND point <= 5) NOT NULL ,
       event_date DATE NOT NULL ,
       status VARCHAR(20) CONSTRAINT valid_status CHECK ( status IN ('UPCOMING', 'IN_PROGRESS', 'DONE')) NOT NULL,
       created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
       updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS submissions(
        id SERIAL PRIMARY KEY,
        user_id VARCHAR(255) REFERENCES users(email) NOT NULL,
        event_id INTEGER REFERENCES events(id) NOT NULL,
        is_participated BOOLEAN DEFAULT FALSE NOT NULL,
        created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
        updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);