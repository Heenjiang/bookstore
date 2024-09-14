-- --------------------------------------------------------
-- Populating the 'book' table with dummy data
-- --------------------------------------------------------

INSERT INTO book (title, author, category, price, discount, publisher, publication_date)
VALUES
    ('Effective Java', 'Joshua Bloch', 'Programming', 45.00, 0, 'Addison-Wesley', '2018-01-06'),
    ('Clean Code', 'Robert C. Martin', 'Programming', 40.00, 0, 'Prentice Hall', '2008-08-11'),
    ('Spring in Action', 'Craig Walls', 'Programming', 50.00, 5, 'Manning', '2018-09-27'),
    ('The Pragmatic Programmer', 'Andrew Hunt and David Thomas', 'Programming', 42.00, 0, 'Addison-Wesley', '1999-10-20'),
    ('Design Patterns', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', 'Programming', 55.00, 0, 'Addison-Wesley', '1994-10-21'),
    ('Refactoring', 'Martin Fowler', 'Programming', 47.50, 0, 'Addison-Wesley', '2018-11-20'),
    ('Head First Design Patterns', 'Eric Freeman, Bert Bates, Kathy Sierra, Elisabeth Robson', 'Programming', 49.99, 0, "O'Reilly Media", '2004-10-25'),
    ('Test-Driven Development', 'Kent Beck', 'Programming', 39.99, 0, 'Addison-Wesley', '2002-11-08'),
    ('Introduction to Algorithms', 'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein', 'Programming', 80.00, 0, 'MIT Press', '2009-07-31'),
    ('Cracking the Coding Interview', 'Gayle Laakmann McDowell', 'Programming', 35.00, 0, 'CareerCup', '2015-07-01');

-- --------------------------------------------------------
-- Populating the 'cart' table with dummy data
-- --------------------------------------------------------

-- Create multiple carts
INSERT INTO cart DEFAULT VALUES; -- Cart ID 1
INSERT INTO cart DEFAULT VALUES; -- Cart ID 2
INSERT INTO cart DEFAULT VALUES; -- Cart ID 3

-- --------------------------------------------------------
-- Populating the 'cart_item' table with dummy data
-- --------------------------------------------------------

-- Cart 1 items
INSERT INTO cart_item (quantity, cart_id, book_id)
VALUES
    (2, 1, 1),  -- 2 copies of 'Effective Java' in cart 1
    (1, 1, 2),  -- 1 copy of 'Clean Code' in cart 1
    (1, 1, 5);  -- 1 copy of 'Design Patterns' in cart 1

-- Cart 2 items
INSERT INTO cart_item (quantity, cart_id, book_id)
VALUES
    (1, 2, 3),  -- 1 copy of 'Spring in Action' in cart 2
    (2, 2, 4),  -- 2 copies of 'The Pragmatic Programmer' in cart 2
    (1, 2, 7);  -- 1 copy of 'Head First Design Patterns' in cart 2

-- Cart 3 items
INSERT INTO cart_item (quantity, cart_id, book_id)
VALUES
    (1, 3, 6),  -- 1 copy of 'Refactoring' in cart 3
    (3, 3, 9),  -- 3 copies of 'Introduction to Algorithms' in cart 3
    (2, 3, 10); -- 2 copies of 'Cracking the Coding Interview' in cart 3
