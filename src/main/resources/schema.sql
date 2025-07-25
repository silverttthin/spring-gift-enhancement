DROP TABLE IF EXISTS wish_list;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   email VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   nickname VARCHAR(100) NOT NULL unique
);

CREATE TABLE item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  image_url VARCHAR(1000) NOT NULL,
  user_id BIGINT NOT NULL,
  CONSTRAINT fk_item_user
      FOREIGN KEY (user_id)
          REFERENCES users(id)
          ON DELETE RESTRICT
          ON UPDATE CASCADE
);

CREATE TABLE wish_list (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  amount INT NOT NULL DEFAULT 1,
  UNIQUE (user_id, item_id)
);
