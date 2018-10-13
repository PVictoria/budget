CREATE SCHEMA budget
  AUTHORIZATION postgres;

CREATE TABLE budget.budget.users (
  id       SERIAL PRIMARY KEY,
  name     VARCHAR(50),
  password VARCHAR(50)
);


CREATE TABLE budget.budget.articles (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(50)
);

CREATE TABLE budget.budget.balance (
  id          SERIAL PRIMARY KEY,
  create_date TIMESTAMP(3),
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
  create_date TIMESTAMP(3),
  balance_id  SERIAL,
  constraint fk_op_balance foreign key (balance_id) references budget.balance (id),
  user_id     SERIAL,
  constraint fk_op_users foreign key (user_id) references budget.users (id)
);

