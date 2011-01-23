/**
 * Routes
 */
define([ "jquery", "routes/user.routes", "routes/booking.routes",
		"routes/hotel.routes", "resthub.controller", "console",
		"resthub.route", "resthub.storage", "jquery.json",
		"jquery.ejs", "jquery.pnotify", "home" ], function($, UserRoutes,
		BookingRoutes, HotelRoutes, Controller) {

	$(document).ready(function() {
					
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
