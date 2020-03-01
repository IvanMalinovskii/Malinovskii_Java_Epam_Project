let marks = [];

function createMarksTable() {
	let json = {
		action: sessionStorage.getItem('userRole') == 'student' ? 'getByUser' : 'getByTest',
		testId: sessionStorage.getItem('testId'),
		userId: sessionStorage.getItem('userId')
	};
	getMarksPromise(json).then(
			result => {
				marks = result.response;
				document.getElementById('central_div').append(createTable());			
			},
			error => {
				alert('error: ' + error.status);
			}
		);
}

function createTable() {
	let table = document.createElement('table');
	let header = table.insertRow(-1);
	let headers = ['№', 'test', 'user', 'mark'];
	for (let i = 0; i < headers.length; i++) {
		let th = document.createElement('th');
		th.innerHTML = headers[i];
		header.append(th);
	}

	for (i = 0; i < marks.length; i++) {
		let row = table.insertRow(-1);
		createCell(row, i + 1);
		createCell(row, marks[i].testTitle);
		createCell(row, marks[i].userLogin);
		createCell(row, marks[i].value);
	}

	return table;
}

function createCell(row, inner) {
	let cell = row.insertCell(-1);
	cell.innerHTML = inner;
}

function getMarksPromise(json = {}) {
		return new Promise(function(resolve, reject) {
			let request = new XMLHttpRequest();
			request.open('POST', 'http://localhost:8080/testing-1.0/marks');
			request.send(JSON.stringify(json));

			request.onload = function() {
				if (request.status == 200) {
					let jsonResponse = JSON.parse(request.response);
					if (jsonResponse.status == 'success') {
						resolve({response : jsonResponse.marks});
					}
					else {
						reject({status: jsonResponse.cause});
					}
				}
				else {
					reject({status: 'error'});
				}
			}
		});
	}