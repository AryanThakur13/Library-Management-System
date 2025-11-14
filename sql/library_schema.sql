
-- Database: library_db
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Users table (admin)
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);

-- Books
CREATE TABLE IF NOT EXISTS books (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255),
  publisher VARCHAR(255),
  year INT,
  isbn VARCHAR(50),
  total_qty INT DEFAULT 1,
  available_qty INT DEFAULT 1
);

-- Students
CREATE TABLE IF NOT EXISTS students (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(100),
  contact VARCHAR(20),
  roll_no VARCHAR(50) UNIQUE
);

-- Issue records
CREATE TABLE IF NOT EXISTS issues (
  id INT AUTO_INCREMENT PRIMARY KEY,
  book_id INT NOT NULL,
  student_id INT NOT NULL,
  issue_date DATE NOT NULL,
  due_date DATE NOT NULL,
  return_date DATE NULL,
  fine DECIMAL(10,2) DEFAULT 0,
  status ENUM('ISSUED','RETURNED') DEFAULT 'ISSUED',
  FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
  FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- Seed admin user (password 'admin' — change after first run).
INSERT INTO users (username, password) VALUES ('admin', 'admin')
ON DUPLICATE KEY UPDATE username=username;
