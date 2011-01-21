/**
 * Routes
 */
define([ "jquery", "routes/user.routes", "routes/booking.routes",
		"routes/hotel.routes", "resthub.controller", "console",
		"route", "resthub.storage", "jquery.json",
		"jquery.ejs", "jquery.pnotify", "home" ], function($, UserRoutes,
		BookingRoutes, HotelRoutes, Controller) {

	$(document).ready(function() {
					
		route('#').bind(function() {
			if ($.storage.getJSONItem('user') != null) {
				route('#/home').run();
			} else {
				$('#content').userLogin();
			}
		});
		
		route('#/home').bind(function() {
			$('#content').home();
		});

		// Rebuild Lucene index
		$.ajax({
			url : 'api/lucene/rebuild',
			dataType : 'json',
			type : 'POST'
		});
		
		route('#').run();

	});

});
