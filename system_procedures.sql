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
	select tests.testId, testTitle, subjectName, userName, userSurname, questions.questionId, questionText, answers.answerId, answerText, answerRight from tests
		inner join subjects on tests.subjectId = subjects.subjectId
		inner join users on tests.userId = users.userId
		inner join questions on tests.testId = questions.testId
		inner join answers on questions.questionId = answers.questionId
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



