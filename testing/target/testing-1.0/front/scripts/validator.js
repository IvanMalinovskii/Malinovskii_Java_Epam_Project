// finds out whether input data is valid or not
class Validator {
	_EMAIL_PATTERN = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	_PASS_PATTERN = /(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,15}/g;
	_LOGIN_PATTERN = /[0-9a-zA-Z]{6,12}/;
	_NAME_PATTERN = /[A-Z]{1}[a-z]{3,}/;

	// validates a string by a type
	validate(line, type) {
		switch(type) {
			let pattern = '';
			case 'login':
				pattern = this._LOGIN_PATTERN;
			case 'email':
				pattern = this._EMAIL_PATTERN;
			case 'password':
				pattern = this._PASS_PATTERN;
			case 'name':
				pattern = this._NAME_PATTERN;
			default:
				return false;
		}
		if (!line.match(pattern)) {
					return false;
				}
				return true;
	}
}
