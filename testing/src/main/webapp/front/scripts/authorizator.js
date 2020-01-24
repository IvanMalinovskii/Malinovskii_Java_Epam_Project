// does ajax requests for signUp/signIn
class Authorizator {
	_login ='';
	_email ='';
	_password ='';
	_role = '';
	_name = '';
	_surname = '';
	_link ='';

	// initializes fields with input data
	constructor(login, email, password, role, name, surname, link) {
		this._login = login;
		this._email = email;
		this._password = password;
		this._role = role;
		this._name = name;
		this._surname = surname;
		this._link = link;
	}

	// does an http request according to the action
	async doRequest(action) {
		let jsonString = this._formJsonString(action);
		return await this._doPost(jsonString, this._link);
	}

	_formJsonString(action) {
		let jsonObject = {
			action: action,
			login: this._login,
			email: this._email,
			password: this._password,
			role: this._role,
			name: this._name,
			surname: this._surname
		}
		return JSON.stringify(jsonObject);
	}

	_doPost(jsonString, link) {
		return new Promise(function(resolve, reject) {
			let request = new XMLHttpRequest();
			request.open('POST', link);
			request.send(jsonString);

			request.onload = function() {
				if(request.status == 200) {
					let response = JSON.parse(request.response);
					resolve({status : response.status, content: response});
				}
				else {
					reject({status: request.status});
				}
			};

			request.onerror = function() {
				reject({status: 'server error'});
			};
		});
	}
}