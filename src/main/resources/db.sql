drop table budget.operations;
drop table budget.balance;
drop table budget.articles;
drop table budget.users;

CREATE SCHEMA budget
  AUTHORIZATION postgres;

CREATE TABLE budget.budget.users (
  id       SERIAL PRIMARY KEY,
  name     VARCHAR(50) UNIQUE ,
  password VARCHAR(50)
);


CREATE TABLE budget.budget.articles (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(50),
  constraint article_name unique (name)
);

CREATE TABLE budget.budget.balance (
  id          SERIAL PRIMARY KEY,
  create_date DATE,
  debit       NUMERIC(18, 2),
  credit      NUMERIC(18, 2),
  amount      NUMERIC(18, 2),
  user_id     SERIAL,
  constraint fk_blnc_users foreign key (user_id) references budget.users (id)

);

CREATE TAble budget.budget.operations (
  id          SERIAL primary key,
  article_id  SERIAL,
  constraint fk_op_articles foreign key (article_id) references budget.articles (id),
  debit       NUMERIC(18, 2),
  credit      NUMERIC(18, 2),
  create_date DATE,
  balance_id  INTEGER references budget.balance,
  user_id     SERIAL,
  constraint fk_op_users foreign key (user_id) references budget.users (id)
);

