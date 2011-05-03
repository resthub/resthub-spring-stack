/**
 * Routes
 */
define([ 'lib/resthub',
         'routes/user.routes',
         'routes/booking.routes',
		'routes/hotel.routes',
		'lib/jquery/jquery.pnotify',
		'lib/oauth2controller',
		'home' ],
		function($, UserRoutes, BookingRoutes, HotelRoutes, Controller) {

	$(document).ready(function() {
		
		OAuth2Controller.clientId = "booking";
		OAuth2Controller.clientSecret = "";
		OAuth2Controller.tokenEndPoint = "oauth/authorize";
		
		// Store default values
		$.storage.set('search-size', 5);
		$.storage.set('search-page', 0);
		
		$.route('#/', function() {
			if ($.storage.get('user') != null) {
				$.route('#/home');
			} else {
				$('#content').user_login();
			}
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
