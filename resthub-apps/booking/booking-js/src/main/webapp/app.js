/**
 * Routes
 */
define(["sammy", "jquery.json", "jquery.ejs", "jquery.pnotify", "resthub.controller",
		"routes/user.routes","routes/booking.routes","routes/hotel.routes", "jquery.tinypubsub","resthub.storage"], function() {
	
	var app = $.sammy(function() {
		
		UserRoutes(this);
		BookingRoutes(this);
		HotelRoutes(this);

		/**
		 * Login page
		 */
		this.get('#/', function() {
			if($.storage.getJSONItem('user') != null) {
				location.hash = '#/home';
			} else {
				document.title = 'Login';
				$('#content').render('user/login.html', {});
			}
		});

		/**
		 * Home page after authentication
		 */
		this.get('#/home', function() {
			require(['home'], function() {
				$('#content').home();
			});
		});

	});

	$(function() {

		// Rebuild Lucene index
		$.ajax({
			url: 'api/lucene/rebuild',
			dataType: 'json',
			type: 'POST'
		});
		
        app.run('#/');
	});
	
});
