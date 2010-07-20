/*!
 * jQuery OAuth 2 implementation
 * 
 * May be used as standalone functions, or directly in ResthubController widget.
 * 
 * Does not manage the access token storage, nor that the protocol errors.
 * Users could thus plug their own storage and error management solutions. 
 */
(function($) {

	jQuery.extend({   
		
		/**
		 * Configuration used for OAuth 2 protocol calls with the server.
		 */
		oauth2Conf: {
			/**
			 * Unic identifier of the client (the webapp).
			 * Not used for now.
			 */
			client_id: "",
			
			/**
			 * Unic secret(password) of the client (the webapp).
			 * Not used for now.
			 */
			client_secret: "",
			
			/**
			 * Url of the authorization token end-point which issued tokens.
			 */
			tokenEndPoint: ""
		},
				
		/**
		 * Sends a request to get the access token.
		 * An OAuth 2 "token request" is sent to the $.oauth2Conf.tokenEndPoint url.
		 * 
		 * The returned token (if successful) is given to the specified callback.
		 * You may stores this token, and passes it further to the oaut2Ajax() function,
		 * which performs an access to an OAuth 2 protected resource.
		 * 
		 * @param username The resource owner login (end-user login).
		 * @param password The resource owner password (end-user password).
		 * @param callback An optionnal callback, called when the token is returned by the server.
		 * This function takes only one parameter, which is the token (JSON structure).
		 */
		getOauth2token: function( username, password, callback ) {					
			var _this = this;
			
			// Performs a request to get an authentication token.
			$.post(
					// Url
					jQuery.oauth2Conf.tokenEndPoint,
					
					// Send data.
					{
						client_id: jQuery.oauth2Conf.client_id,
						client_secret: jQuery.oauth2Conf.client_secret,
						grant_type: "basic-credentials",
						username: username,
						password: password
					},
						
					// Success callback.
					function ( data ) {
						// Calls the callback
						callback.call( this, $.parseJSON(data));
					},
					
					// Awaited response format.
					"application/json"
			);
		}, // getOauth2token().
		
		/**
		 * Sends an Ajax request to access an OAuth 2 protected resource.
		 * Uses the same first parameter as jquery.ajax() (which is internally called).
		 * It adds to this structure the attribute: 
		 * - authorizationError: function ( errorObj, XMLHttpRequest ) 
		 * This callback is called when an OAuth 2 protocol error occurs.
		 * errorObj is a structure containing :
		 * - status: A string returned by the server explaining error.
		 * - message: An optionnal explanation message.
		 * 
		 * @param params The jquery.ajax() parameter, containing url, data, callbacks...
		 * @param accessToken The accessToken structure, retrieved with getOauth2token().
		 */
		oauth2Ajax: function( params, accessToken) {
			// Keep the existing beforeSend handler.
			var existingBefore = params.beforeSend;
			// Adds the access token in the request header.
			params.beforeSend = function( XMLHttpRequest ) {
				if(accessToken && "access_token" in accessToken) {
					XMLHttpRequest.setRequestHeader("Authorization", 
							'Token token="'+ accessToken.access_token +'"');
				}
				// Relaunches the existing callback, on the params context.
				if(existingBefore instanceof Function) {
					existingBefore.call(params, XMLHttpRequest);
				}	
			};
			// Keep the existing error handler.
			var existingError = params.error;
			// Gets the protocol errors.
			params.error = function( XMLHttpRequest, textStatus, errorThrown ) {
				// Only for 400, 401 and 403 scopes.
				if(XMLHttpRequest.status == 400 ||
						XMLHttpRequest.status == 401 || 
						XMLHttpRequest.status == 403) {
					// Extract the WWW-Authenticate response header.
					var error = XMLHttpRequest.getResponseHeader("WWW-Authenticate");
					var errorObj = {};
					// Parse the error status and optionnal message.
					var i = error.indexOf('error="');
					if (i != -1) {
						errorObj.status = error.substring(i+7, error.indexOf('"', i+7));
					} else {
						errorObj.status = "invalid-request";
					}
					i = error.indexOf('error-description="');
					if (i != -1) {
						errorObj.message = error.substring(i+19, error.indexOf('"', i+19));
					}
					if (this.authorizationError instanceof Function) {
						this.authorizationError.call(params, errorObj, XMLHttpRequest);
					}
				}
				// Relaunches the existing callback.
				if(existingError instanceof Function) {
					existingError.call(XMLHttpRequest, XMLHttpRequest);
				}	
			};
			// Triggers the ajax call.
			return jQuery.ajax( params );
		} // oauth2Ajax
	});

})(jQuery);