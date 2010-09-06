/**
 * Resthub-controller is a generic javascript controller for resthub applications.
 * It provides utility functions for basic op�rations.
 */

(function($)
{
	var resthubController =
	{
		_create: function() {
		// TODO
		},

		_init: function() {
		// TODO
		},

		/**
		 * Sends a request to get the access token.
		 * An OAuth 2 "token request" is sent to the $.oauth2Conf.tokenEndPoint url.
		 * 
		 * Used the getOauth2token() method, and stores the access token (if successful)
		 * in session, for further resource calles.
		 * 
		 * @param username The resource owner login (end-user login).
		 * @param password The resource owner password (end-user password).
		 * @param success A callback, called when the token is returned by the server.
		 * This function takes only one parameter, which is the token (JSON structure).
		 * @param error A callback, called when the server refused to issue a token.
		 * This function takes two parameters: the first is the error string, and the second
		 * an option explanation.
		 */
		_authenticate: function(username, password, success, error) {
			// Clears previous session storage.
			var accessToken = this.options.context.store('session').clear('accessToken');
			// Sends a token request.
			$.getOauth2token(
				username,
				password,
				$.proxy(function( accessToken ) {
					// Stores the token in session.
					this.options.context.session('accessToken', accessToken);
					// Calls the callback
					if (success) {
						success.call(this);
					}
				}, this),
				error
			);
		},

		_post: function _post(url, callback, data) {
			this._ajax(url, callback, 'post', data);
		},

		_get: function _get(url, callback) {
			this._ajax(url, callback, 'get', null);
		},

		_put: function _put(url, callback, data) {
			this._ajax(url, callback, 'put', data);
		},

		_delete: function _delete(url, callback) {
			this._ajax(url, callback, 'delete', null);
		},

		_securedGet: function _get(url, callback) {
			this._securedAjax(url, callback, 'get', null);
		},

		_securedPut: function _put(url, callback, data) {
			this._securedAjax(url, callback, 'put', data);
		},

		_securedDelete: function _delete(url, callback) {
			this._securedAjax(url, callback, 'delete', null);
		},

		_securedPost: function _post(url, callback, data) {
			this._securedAjax(url, callback, 'post', data);
		},

		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax: function(url, callback, type, data) {
			$.ajax({
				url: url,
				dataType: this.options.dataType,
				contentType: this.options.contentType,
				type: type,
				data: data,
				success: $.proxy( callback , this )
			});
		},
		
		/**
		 * Perform basic ajax request and call your widget back.
		 * 
		 * If an OAuth 2 accessToken is found un session, use it to access the protected URL.
		 */
		_securedAjax: function(url, callback, type, data) {
			// Gets the token existing in session.
			var accessToken = this.options.context.session('accessToken');
			// Performs an Ajax's Oauth secured request.
			$.oauth2Ajax({
				url: url,
				dataType: this.options.dataType,
				contentType: this.options.contentType,
				type: type,
				data: data,
				success: $.proxy( callback , this )
			});
		},

		
		_set: function(key, value) {
			this.options[key] = value;
		},
		options: {
			dataType: 'json',
			contentType: 'application/json; charset=utf-8'
		}
	};

	$.widget("resthub.resthubController", resthubController);
	
})(jQuery);