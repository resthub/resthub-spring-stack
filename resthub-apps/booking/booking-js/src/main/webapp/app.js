/**
 * Routes
 */
define(["jquery", "routes/user.routes", "routes/booking.routes", "routes/hotel.routes",// both can be loaded as modules
	"resthub.controller", "sammy", "sammy.storage", "sammy.title",
	/*"sammy.json",*/ "jquery.ejs", "jquery.pnotify"], function($, UserRoutes, BookingRoutes, HotelRoutes, Controller) {
	
	var app = $.sammy(function() {
		this.use('Title');
		this.use('Session');
		
		UserRoutes(this);
		BookingRoutes(this);
		HotelRoutes(this);

		/**
		 * Login page
		 */
		this.get('#/', function(cx) {
			if(this.session('user') != null) {
				cx.redirect('#/home');
			} else {
				this.title('Login');
				$('#content').render('user/login.html', {});
			}
		});

		/**
		 * Home page after authentication
		 */
		this.get('#/home', function(cx) {
			require(['home'], function() {
				$('#content').home({cx: cx});
			});
		});

	});
	
	// Rebuild Lucene index
	$.ajax({
		url: 'api/lucene/rebuild',
		dataType: 'json',
		type: 'POST'
	});
		
	$(function() {	
        app.run('#/');
	});
	
});
