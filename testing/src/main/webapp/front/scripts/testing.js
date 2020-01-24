
var testsJson = {
	tests: []
}

function setHeader() {
	let headerString = sessionStorage.getItem('userName') + " " 
					   + sessionStorage.getItem('userSurname') + " "
					   + "role: " + sessionStorage.getItem('userRole');
	document.getElementById('description').innerText = headerString;					   
}

function formTable(/*testsJson = {}*/) {
	for(let index = 0; index < testsJson.tests.length; index++) {
		let table = document.getElementById('tests_table');
		let rowsCount = table.rows.length;
		let row = table.insertRow(rowsCount);
		row.insertCell(0).innerHTML = rowsCount;
		row.insertCell(1).innerHTML = testsJson.tests[index].title;
		row.insertCell(2).innerHTML = testsJson.tests[index].subject;
		row.insertCell(3).innerHTML = testsJson.tests[index].name;
		row.insertCell(4).innerHTML = testsJson.tests[index].surname;
		row.onclick = function() {
			sessionStorage.setItem('testId', Number(testsJson.tests[index].id));
		};
	}
}

function formButtons(/*testsJson = {}*/) {
	for(let index = 0; index < testsJson.tests.length; index++) {
		let table = document.getElementById('buttons_table');
		let rowsCount = table.rows.length;
		let row = table.insertRow(rowsCount);
		row.insertCell(0).innerHTML = 'marks';
		row.cells[0].onclick = function() {
			alert('right cell');
		}
	}
}

function onload() {
	let flag = sessionStorage.getItem('userRole') == 'student' ? true : false;
	setHeader();
	getTests('http://localhost:8080/testing-1.0/test', flag);
}

// to do: add a request for getting tests 

function getTests(link = '', IsStudent = false) {
	let jsonObj = {
		action: IsStudent ? 'getAll' : 'getByUser',
		id: sessionStorage.getItem('userId')
	}

	let request = new XMLHttpRequest();
	request.open('POST', link);
	request.send(JSON.stringify(jsonObj));

	request.onload = function() {
		if (request.status == 200) {
			responseJson = JSON.parse(request.response);
			if (responseJson.status == 'success') {
				testsJson.tests = responseJson.tests;
				formTable();
				formButtons();
			}
			else {
				alert(responseJson.cause);
			}
		}
		else {
			alert('request error');
		}
	}

	request.onerror = function() {
		alert('server error');
	}
}