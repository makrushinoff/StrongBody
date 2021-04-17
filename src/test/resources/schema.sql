CREATE TABLE authorization_data (
    id UUID PRIMARY KEY ,
    email VARCHAR(50),
    username VARCHAR(50),
    password VARCHAR(50),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(50)
);
CREATE TABLE customer (
    id UUID PRIMARY KEY ,
    authorization_id UUID ,
    FOREIGN KEY (authorization_id) REFERENCES authorization_data(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY ,
    name VARCHAR(100) NOT NULL,
    price int NOT NULL,
    article VARCHAR(50) NOT NULL UNIQUE,
    description text,
    available_amount int not null
);