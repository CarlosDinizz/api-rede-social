CREATE TABLE users(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(256) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN NOT NULL,
    role VARCHAR(256) NOT NULL
);

CREATE TABLE profile(
    id UUID PRIMARY KEY,
    username VARCHAR(256) UNIQUE NOT NULL,
    bio VARCHAR(256) NOT NULL,
    user_id UUID NOT NULL,

    FOREIGN KEY(user_id) REFERENCES users(id)

);

CREATE TABLE post(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    profile_id UUID NOT NULL,
    url_image VARCHAR(256),
    description VARCHAR(256),
    likes INT NOT NULL,
    time_recorded TIMESTAMP NOT NULL,
    is_comments_blocked BOOLEAN NOT NULL,

    FOREIGN KEY (profile_id) REFERENCES profile(id)
);

CREATE TABLE comment(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    profile_id UUID NOT NULL,
    post_id UUID NOT NULL,
    description VARCHAR(256) NOT NULL,
    likes INT NOT NULL,
    time_recorded TIMESTAMP NOT NULL,

    FOREIGN KEY (profile_id) REFERENCES profile(id),
    FOREIGN KEY (post_id) REFERENCES post(id)
);

CREATE TABLE followers(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    profile_id UUID NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES profile(id)
);


CREATE TABLE followers_profile(
    profile_id UUID,
    followers_id UUID,

    PRIMARY KEY(profile_id, followers_id),
    FOREIGN KEY (profile_id) REFERENCES profile(id),
    FOREIGN KEY (followers_id) REFERENCES followers(id)
);

CREATE TABLE following(

    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    profile_id UUID NOT NULL,

    FOREIGN KEY(profile_id) REFERENCES profile(id)
);

CREATE TABLE following_profile(
    profile_id UUID,
    following_id UUID,
    PRIMARY KEY(profile_id, following_id),
    FOREIGN KEY(profile_id) REFERENCES profile(id),
    FOREIGN KEY(following_id) REFERENCES following(id)
);



