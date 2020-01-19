CREATE DATABASE testing;

USE testing;

CREATE TABLE subjects(subjectId INT AUTO_INCREMENT PRIMARY KEY,
					  subjectName VARCHAR(15) NOT NULL);
                      
CREATE TABLE roles(roleId INT AUTO_INCREMENT PRIMARY KEY,
				   roleName VARCHAR(15) NOT NULL);
                   
CREATE TABLE users(userId INT AUTO_INCREMENT PRIMARY KEY,
				   userLogin VARCHAR(20) NOT NULL,
                   userPassword VARCHAR(20) NOT NULL,
                   userMail VARCHAR(30) NOT NULL,
				   userName VARCHAR(10) NOT NULL,
				   userSecondName VARCHAR(10) NOT NULL,
				   userSurname VARCHAR(20) NOT NULL,
				   roleId INT NOT NULL,
				   FOREIGN KEY(roleId) REFERENCES roles(roleId) ON DELETE CASCADE);
                   
CREATE TABLE tests(testId INT AUTO_INCREMENT PRIMARY KEY,
				   testTitle VARCHAR(50) NOT NULL,
                   userId INT NOT NULL,
				   subjectId INT NOT NULL,
                   FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE,
				   FOREIGN KEY(subjectId) REFERENCES subjects(subjectId) ON DELETE CASCADE);
                   
CREATE TABLE marks(markId INT AUTO_INCREMENT PRIMARY KEY,
				   mark INT NOT NULL,
                   testId INT NOT NULL,
                   userId INT NOT NULL,
                   FOREIGN KEY(testId) REFERENCES tests(testId) ON DELETE CASCADE,
                   FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE);
                   
CREATE TABLE questions(questionId INT AUTO_INCREMENT PRIMARY KEY,
					   questionText VARCHAR(100) NOT NULL,
                       testId INT NOT NULL,
                       FOREIGN KEY(testId) REFERENCES tests(testId) ON DELETE CASCADE);
                       
CREATE TABLE answers(answerId INT AUTO_INCREMENT PRIMARY KEY,
					  answerText VARCHAR(50) NOT NULL,
                      answerRight BOOLEAN NOT NULL,
                      questionId INT NOT NULL,
                      FOREIGN KEY(questionId) REFERENCES questions(questionId) ON DELETE CASCADE);	