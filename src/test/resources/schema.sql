CREATE TABLE customer
(
    id             UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    email          VARCHAR(50)  NOT NULL,
    username       VARCHAR(50)  NOT NULL,
    password       VARCHAR(50)  NOT NULL,
    first_name     VARCHAR(50)  NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    phone_number   VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS product
(
    id               UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    price            int          NOT NULL,
    article          VARCHAR(50)  NOT NULL UNIQUE,
    description      text,
    available_amount int          not null
);
