DROP TABLE IF EXISTS PARTICIPATE;
DROP TABLE IF EXISTS SESSIONS;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS TEACHERS;

CREATE TABLE TEACHERS (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  last_name VARCHAR(40),
  first_name VARCHAR(40),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE SESSIONS (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  description VARCHAR(2000),
  date TIMESTAMP,
  teacher_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (teacher_id) REFERENCES TEACHERS (id)
);

CREATE TABLE USERS (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  last_name VARCHAR(40),
  first_name VARCHAR(40),
  admin BOOLEAN NOT NULL DEFAULT false,
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PARTICIPATE (
  user_id BIGINT,
  session_id BIGINT,
  PRIMARY KEY (user_id, session_id),
  FOREIGN KEY (user_id) REFERENCES USERS (id),
  FOREIGN KEY (session_id) REFERENCES SESSIONS (id)
);
