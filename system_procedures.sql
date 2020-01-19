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
