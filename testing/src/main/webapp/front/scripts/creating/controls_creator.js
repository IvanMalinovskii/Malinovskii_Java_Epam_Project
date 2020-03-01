class Controls {
	_questionNum = 0;

	createTestTable() {
		let testThs = ['title', 'subject'];
		let table = this._createTable(testThs);
		let finishCell = table.rows[0].insertCell(-1);
		let finishButton = Controls.createButton('close', 'yellow', this._finishButtonClick);
		finishCell.append(finishButton);
		let testDescr = table.insertRow(-1);
		let titleInput = Controls.createInput('test_title');
		let titleCell = testDescr.insertCell(-1);
		titleCell.append(titleInput);

		let subjectInput = Controls.createInput('test_subject');
		let subjectCell = testDescr.insertCell(-1);
		subjectCell.append(subjectInput);

		let updateButton = Controls.createButton('add', 'green', this._addTestButtonClick);
		
		let addCell = testDescr.insertCell(-1);
		addCell.setAttribute('style', 'width: 10%;');
		addCell.append(updateButton);
		

		return table;
	}

	_finishButtonClick() {
		document.location.href = 'testing.html';
	}

	static _deleteTestButtonClick() {
		let testRow = this.parentNode.parentNode;
		let outer = this.parentNode.parentNode.parentNode.parentNode.parentNode;
		let json = Controls._formJsonTest('delete', sessionStorage.getItem('testId'));
		Controls._doRequest(json).then(
				result => {
					//alert('deleted');
					while (outer.children.length > 1) {
						outer.removeChild(outer.children[outer.children.length - 1]);
					}
					let testInput = Controls.createInput('test_title');
					testRow.cells[0].innerText = '';
					let subjectInput = Controls.createInput('test_subject');
					testRow.cells[1].innerText = '';
					testRow.cells[0].append(testInput);
					testRow.cells[1].append(subjectInput);
					testRow.cells[2].children[0].setAttribute('style', 'background-color: green;');
					testRow.cells[2].children[0].value = 'add';
					testRow.removeChild(this.parentNode);
				},
				error => {
					alert('error: ' + error.status);
				}
			);
	}

	_createQuestionTable() {
		let questionThs = ['question', 'answers'];
		let table = this._createTable(questionThs);

		let qRow = table.insertRow(-1);
		let questInput = Controls.createInput('question' + this._questionNum);
		let textCell = qRow.insertCell(-1);
		textCell.append(questInput);

		let answersCell = qRow.insertCell(-1);

		let addCell = qRow.insertCell(-1);
		addCell.setAttribute('style', 'width: 10%;');
		let addButton = Controls.createButton('add', 'green', this._addQuestionButtonClick);
		addCell.append(addButton);
		
		

		return table;
	}
	
	static _deleteQuestionButtonClick() {
		let table = this.parentNode.parentNode.parentNode.parentNode;
		let json = Controls._formJsonQuestion('delete', table.id);
		Controls._doRequest(json).then(
				result => {
					table.parentNode.removeChild(table);
				},
				error => {
					alert('error: ' + error.status);
				}
			);
	}

	_addQuestionButtonClick() {
		let row = this.parentNode.parentNode;
		let table = this.parentNode.parentNode.parentNode.parentNode;
		if (this.value == 'add') {
			let text = row.cells[0].children[0].value;
			let json = Controls._formJsonQuestion('insert', -1, text, sessionStorage.getItem('testId'));

			Controls._doRequest(json).then(
					result => {
						table.id = result.response.id;
						row.children[1].append(new Controls()._createAnswer());
						row.cells[0].innerText = text;
						this.setAttribute('style', 'background-color: blue;');
						this.value = 'update';
						let deleteCell = row.insertCell(-1);
						deleteCell.setAttribute('style', 'width: 10%;');
						let deleteButton = Controls.createButton('delete', 'red', Controls._deleteQuestionButtonClick);
						deleteCell.append(deleteButton);
						row.parentNode.parentNode.parentNode.append(new Controls()._createQuestionTable());
					},
					error => {
						alert('error: ' + error.status);
					}
				);
		}
		else if (this.value == 'update') {
			let input = Controls.createInput('');
			input.value = row.cells[0].innerText;
			row.cells[0].innerText = '';
			row.cells[0].append(input);
			this.setAttribute('style', 'background-color: #00ccff;');
			this.value = 'set';
		}
		else if (this.value == 'set') {
			let text = row.cells[0].children[0].value;
			let json = Controls._formJsonQuestion('update', table.id, text);
			Controls._doRequest(json).then(
					result => {
						row.cells[0].innerText = text;
						this.setAttribute('style', 'background-color: blue;');
						this.value = 'update';
					},
					error => {
						alert('error: ' + error.status);
					}
				);
		}
	}

	_createAnswer() {
		let answerThs = ['answer', 'right'];
		let table = this._createTable(answerThs, false);
		table.setAttribute('style', 'width: 95%;');
		this._createAnswerRow(table);


		return table;
	}

	_createAnswerRow(table) {
		let row = table.insertRow(-1);

		let input = Controls.createInput('');
		let inputCell = row.insertCell(-1);
		inputCell.append(input);

		let select = document.createElement('select');
		select.setAttribute('style', 'width: 95%;');
		select.innerHTML += '<option value="true">true</option>';
		select.innerHTML += '<option value="false">false</option>';
		let selectCell = row.insertCell(-1);
		selectCell.append(select);

		let addButton = Controls.createButton('add', 'green', this._addAnswerButtonClick);
		let addCell = row.insertCell(-1);
		addCell.setAttribute('style', 'width: 10%;');
		addCell.append(addButton);

		

		//return row;
	}

	static _deleteAnswerButtonClick() {
		let row = this.parentNode.parentNode;
		let table = row.parentNode.parentNode;
		let json = Controls._formJsonAnswer('delete', table.id);
		Controls._doRequest(json).then(
				result => {
					row.parentNode.removeChild(row);
				},
				error => {
					alert('error: ' + error.status);
				}
			);
	}

	_addAnswerButtonClick() {
		let row = this.parentNode.parentNode;
		let table = row.parentNode.parentNode;
		let outerTable = table.parentNode.parentNode.parentNode.parentNode;
		if (this.value == 'add') {
			let text = row.cells[0].children[0].value;
			let right = row.cells[1].children[0].value;
			let json = Controls._formJsonAnswer('insert', -1, text, right, outerTable.id);
			Controls._doRequest(json).then(
					result => {
						table.id = result.response.id;
						row.cells[0].innerText = text;
						row.cells[1].innerText = right;
						this.value = 'update';
						this.setAttribute('style', 'background-color: blue;');
						let deleteButton = Controls.createButton('delete', 'red', Controls._deleteAnswerButtonClick);
						let deleteCell = row.insertCell(-1);
						deleteCell.setAttribute('style', 'width: 10%;');
						deleteCell.append(deleteButton);
						new Controls()._createAnswerRow(row.parentNode);
					},
					error => {
						alert('error: ' + error.status);
					}
				);
			
		}
		else if (this.value == 'update') {
			let input = Controls.createInput('');
			input.value = row.cells[0].innerText;
			let select = document.createElement('select');
			select.setAttribute('style', 'width: 95%;');
			select.innerHTML += '<option value="true">true</option>';
			select.innerHTML += '<option value="false">false</option>';
			row.cells[0].innerText = '';
			row.cells[0].append(input);
			row.cells[1].innerText = '';
			row.cells[1].append(select);
			this.value = 'set';
			this.setAttribute('style', 'background-color: #00ccff;');
		}
		else if (this.value == 'set') {
			let text = row.cells[0].children[0].value;
			let right = row.cells[1].children[0].value;
			let json = Controls._formJsonAnswer('update', table.id, text, right);
			Controls._doRequest(json).then(
						result => {
							row.cells[0].innerText = text;
							row.cells[1].innerText = right;
							this.value = 'update';
							this.setAttribute('style', 'background-color: blue;');
						},
						error => {
							alert('error: ' + error.status);
						}
				);
		}
	}

	_createTable(ths = [], flag = true) {
		let table = document.createElement('table');
		let header = table.insertRow(-1);

		for(let i = 0; i < ths.length; i++) {
			let th = document.createElement('th');
			let percent = flag ? (28 * (i + 1)) : 40;
			th.setAttribute('style', 'width: ' + percent + '%;');
			th.innerHTML = ths[i];
			header.append(th);
		}
		return table;
	}

	static createInput(id = '') {
		let input = document.createElement('input');
		input.setAttribute('id', id);
		input.setAttribute('type', 'text');
		input.setAttribute('style', 'display: initial; width: 95%;');
		return input;
	}

	static createButton(value = '', color = '', func) {
		let button = document.createElement('input');
		button.setAttribute('type', 'button');
		button.setAttribute('value', value);
		button.setAttribute('style', 'background-color: ' + color + '; display: initial;')
		button.onclick = func;
		return button;
	}

	_addTestButtonClick() {
		let testRow = this.parentNode.parentNode;
		if (this.value == 'add') {
			let title = testRow.cells[0].children[0].value;			
			let subject = testRow.cells[1].children[0].value;
			
			let json = Controls._formJsonTest('insert', -1, title, subject, sessionStorage.getItem('userId'));
			Controls._doRequest(json).then(
					result => {
						sessionStorage.setItem('testId', result.response.id);
						testRow.cells[0].innerText = title;
						testRow.cells[1].innerText = subject;
						this.setAttribute('style', 'background-color: blue;');
						this.value = 'update';
						let deleteButton = Controls.createButton('delete', 'red', Controls._deleteTestButtonClick);
						let deleteCell = testRow.insertCell(-1);
						deleteCell.setAttribute('style', 'width: 10%;');
						deleteCell.append(deleteButton);
						testRow.parentNode.parentNode.parentNode.append(new Controls()._createQuestionTable());
					},
					error => {
						alert('erorr: ' + error.status);
					}
				);
		}
		else if (this.value == 'set') {
			let title = testRow.cells[0].children[0].value;
			let subject = testRow.cells[1].children[0].value;
			let json = Controls._formJsonTest('update', sessionStorage.getItem('testId'), title, subject, sessionStorage.getItem('userId'));
			Controls._doRequest(json).then(
					result => {
						testRow.cells[0].innerText = title;
						testRow.cells[1].innerText = subject;
						this.setAttribute('style', 'background-color: blue;');
						this.value = 'update';
					},
					error => {
						alert('erorr: ' + error.status);
					}
				);
		}
		else {
			let testInput = Controls.createInput('test_title');
			testInput.value = testRow.cells[0].innerText;
			testRow.cells[0].innerText = '';
			let subjectInput = Controls.createInput('test_subject');
			subjectInput.value = testRow.cells[1].innerText;
			testRow.cells[1].innerText = '';
			testRow.cells[0].append(testInput);
			testRow.cells[1].append(subjectInput);
			this.setAttribute('style', 'background-color: #00ccff;');
			this.value = 'set';
		}
	}

	static _doRequest(json = {}) {
		return new Promise(function(resolve, reject) {
			let request = new XMLHttpRequest();
			request.open('POST', 'http://localhost:8080/testing-1.0/change');
			request.send(JSON.stringify(json));

			request.onload = function() {
				if (request.status == 200) {
					let jsonResponse = JSON.parse(request.response);
					if (jsonResponse.status == 'success') {
						resolve({response : jsonResponse});
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

	static _formJsonTest(action = '', id = -1, title = '', subject = '', userId = -1) {
		return {
			entity: 'test',
			action: action,
			id: id,
			title: title,
			subject: subject,
			userId: userId
		};
	}

	static _formJsonQuestion(action = '', id = -1, text = '', testId = -1) {
		return {
			entity: 'question',
			action: action,
			id: id,
			text: text,
			testId: testId
		};
	}

	static _formJsonAnswer(action = '', id = -1, text = '', right = false, questionId = -1) {
		return {
			entity: 'answer',
			action: action,
			id: id,
			text: text,
			right: right,
			questionId: questionId
		};
	}
}