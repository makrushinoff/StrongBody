CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS customer
(
    id           UUID NOT NULL DEFAULT uuid_generate_v4(),
    email        TEXT NOT NULL UNIQUE,
    username     TEXT NOT NULL UNIQUE,
    password     TEXT NOT NULL,
    first_name   TEXT NOT NULL,
    last_name    TEXT NOT NULL,
    phone_number TEXT NOT NULL UNIQUE,

    CONSTRAINT "customer_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cart
(
    id          UUID NOT NULL DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL,

    CONSTRAINT "cart_pkey" PRIMARY KEY (id),
    CONSTRAINT "customer_id_fkey" FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS product
(
    id               UUID    NOT NULL DEFAULT uuid_generate_v4(),
    name             TEXT    NOT NULL,
    price            NUMERIC NOT NULL,
    article          TEXT    NOT NULL UNIQUE,
    description      TEXT,
    available_amount INTEGER not null,

    CONSTRAINT "product_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS booking
(
    id             UUID    NOT NULL DEFAULT uuid_generate_v4(),
    order_date     DATE    NOT NULL,
    product_amount INTEGER NOT NULL,
    order_number   TEXT    NOT NULL,
    product_id     UUID    NOT NULL,
    cart_id        UUID    NOT NULL,

    CONSTRAINT "booking_id" PRIMARY KEY (id),
    CONSTRAINT "product_id_fkey" FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    CONSTRAINT "cart_id_fkey" FOREIGN KEY (cart_id) REFERENCES cart (id) ON DELETE CASCADE
);
