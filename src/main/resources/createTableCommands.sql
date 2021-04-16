CREATE TABLE IF NOT EXISTS authorization_data (
    id UUID NOT NULL UNIQUE PRIMARY KEY ,
    email VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS customer (
    id UUID NOT NULL UNIQUE PRIMARY KEY ,
    FOREIGN KEY (authorization_id) REFERENCES authorization_data(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS cart(
    id UUID NOT NULL UNIQUE PRIMARY KEY ,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS product (
    id UUID NOT NULL UNIQUE PRIMARY KEY ,
    name VARCHAR(100) NOT NULL,
    price int NOT NULL,
    artice VARCHAR(50), NOT NULL UNIQUE,
    description text,
    available_amount int not null
);
CREATE TABLE IF NOT EXISTS booking(
    id UUID NOT NULL UNIQUE PRIMARY KEY ,
    order_date date NOT NULL,
    product_amount int NOT NULL,
    order_number int NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE
);