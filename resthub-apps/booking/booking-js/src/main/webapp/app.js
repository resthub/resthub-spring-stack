/**
 * Routes
 */
define([ "jquery", "routes/user.routes", "routes/booking.routes",
		"routes/hotel.routes", "resthub.controller", "console",
		"jquery.tinypubsub", "route", "resthub.storage", "jquery.json",
		"jquery.ejs", "jquery.pnotify", "home" ], function($, UserRoutes,
		BookingRoutes, HotelRoutes, Controller) {

	$(document).ready(function() {
		/**
		 * Login page
		 */
		route('#/').bind(function() {
			if ($.storage.getJSONItem('user') != null) {
				route('#/home').run();
			} else {
				document.title = 'Login';
				$('#content').render('user/login.html', {});
			}
		});

		/**
		 * Home page after authentication
		 */
		route('#/home').bind(function() {
			$('#content').home();
		});

		// Rebuild Lucene index
		$.ajax({
			url : 'api/lucene/rebuild',
			dataType : 'json',
			type : 'POST'
		});

		route('#/home').run();
		console.log('location.hash changed');
	});

});
