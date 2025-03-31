BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS budgets (category_id INTEGER NOT NULL, year_month TEXT NOT NULL, amount TEXT NOT NULL DEFAULT "0", PRIMARY KEY (category_id, year_month));

INSERT INTO budgets (category_id, year_month, amount) VALUES (2, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (3, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (4, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (5, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (6, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (7, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (8, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (9, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (10, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (11, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (12, strftime('%m %Y', current_date), '0');
INSERT INTO budgets (category_id, year_month, amount) VALUES (13, strftime('%m %Y', current_date), '0');

COMMIT;
