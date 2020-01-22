use testing;

DELIMITER //
create procedure getTests()
begin
	select testId, testTitle, subjectName, userName, userSurname from tests
    inner join subjects
    on tests.subjectId = subjects.subjectId
    inner join users
    on tests.userId = users.userId;
end //

DELIMITER //
create procedure getTestsByTutor(in tutorLogin varchar(20))
begin
	select testId, testTitle, subjectName, userName, userSurname from tests
    inner join subjects
    on tests.subjectId = subjects.subjectId
    inner join users
    on tests.userId = users.userId
    where userLogin like tutorLogin;
end //

DELIMITER //
create procedure getTestById(in testId int)
begin
	select tests.testId, testTitle, subjectName, userName, userSurname from tests
		inner join subjects on tests.subjectId = subjects.subjectId
		inner join users on tests.userId = users.userId
		where tests.testId = testId;
end //

DELIMITER //
create procedure insertTest(in testTitle varchar(50), subjectId int, userId int)
begin
	insert into tests(testTitle, subjectId, userId)
		values(testTitle, subjectId, userId);
	select last_insert_id();
end //

DELIMITER //
create procedure insertQuestion(in questionText varchar(100), testId int)
begin
	insert into questions(questionText, testId)
		values(questionText, testId);
	select last_insert_id();
end //

DELIMITER //
create procedure insertAnswer(in answerText varchar(50), answerRight bool, questionId int)
begin
	insert into answers(answerText, answerRight, questionId)
		values(answerText, answerRight, questionId);
	select last_insert_id();
end //

DELIMITER //
create procedure updateTest(in testId int, testTitle varchar(50), subjectId int, userId int)
begin
	update tests
		set tests.testTitle = testTitle,
            tests.subjectId = subjectId,
            tests.userId = userId
		where tests.testId = testId;
end //

DELIMITER //
create procedure deleteTest(in testId int)
begin
	delete from tests where tests.testId = testId;
end //

DELIMITER //
create procedure getUser(in userLogin varchar(20), userPassword varchar(20))
begin
	select userId, userName, userSecondName, userSurname, roleName, users.roleId from users
		inner join roles on users.roleId = roles.roleId
        where users.userLogin like userLogin and users.userPassword like userPassword; 
end //

DELIMITER //
create procedure checkLogin(in userLogin varchar(20))
begin
	select count(*) from users where users.userLogin like userLogin;
end //

DELIMITER //
create procedure checkMail(in userMail varchar(30))
begin
	select count(*) from users where users.userMail like userMail;
end //

DELIMITER //
create procedure insertUser(in userLogin VARCHAR(20), userPassword VARCHAR(20),
							   userMail VARCHAR(30), userName VARCHAR(10),
                               userSecondName VARCHAR(10), userSurname VARCHAR(20),
							   roleId INT)
begin
	insert into users(userLogin, userPassword, userMail, userName, userSecondName, userSurname, roleId)
				values(userLogin, userPassword, userMail, userName, userSecondName, userSurname, roleId);
	select last_insert_id();
end //

DELIMITER //
create procedure deleteUser(in userId int)
begin
	delete from users where users.userId = userId;
end //

DELIMITER //
create procedure getQuestions(in testId int)
begin
	select * from questions where questions.testId = testId;
end //

DELIMITER //
create procedure getAnswers(in questionId int)
begin
	select * from answers where answers.questionId = questionId;
end //

DELIMITER //
create procedure deleteQuestion(in questionId int)
begin
	delete from questions where questions.questionId = questionId;
end //

DELIMITER //
create procedure deleteAnswer(in answerId int)
begin
	delete from answers where answers.answerId = answerId;
end //

DELIMITER //
create procedure updateQuestion(in questionId int, questionText varchar(100))
begin
	update questions
		set questions.questionText = questionText
		where questions.questionId = questionId;
end //

DELIMITER //
create procedure updateAnswer(in answerId int, answerText varchar(50), answerRight bool)
begin
	update answers
		set answers.answerText = answerText,
			answers.answerRight = answerRight
		where answers.answerId = answerId;
end //

DELIMITER //
create procedure getSubject(in subjectName varchar(15))
begin
	select * from subjects where subjects.subjectName like subjectName; 
end //

DELIMITER //
create procedure insertSubject(in subjectName varchar(15))
begin
	insert into subjects(subjectName) values(subjectName);
    select last_insert_id();
end //

DELIMITER //
create procedure getMarksByUser(in userId int)
begin
	select markId, mark, testTitle, userLogin from marks
		inner join tests on marks.testId = tests.testId
        inner join users on marks.userId = users.userId
    where marks.userId = userId;
end //

DELIMITER //
create procedure getMarksByTest(in testId int)
begin
	select markId, mark, testTitle, userLogin from marks
		inner join tests on marks.testId = tests.testId
        inner join users on marks.userId = users.userId
    where marks.testId = testId;
end //

