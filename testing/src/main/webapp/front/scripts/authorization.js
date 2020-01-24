// loads a page via ajax
function loadPage(link) {
	let request = new XMLHttpRequest();
	request.open('GET', link);
	request.send();

	request.onload = function() {
		document.getElementById('main_div').innerHTML = request.response;
	}
}

// sends a request for registration
function signUp() {
	if (isValid(true)) {
		let login = document.getElementById('login').value;
		let email = document.getElementById('email').value;
		let password = document.getElementById('password').value;
		let name = document.getElementById('name').value;
		let surname = document.getElementById('surname').value;

		let authorizator = new Authorizator(login, email, password, 'student', 
											name, surname, 'http://localhost:8080/testing-1.0/authorization');
		authorizator.doRequest('signUp').then(
			result => {
				if (result.status == 'success') {
					sessionStorage.setItem('userId', result.content.id);
					sessionStorage.setItem('userRole', 'student');
					sessionStorage.setItem('userName', name);
					sessionStorage.setItem('userSurname', surname);
					document.location.href = 'testing.html';
				}
			},
			error => alert(JSON.stringify(error))
		);
	}
}

// sends a request for authorization
function signIn() {
	if (isValid(false)) {
		let login = document.getElementById('login').value;
		let password = document.getElementById('password').value;

		let authorizator = new Authorizator(login, 'none', password, 
											'none', 'none', 'none', 'http://localhost:8080/testing-1.0/authorization');
		authorizator.doRequest('signIn').then(
			result => {
				if (result.status == 'success') {
					sessionStorage.setItem('userId', result.content.id);
					sessionStorage.setItem('userRole', result.content.role);
					sessionStorage.setItem('userName', result.content.name);
					sessionStorage.setItem('userSurname', result.content.surname);
					document.location.href = 'testing.html';
				}
			},
			error => alert(JSON.stringify(error))
		);
	}
}

// changes windows foe reigistration/authorization
function chooseAction() {
	let action = document.getElementById('choosing_field').innerText;
	if (action == 'signIn') {
		loadPage('additionals/signIn.html');
		document.getElementById('choosing_field').innerText = 'signUp';
	}
	else if (action == 'signUp') {
		loadPage('additionals/signUp.html');
		document.getElementById('choosing_field').innerText = 'signIn';
	}
}

// finds out whether fields are filled correctly
function isValid(registration) {	
	let validationStatus = true;
	let validator = new Validator();
	document.getElementById('login_status').innerText = '';
	if (!validator.validate(document.getElementById('login').value, 'login')) {
		document.getElementById('login_status').innerText = 'login is not valid';
		validationStatus = false;
	}
	if (registration == true) {
		document.getElementById('email_status').innerText = '';
		if (!validator.validate(document.getElementById('email').value, 'email')) {
			document.getElementById('email_status').innerText = 'email is not valid';
			validationStatus = false;
		}
		document.getElementById('name_status').innerText = '';
		if (!validator.validate(document.getElementById('name').value, 'name')) {
			document.getElementById('name_status').innerText = 'name is not valid';
			validationStatus = false;
		}
		document.getElementById('surname_status').innerText = '';
		if (!validator.validate(document.getElementById('surname').value, 'name')) {
			document.getElementById('surname_status').innerText = 'surname is not valid';
			validationStatus = false;
		}
	}
	document.getElementById('password_status').innerText = '';
	if (!validator.validate(document.getElementById('password').value, 'password')) {
		document.getElementById('password_status').innerText = 'password is not valid';
		validationStatus = false;
	}	
	return validationStatus;
}