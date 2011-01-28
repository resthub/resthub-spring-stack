/**
 * Routes
 */
define([ 'lib/resthub', 'routes/user.routes', 'routes/booking.routes',
		'routes/hotel.routes', 'lib/jquery/jquery.pnotify', 'home' ],
		function($, UserRoutes, BookingRoutes, HotelRoutes, Controller) {

	$(document).ready(function() {
		
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
