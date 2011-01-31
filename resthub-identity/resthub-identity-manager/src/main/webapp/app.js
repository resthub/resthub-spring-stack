define(['lib/resthub',
        'lib/oauth2controller',
        'routes/user.routes'], function(){	
		
	Constants = {
		/**
		 * Key used to store user in storage.
		 */
		USER_KEY: "currentUser"	
	};
	
	$(document).ready(function() {
		OAuth2Controller.clientId = "";
		OAuth2Controller.clientSecret = "";
		OAuth2Controller.tokenEndPoint = "api/authorize/token";

		$.route('#/', function() {
			// When arriving, if user is already known, go to home.
			if ($.storage.get(Constants.USER_KEY) != null) {
				$.route('#/home');
				$.publish('user-logged-in');
			} else {
				$.route('#/login');
			}
		});
		
		$.route(location.hash);
	});
});