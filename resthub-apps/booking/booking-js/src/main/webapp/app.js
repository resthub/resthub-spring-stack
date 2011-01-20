/**
 * Routes
 */
define([ "jquery", "routes/user.routes", "routes/booking.routes",
		"routes/hotel.routes", "resthub.controller", "console",
		"jquery.tinypubsub", "route", "resthub.storage", "jquery.json",
		"jquery.ejs", "jquery.pnotify", "home" ], function($, UserRoutes,
		BookingRoutes, HotelRoutes, Controller) {

	$(document).ready(function() {
					
		$.storage.clear();
		
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
		
		if ($.storage.getJSONItem('user') != null) {
			var user = $.storage.getJSONItem('user');
			$('#header').render('header.html', {
				user : user
			});
			location.hash = '#/home';
		} else {
			document.title = 'Login';
			$('#content').render('user/login.html', {});
		}

	});

});
