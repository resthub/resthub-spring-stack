define([ 'lib/repository', 'lib/oauth2client' ], function(Repository, OAuth2Client) {

	/**
	 * Use this special Repository implementation when accessing a backend protected with OAuth2.
	 */
	return Repository.extend("OAuth2Repository", {
	
		/**
		 * This callback is invoked when an authentication error occured.
		 * Used to have a single central management of 401 and 400 errors.
		 * Please overload it with a function taking three parameters:
		 * 1- XMLHttpRequest object.
		 * 2- String error status.
		 * 3- String error detailed message.
		 */
		authorizationError: null,
	
		/**
		 * Overload of the Repository ajax method to add the OAuth2 token when available.
		 * The token is retrieved in the $.storage() with the key OAuth2Client.storageKey.
		 */
		_ajax : function(url, callback, type, data, errorCallback, settings) {
			// Gets the token existing in session.
			var accessToken = $.storage.get(OAuth2Client.storageKey);
			var _settings = {
					url : url,
					dataType : this.defaults.dataType,
					contentType : this.defaults.contentType,
					type : type,
					data : data,
					success : callback,
					// Adds the access token in the request header.
					beforeSend: function( XMLHttpRequest ) {
						if(accessToken && "access_token" in accessToken) {
							XMLHttpRequest.setRequestHeader("Authorization", 
									'Bearer '+ accessToken.access_token);
						}
					}
				};
			// Default callback
			var existingError = function(XMLHttpRequest, textStatus, errorThrown) {
				var error = {
					pnotify_title: 'Server problem',
					pnotify_text: 'The action cannot be realized:\n',
					pnotify_type: 'error'
				};
				error.pnotify_text += errorThrown ? errorThrown : textStatus;
				$.pnotify(error);
			};
			if (errorCallback) {
				existingError = errorCallback;
			}
			// Gets the protocol errors.
			var authorizationError = this.authorizationError;
			_settings.error = function(XMLHttpRequest, textStatus, errorThrown) {
				// Only for 400, 401 and 403 scopes.
				if(XMLHttpRequest.status == 400 ||
						XMLHttpRequest.status == 401
						// || 
						//XMLHttpRequest.status == 403
						//TODO fix http://bitbucket.org/ilabs/resthub/issue/42/oauth2-resthuboauth2js-oauth2ajax
						) {
					// Extract the WWW-Authenticate response header.
					var error = XMLHttpRequest.getResponseHeader("WWW-Authenticate");
					var status='';
					var message='';
					// Parse the error status and optionnal message.
					var i = error.indexOf('error="');
					if (i != -1) {
						status = error.substring(i+7, error.indexOf('"', i+7));
					} else {
						status = 'invalid_request';
					}
					i = error.indexOf('error_description="');
					if (i != -1) {
						message = error.substring(i+19, error.indexOf('"', i+19));
					}
					// Special case: expired_token needs to be removed.
					if (status == 'expired_token' || status == 'invalid_token') {
						$.storage.remove(OAuth2Client.storageKey);
					}
					if (authorizationError instanceof Function) {
						authorizationError.call(XMLHttpRequest, XMLHttpRequest, status, message);
					} else {
						// Relaunches the existing callback.
						existingError.call(XMLHttpRequest, XMLHttpRequest, status, message);
					}
				} else {
					// Relaunches the existing callback.
					existingError.call(XMLHttpRequest, XMLHttpRequest, textStatus, errorThrown);
				}
			};
			// Triggers the ajax call.
			if (settings) {
				$.extend(true, _settings, settings);
			}
			$.ajax(_settings);
		}
	}, {});
});
