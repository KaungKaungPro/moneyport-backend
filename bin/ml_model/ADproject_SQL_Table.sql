-- Create Database

USE moneyport;

-- Create Tables

-- Table for storing questions
CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(255) NOT NULL
);

-- Table for storing answer options
CREATE TABLE answer_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT,
    option_text VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- Table for storing user responses
CREATE TABLE responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT,
    user_id INT,
    response_text VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- Insert Questions
INSERT INTO questions (question_text) VALUES 
('What is your primary investment goal?'),
('How long do you plan to hold your investments?'),
('How would you describe your risk tolerance when it comes to investing?'),
('What is your annual income range?'),
('How would you rate your investment experience?'),
('If your investment suddenly dropped 20% in value, how would you react?'),
('What is your age group?');

select * from questions;
select * from questions;
select * from answer_options;

-- Insert Options for Question 1
INSERT INTO answer_options (question_id, option_text) VALUES 
(1, 'Preserve capital'),
(1, 'Generate income'),
(1, 'Balanced growth and income'),
(1, 'Maximize long-term growth');

-- Insert Options for Question 2
INSERT INTO answer_options (question_id, option_text) VALUES 
(2, 'Less than 1 year'),
(2, '1-3 years'),
(2, '3-5 years'),
(2, 'More than 5 years');

-- Insert Options for Question 3
INSERT INTO answer_options (question_id, option_text) VALUES 
(3, 'Very low- I am not comfortable with any risk'),
(3, 'Low- I prefer safer investments, even if returns are lower'),
(3, 'Moderate-I am willing to accept some risk for potentially higher return'),
(3, 'High- I am comfortable with significant risk for the potential of high returns');

-- Insert Options for Question 4
INSERT INTO answer_options (question_id, option_text) VALUES 
(4, 'Less than $30,000'),
(4, '$30000-$60000'),
(4, '$60000-$100000'),
(4, 'Greater than $100000');

-- Insert Options for Question 5
INSERT INTO answer_options (question_id, option_text) VALUES 
(5, 'No experience'),
(5, 'Some experience with basic investments like saving account'),
(5, 'Moderate experience with stocks, bonds and mutual funds'),
(5, 'Extensive experience with various investment types');

-- Insert Options for Question 6
INSERT INTO answer_options (question_id, option_text) VALUES 
(6, 'Sell all my investments immediately'),
(6, 'Sell some of my investments'),
(6, 'Hold onto my investments and wait for recovery'),
(6, 'Buy more investments at the lower price');

-- Insert Options for Question 7
INSERT INTO answer_options (question_id, option_text) VALUES 
(7, 'Over 60'),
(7, '45-60'),
(7, '30-45'),
(7, 'Under 30');

SELECT * FROM responses;
CREATE TABLE stocks (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    risk_level ENUM('R1', 'R2', 'R3') NOT NULL,
    sector VARCHAR(50),
    UNIQUE (symbol)
);

select *from stocks;

-- CREATE TABLE users (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(255) NOT NULL UNIQUE,
--     email VARCHAR(255) NOT NULL UNIQUE,
--     password_hash VARCHAR(255) NOT NULL
-- );

DROP TABLE IF EXISTS responses;

DROP TABLE IF EXISTS responses;
CREATE TABLE responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    response_value INT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
select * from responses;

delete from responses where id <> 0;

drop table responses;
-- INSERT INTO users (username, email, password_hash) 
-- VALUES ('testuser', 'testuser@example.com', 'hashedpassword123');
-- select*from users;

