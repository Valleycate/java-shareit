CREATE TABLE IF NOT EXISTS users (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(300) NOT NULL,
  email VARCHAR(300) NOT NULL,
  UNIQUE (email),
  UNIQUE (ID)
);
CREATE TABLE IF NOT EXISTS requests (
  id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  description varchar(300) NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  requester_id BIGINT,
  FOREIGN KEY(requester_id) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS items (
 id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 description varchar(300) NOT NULL,
 available varchar(5) NOT NULL,
 name varchar(100) NOT NULL,
 owner_id BIGINT,
 FOREIGN KEY(owner_id) REFERENCES users(id),
 request_id BIGINT,
 FOREIGN KEY(request_id) REFERENCES requests(id)
 );

CREATE TABLE IF NOT EXISTS booking(
 id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 item_id BIGINT,
 status varchar(8) NOT NULL,
 FOREIGN KEY(item_id) REFERENCES items(id),
 booker_id BIGINT,
 FOREIGN KEY(booker_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS comments(
 id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 text varchar(300) NOT NULL,
 created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 item_id BIGINT,
 FOREIGN KEY(item_id) REFERENCES items(id),
 author_id BIGINT,
 FOREIGN KEY(author_id) REFERENCES users(id)
);