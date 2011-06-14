/**
 * Routes
 */
define([ 'lib/resthub',
         'routes/user.routes',
         'routes/booking.routes',
		 'routes/hotel.routes',
		 'lib/oauth2client',
		 'lib/jquery/jquery.pnotify',
		 'home' ],
		 function($, UserRoutes, BookingRoutes, HotelRoutes, OAuth2Client) {

	$(document).ready(function() {
		
		OAuth2Client.clientId = "booking";
		OAuth2Client.clientSecret = "";
		OAuth2Client.tokenEndPoint = "oauth/authorize";
		
		// Store default values
		$.storage.set('search-size', 5);
		$.storage.set('search-page', 0);
		
		$.route('#/', function() {
			$('#content').user_login();
		});
		
		$.route('#/home', function() {
			$('#content').home();
		});

		// Rebuild Lucene index
		$.ajax({
			url : 'api/lucene/rebuild',
			dataType : 'json',
			type : 'POST'
		});
		
		$.route(location.hash);

	});

});
