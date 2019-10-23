DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id) VALUES
  ('2019-10-18 10:00', 'Завтрак', 800, 100000),
  ('2019-10-18 17:00', 'Ужин', 700, 100000),
  ('2019-10-18 11:00', 'Завтрак', 900, 100001),
  ('2019-10-18 13:00', 'Обед', 900, 100000),
  ('2019-10-18 17:30', 'Ужин', 1000, 100001);