
class Test {
	_test = {};
	_currentQuestion = 0;
	_questionNumber = 0;
	_maxPoint = 0;
	_points = [];

	constructor(test = {}) {
		this._test = test;
		this._questionNumber = test.questions.length;
		for (let qIndex = 0; qIndex < this._questionNumber; qIndex++) {
			for (let aIndex = 0; aIndex < this._test.questions[qIndex].answers.length; aIndex++) {
				if (this._test.questions[qIndex].answers[aIndex].right == true) {
					this._maxPoint++;
				}
				this._points.push(0);
			}
		}
	}

	getCurrent() {
		return this._test.questions[this._currentQuestion];
	}

	getQIndex() {
		return this._currentQuestion;
	}

	isNext() {
		if ((this._currentQuestion + 1) < this._questionNumber) {
			this._currentQuestion++;
			return true;
		}
		return false;
	}

	isPrevious() {
		if ((this._currentQuestion - 1) >= 0) {
			this._currentQuestion--;
			return true;
		}
		return false;
	}

	getNext() {
		if ((this._currentQuestion + 1) < this._questionNumber) {
			return this._test.questions[++this._currentQuestion];
		}
		return null;
	}

	getPrevious() {
		if ((this._currentQuestion - 1) >= 0) {
			return this._test.questions[--this._currentQuestion];
		}
		return null;
	}

	getPoints(aIndexes = []) {
		let points = 0;
		for (let i = 0; i < aIndexes.length; i++) {
			if (this._test.questions[this._currentQuestion].answers[aIndexes[i]].right == true) {
				points++;
			}
			else {
				points--;
			}
		}
		this._points[this._currentQuestion] = points;
	}

	getPercent() {
		if (this._maxPoint != 0) {
			let points = 0;
			for (let i = 0; i < this._points.length; i++) {points += this._points[i];}
			return points / this._maxPoint * 10;
		}
		return 10;
	}
}