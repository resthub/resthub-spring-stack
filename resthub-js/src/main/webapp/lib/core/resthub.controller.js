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

		_authenticate: function(username, password, callback) {
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
					if (callback) {
						callback.call(this);
					}
				}, this)
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

		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax: function(url, callback, type, data) {
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
			},
			accessToken);
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