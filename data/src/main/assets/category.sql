CREATE TABLE IF NOT EXISTS categories(uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, type TEXT NOT NULL, icon TEXT NOT NULL, isNeed INTEGER DEFAULT NULL, budgets TEXT DEFAULT NULL);

-- Transfer
INSERT INTO categories (name, type, icon) VALUES('Transfer', 'TRANSFER', 'TRANSFER');

-- Expense
INSERT INTO categories (name, type, icon, isNeed) VALUES ('Rent', 'EXPENSE', 'HOME', 1);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Utilities', 'EXPENSE', 'POWER', 1);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Groceries', 'EXPENSE', 'GROCERY', 1);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Transportation', 'EXPENSE', 'TRANSPORT', 1);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Healthcare', 'EXPENSE', 'MEDICAL', 1);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Education', 'EXPENSE', 'EDUCATION', 1);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Personal Care', 'EXPENSE', 'SPA', 1);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Dining out', 'EXPENSE', 'RESTAURANT', 0);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Entertainment', 'EXPENSE', 'MOVIE', 0);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Hobbies', 'EXPENSE', 'SPORTS', 0);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Shopping', 'EXPENSE', 'SHOPPING', 0);

INSERT INTO categories (name, type, icon, isNeed) VALUES ('Subscriptions', 'EXPENSE', 'SUBSCRIPTIONS', 0);

-- Income
INSERT INTO categories (name, type, icon) VALUES ('Income', 'INCOME', 'BILLS');

COMMIT;