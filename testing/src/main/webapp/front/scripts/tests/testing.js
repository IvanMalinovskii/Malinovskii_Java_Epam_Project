
let testsJson = {
	tests: []
};

function loadPage(link, func) {
	let request = new XMLHttpRequest();
	request.open('GET', link);
	request.send();

	request.onload = function() {
		document.getElementById('central_div').innerHTML = request.response;
		func();
	}
}

function setHeader() {
	let headerString = sessionStorage.getItem('userName') + " " 
					   + sessionStorage.getItem('userSurname') + " "
					   + "role: " + sessionStorage.getItem('userRole');
	document.getElementById('description').innerText = headerString;
	document.getElementById('description').onclick = function() {document.location.href = 'testing.html'};
}

function formTable(/*testsJson = {}*/) {
	let div = document.getElementById('central_div');
	let table = document.createElement('table');
	table.setAttribute('style', 'width: 95%;')
	let headers = ['№', 'title', 'subject', 'creator'];
	let headerRow = table.insertRow(-1);
	for (let i = 0; i < headers.length; i++) {
		let th = document.createElement('th');
		th.innerHTML = headers[i];
		headerRow.append(th);
	}

	if (sessionStorage.getItem('userRole') == 'tutor') {
		let cell = headerRow.insertCell(-1);
		let creationButton = createButton('create', '#80ff80', creationButtonClick);
		cell.append(creationButton);
	}

	for (let i = 0; i < testsJson.tests.length; i++) {
		let row = table.insertRow(-1);
		let idCell = row.insertCell(-1);
		idCell.innerHTML = i + 1;
		let titleCell = row.insertCell(-1);
		titleCell.innerHTML = testsJson.tests[i].title;
		let subjectCell = row.insertCell(-1);
		subjectCell.innerHTML = testsJson.tests[i].subject;
		let nameCell = row.insertCell(-1);
		nameCell.innerHTML = testsJson.tests[i].surname + " " + testsJson.tests[i].name;
		if (sessionStorage.getItem('userRole') == 'student') {
			let passButtonCell = row.insertCell(-1);
			let passButton = createButton('pass', 'green', passButtonClick);
			passButtonCell.append(passButton);
		}
		let marksButtonCell = row.insertCell(-1);
		let marksButton = createButton('marks', 'blue', marksButtonClick);
		marksButtonCell.append(marksButton);
		row.id = testsJson.tests[i].id;
	}

	div.append(table);
}

function creationButtonClick() {
	document.getElementById('central_div').innerHTML = '';
	document.getElementById('central_div').append(new Controls().createTestTable());
	//create();
}

function passButtonClick() {
	sessionStorage.setItem('testId', this.parentNode.parentNode.id);
	loadPage('additionals/exam.html', getTest);
	//getTest();
}

function marksButtonClick() {
	sessionStorage.setItem('testId', this.parentNode.parentNode.id);
	document.getElementById('central_div').innerHTML = '';
	createMarksTable();
}


function createButton(value = '', color = '', func) {
		let button = document.createElement('input');
		button.setAttribute('type', 'button');
		button.setAttribute('value', value);
		button.setAttribute('style', 'background-color: ' + color + '; display: initial;')
		button.onclick = func;
		return button;
	}

function onload() {
	let flag = sessionStorage.getItem('userRole') == 'student' ? true : false;
	setHeader();
	getTests('http://localhost:8080/testing-1.0/test', flag);
}

function getTests(link = '', IsStudent = false) {
	let jsonObj = {
		action: IsStudent ? 'getAll' : 'getByUser',
		login: sessionStorage.getItem('userLogin')
	};
	let request = new XMLHttpRequest();
	request.open('POST', link);
	request.send(JSON.stringify(jsonObj));

	request.onload = function() {
		if (request.status == 200) {
			responseJson = JSON.parse(request.response);
			if (responseJson.status == 'success') {
				testsJson.tests = responseJson.tests;
				formTable();
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