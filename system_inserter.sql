use testing;

insert into roles(roleName) 
	values('student'),
		  ('tutor');
          
insert into subjects(subjectName)
	values('history'),
		  ('programming');
          
insert into users(userLogin, userPassword, userMail, userName, userSecondName, userSurname, roleId)
	values('SomeLogin', '67876566fF', 'someMail@list.ru', 'Ivan', 'Ivanovich', 'Ivanov', 1),
		  ('SomeLogin2', '67876566fF', 'someMail2@list.ru', 'Igor', 'Ilich', 'Igorni', 2);
          
insert into tests(testTitle, userId, subjectId)
	values('history test', 2, 1);
    
insert into marks(mark, testId, userId) 
	values(8, 1, 1);
    
insert into questions(questionText, testId)
		values('first', 1),
			  ('second', 1);
              
insert into answers(answerText, answerRight, questionId)
				values('answ1', true, 1),
					  ('answ2', false, 1),
                      ('answ3', false, 1),
                      ('answ1', true, 2),
                      ('answ2', true, 2),
                      ('answ3', false, 2);
    
    
    