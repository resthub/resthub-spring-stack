/**
 * Routes
 */
define(["sammy", "sammy.storage", "sammy.title", "sammy.json", "jquery-ejs", "jquery.pnotify", "resthub.controller",
		"controllers/user.controller","controllers/booking.controller",,"controllers/hotel.controller"], function() {
	
	var app = $.sammy(function() {

		this.use('Title');
		this.use('Session');
		
		UserController(this);
		BookingController(this);

		/**
		 * Login page
		 */
		this.get('#/', function(context) {
			if(this.session('user') != null) {
				context.redirect('#/home');
			} else {
				this.title('Login');
				$('#content').render('/components/user/login.html', {});
			}
		});

		/**
		 * Home page after authentication
		 */
		this.get('#/home', function(context) {
			require(['components/home'], function() {
				$('#content').home({context: context});
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
