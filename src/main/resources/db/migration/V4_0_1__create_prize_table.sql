-- CREATE TABLE PRIZES
CREATE TABLE prizes (
    id SERIAL PRIMARY KEY ,
    prize_name VARCHAR(255) CONSTRAINT prize_prize_name_not_null NOT NULL,
    description TEXT CONSTRAINT prize_prize_name_not_null NOT NULL,
    point_archive INTEGER CONSTRAINT prize_point_archive_not_null  NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_deleted BOOLEAN DEFAULT false
)

