CREATE TABLE users(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_activated BOOLEAN NOT NULL
);

CREATE TABLE post(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    url_image VARCHAR(256),
    description VARCHAR(256),
    likes INT NOT NULL,
    time_recorded TIMESTAMP NOT NULL,
    is_comments_blocked BOOLEAN NOT NULL
);