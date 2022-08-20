CREATE TABLE currencies
(
    id          bigint IDENTITY (1, 1) NOT NULL,
    date        date,
    name        varchar(255),
    description varchar(255),
    CONSTRAINT pk_currencies PRIMARY KEY (id)
)
    GO

CREATE TABLE currency_types
(
    id                         bigint IDENTITY (1, 1) NOT NULL,
    type                       varchar(255),
    code                       varchar(255),
    nominal                    varchar(255),
    name                       varchar(255),
    value                      decimal(18, 0),
    currency_conversion_holder bigint NOT NULL,
    CONSTRAINT pk_currency_types PRIMARY KEY (id)
)
    GO

CREATE TABLE tokens
(
    id          bigint IDENTITY (1, 1) NOT NULL,
    username    varchar(255),
    token_value varchar(255),
    CONSTRAINT pk_tokens PRIMARY KEY (id)
)
    GO

CREATE TABLE users
(
    id       bigint IDENTITY (1, 1) NOT NULL,
    username varchar(255),
    password varchar(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
)
    GO

ALTER TABLE currency_types
    ADD CONSTRAINT FK_CURRENCY_TYPES_ON_CURRENCY_CONVERSION_HOLDER FOREIGN KEY (currency_conversion_holder) REFERENCES currencies (id)
    GO
