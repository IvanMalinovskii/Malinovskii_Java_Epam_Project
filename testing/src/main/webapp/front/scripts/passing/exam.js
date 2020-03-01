
let test = {}
let testObj;

function getTest() {
	let request = new XMLHttpRequest();
	request.open('POST', 'http://localhost:8080/testing-1.0/test');
	let jsonRequest = {
		action: 'getById',
		id: sessionStorage.getItem('testId')
	};
	request.send(JSON.stringify(jsonRequest));

	request.onload = function() {
		if (request.status == 200) {
			let jsonResponse = JSON.parse(request.response);
			test = jsonResponse.test;
			testObj = new Test(test);
			for (let i = 0; i < test.questions.length; i++) {
				createDivQuestion(test.questions[i], i);
			}
			if (document.getElementById('question0') == undefined) {
				alert("empty test");
				document.location.href = "testing.html";
			}
			else {
				document.getElementById('question0').style.display = 'initial';
			}
		}
	}
}

function createDivQuestion(question = {}, qIndex) {
	let div = document.createElement('div');
	div.className = 'inner_div';
	div.id = 'question' + qIndex;
	div.innerHTML = "<p class='q_p'>" + (qIndex + 1) + ': ' + question.text + "</p>";
	for (let i = 0; i < question.answers.length; i++) {
			let checkbox_id = 'id="answer' + qIndex + '_' + i + '"';
			//alert(checkbox_id);
			div.innerHTML += '<label class="check_container">' + question.answers[i].text 
						  + '<input type="checkbox"' + checkbox_id + '><span class="checkmark"></span></label>';
		}
	div.style.display = 'none';
	document.getElementById('questions').append(div);
}

function previousClick() {
	testObj.getPoints(getChecked());
	let prevIndex = testObj.getQIndex();
	if (testObj.isPrevious()) {
		document.getElementById('question' + prevIndex).style.display = 'none';
		document.getElementById('question' + testObj.getQIndex()).style.display = 'initial';
	}
}

function nextClick() {
	testObj.getPoints(getChecked());
	let prevIndex = testObj.getQIndex();
	if (testObj.isNext() != null) {
		document.getElementById('question' + prevIndex).style.display = 'none';
		document.getElementById('question' + testObj.getQIndex()).style.display = 'initial';
	}
}

function getChecked() {
	let questions = testObj.getCurrent();
	let aIndexes = [];
	//alert(testObj.getQIndex());
	for (let i = 0; i < questions.answers.length; i++) {
		//alert('answer' + testObj.getQIndex + '_' + i);
		if (document.getElementById('answer' + testObj.getQIndex() + '_' + i).checked) {
			aIndexes.push(i);
		}
	}
	return aIndexes;
}

function resultClick() {
	testObj.getPoints(getChecked());
	document.getElementById('question' + testObj.getQIndex()).style.display = 'none';
	let request = new XMLHttpRequest();
	request.open('POST', 'http://localhost:8080/testing-1.0/marks');
	let jsonRequest = {
		action: 'insert',
		value: Math.round(testObj.getPercent()),
		testId: sessionStorage.getItem('testId'),
		userId: sessionStorage.getItem('userId')
	};
	request.send(JSON.stringify(jsonRequest));

	request.onload = function() {
		if(request.status == 200) {
			let jsonResponse = JSON.parse(request.response);
			if (jsonResponse.status == 'success') {
				//alert('+');
				let p = document.createElement('p');
				p.innerText = 'Your mark is ' + jsonRequest.value;
				document.getElementById('next_but').style.display = 'none';
				document.getElementById('prev_but').style.display = 'none';
				document.getElementById('result_button').style.display = 'none';
				document.getElementById('questions').append(p);
			}
			else {
				alert(jsonResponse.cause);
			}
		}
	}
}

