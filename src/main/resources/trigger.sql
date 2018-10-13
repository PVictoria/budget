
CREATE OR REPLACE FUNCTION budget.on_delete_balance()
  RETURNS TRIGGER AS $$
BEGIN
  update budget.operations set balance_id = null where balance_id = old.id;
  RETURN old;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER on_delete_balance
  before DELETE
  ON budget.balance
  FOR EACH ROW
EXECUTE PROCEDURE budget.on_delete_balance();


CREATE OR REPLACE FUNCTION budget.on_delete_operation()
  RETURNS TRIGGER AS $$
BEGIN
  if old.balance_id is not null
  then
    raise exception 'You have a pointer on this operation!';
  end if;
  RETURN old;
END;
$$
LANGUAGE plpgsql;


CREATE TRIGGER on_delete_operation
  before DELETE
  ON budget.operations
  FOR EACH ROW EXECUTE PROCEDURE budget.on_delete_operation();


CREATE OR REPLACE FUNCTION budget.on_delete_article()
  RETURNS TRIGGER AS $$
DECLARE
  counter NUMERIC;
BEGIN
  SELECT COUNT(*) INTO counter FROM budget.operations op WHERE op.article_id = old.id;
  if counter > 0
  then
    raise exception 'You have a pointer on this operation!';
  end if;
  RETURN new;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER on_delete_article
  before DELETE
  ON budget.articles
  FOR EACH ROW EXECUTE PROCEDURE budget.on_delete_article();


CREATE OR REPLACE FUNCTION budget.insert_null_operation()
  RETURNS TRIGGER AS $$
BEGIN
  if (new.debit = 0 and new.credit = 0)
  then
    RAISE EXCEPTION 'Zero in credit and debit!';
  end if;
  RETURN new;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_null_operation
  BEFORE INSERT
  ON budget.operations
  FOR EACH ROW EXECUTE PROCEDURE budget.insert_null_operation();


CREATE OR REPLACE FUNCTION budget.deny_update_operations()
  RETURNS TRIGGER AS $$
BEGIN
  if (new.balance_id is not null)
  then
    RAISE EXCEPTION 'Operation in balance cannot be updated!';
  end if;
  RETURN new;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER deny_update_operations
  BEFORE UPDATE
  ON budget.operations
  FOR EACH ROW EXECUTE PROCEDURE budget.deny_update_operations();


CREATE OR REPLACE FUNCTION budget.deny_delete_operations()
  RETURNS TRIGGER AS $$
BEGIN
  if (old.balance_id is not null)
  then
    RAISE EXCEPTION 'Operation is included in balance!';
  end if;
  RETURN new;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER deny_delete_operations
  BEFORE UPDATE
  ON budget.operations
  FOR EACH ROW EXECUTE PROCEDURE budget.deny_delete_operations();


CREATE OR REPLACE FUNCTION budget.last_day(DATE)
  RETURNS DATE AS
$$
SELECT (date_trunc('MONTH', $1) + INTERVAL '1 MONTH - 1 day') :: DATE;
$$
LANGUAGE 'sql'
IMMUTABLE
STRICT;

CREATE OR REPLACE FUNCTION budget.deny_insert_operation()
  RETURNS TRIGGER AS $$
DECLARE
  b_date  DATE;
  counter NUMERIC;
  userId  INTEGER;
BEGIN
  userId = tg_argv [0] :: INTEGER;
  b_date = budget.last_day(new.create_date);
  select count(id) into counter from budget.balance where (create_date = b_date AND balance.user_id = userId);
  if (counter > 0)
  then
    RAISE EXCEPTION 'Balance for this date is closed!';
  end if;
  RETURN new;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER deny_insert_operation
  BEFORE UPDATE
  ON budget.operations
  FOR EACH ROW EXECUTE PROCEDURE budget.deny_insert_operation(user_id);


CREATE OR REPLACE FUNCTION budget.wrong_operation_in_balance()
  RETURNS TRIGGER AS $$
DECLARE
  max_date Date;
  userId   INTEGER;
BEGIN
  userId = tg_argv [0] :: integer;
  select max(b.create_date) into max_date from budget.balance b WHERE b.user_id = userId;
  if (new.create_date < max_date)
  then
    RAISE EXCEPTION 'Balance date is more than balance date!';
  end if;
  RETURN new;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER wrong_operation_in_balance
  BEFORE INSERT or UPDATE
  ON budget.balance
  FOR EACH ROW EXECUTE PROCEDURE budget.wrong_operation_in_balance(user_id);


CREATE OR REPLACE FUNCTION budget.insert_existing_articles()
  RETURNS TRIGGER AS $$
DECLARE
  ROW_COUNT NUMERIC;
BEGIN
  select count(*)into row_count from budget.articles where articles.name = new.name;
  if (row_count > 0)
  then
    RAISE EXCEPTION 'This article already exists, please enter a different name!';
  end if;
  RETURN new;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_existing_article
  BEFORE INSERT
  ON budget.articles
  FOR EACH ROW EXECUTE PROCEDURE budget.insert_existing_articles();


CREATE OR REPLACE FUNCTION budget.second_balance()
  RETURNS TRIGGER AS $$
DECLARE
  counter NUMERIC;
  userId  INTEGER;
BEGIN
  userId = tg_argv [0] :: INTEGER;
  SELECT COUNT(*) INTO counter from balance b WHERE b.create_date = new.create_date;
  IF counter > 0
  THEN
    RAISE EXCEPTION 'Balance already exists!';
  END IF;
  RETURN new;
END;
$$
LANGUAGE plpgsql;


CREATE TRIGGER second_balance
  before INSERT
  on budget.balance
  FOR EACH ROW EXECUTE PROCEDURE budget.second_balance(user_id);



