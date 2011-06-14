define(['lib/resthub',
        'lib/oauth2client',
        'routes/user.routes',
        'routes/group.routes'], function(){	
		
	Constants = {
		/**
		 * Key used to store user in storage.
		 */
		USER_KEY: "currentUser"	
	};
	
	/**
	 * Application launch.
	 * Runs the login route, or goes to home if a user is found in local storage.
	 */
	$(document).ready(function() {
		OAuth2Client.clientId = "identity";
		OAuth2Client.clientSecret = "";
		OAuth2Client.tokenEndPoint = "oauth/authorize";

		$.route('#/', function() {
			// When arriving, if user is already known, go to home.
			if ($.storage.get(Constants.USER_KEY) != null) {
				$.route('#/home');
				$.publish('user-logged-in');
			} else {
				$.route('#/login');
			}
		});
		
		// Run the selected route.
		$.route(location.hash);
	});
});