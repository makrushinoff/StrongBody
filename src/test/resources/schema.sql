CREATE TABLE customer
(
    id           UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    email        VARCHAR(50) NOT NULL,
    username     VARCHAR(50) NOT NULL,
    password     VARCHAR(50) NOT NULL,
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    phone_number VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS product
(
    id               UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name             VARCHAR(100)   NOT NULL,
    price            int            NOT NULL,
    article          VARCHAR(50)    NOT NULL UNIQUE,
    description      text,
    available_amount int            NOT NULL,
    reserved_amount  int  DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS cart
(
    id          UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    customer_id UUID NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS booking
(
    id             UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    order_date     date        NOT NULL,
    product_amount int         NOT NULL,
    order_number   VARCHAR(50) NOT NULL,
    product_id     UUID        NOT NULL,
    cart_id        UUID        NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (cart_id) REFERENCES cart (id) ON DELETE CASCADE
);
