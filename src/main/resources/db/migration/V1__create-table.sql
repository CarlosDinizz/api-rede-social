CREATE TABLE users(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_activated BOOLEAN NOT NULL,
    followers_id UUID,
    followers INT,
    following INT
);

CREATE TABLE post(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    url_image VARCHAR(256),
    description VARCHAR(256),
    likes INT NOT NULL,
    time_recorded TIMESTAMP NOT NULL,
    is_comments_blocked BOOLEAN NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comment(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    post_id UUID NOT NULL,
    description VARCHAR(256) NOT NULL,
    likes INT NOT NULL,
    time_recorded TIMESTAMP NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES post(id)
);

CREATE TABLE followers(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE users
ADD FOREIGN KEY (followers_id) REFERENCES followers(id);

CREATE TABLE followers_users(
    user_id UUID,
    followers_id UUID,

    PRIMARY KEY(user_id, followers_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (followers_id) REFERENCES followers(id)
);

CREATE TABLE following(

    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,

    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE following_users(
    user_id UUID,
    following_id UUID,
    PRIMARY KEY(user_id, following_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(following_id) REFERENCES following(id)
);
