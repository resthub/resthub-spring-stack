/**
 * Routes
 */
(function($) {
	
	var app = $.sammy(function() {

		this.use(Sammy.Title);
		this.use(Sammy.Session);

		this.get('#/', function() {
			this.title('Login');
			var user = this.session('user', function() {return {};});
			$('#header').render('components/header.html', {user: user});
			$('#content').render('components/login.html', {})
		});

		this.get('#/logout', function(context) {
			this.store('session').clearAll();
			context.redirect('#/');
		});

		this.get('#/register', function() {
			this.title('Register');
			$('#content').render('components/register.html', {});
		});

		/*
		 * Home page after authentication
		 */
		this.get('#/home', function(context) {
			this.title('Home');
			var user = this.session('user', function() {return {};});
			$('#content').restController({url: 'api/booking/user/' + user.id, jsFile: 'components/booking/list.js', callbackFunction: 'listBookings', context: context});
		});

		/*
		 * Search hotels
		 */
		this.get('#/hotel/search', function() {
			var searchVal = this.params['q'];
			$('#result').restController({url: 'api/hotel/search?q=' + searchVal, jsFile: 'components/hotel/list.js', callbackFunction: 'listHotels'});
		});

		/*
		 * List bookings
		 */
		this.get('#/booking/list', function() {
			$('#content').restController({url: 'api/booking', jsFile: 'components/booking/list.js', callbackFunction: 'listBookings'});
		});

		/*
		 * List hotels
		 */
		this.get('#/hotel/list', function() {
			$('#result').restController({url: 'api/hotel', jsFile: 'components/hotel/list.js', callbackFunction: 'listHotels'});
		});

		/**
		 * View hotel
		 */
		this.get('#/hotel/:id', function(context) {
			var id = this.params['id'];
			$('#content').restController({url: 'api/hotel/'+id, jsFile: 'components/hotel/view.js', callbackFunction: 'viewHotel', context: context});
        });

		/**
		 * Book hotel identified by 'id'
		 */
		 this.get('#/booking/hotel/:id', function(context) {
			var id = this.params['id'];
			$('#content').restController({jsFile: 'components/booking/book.js', callbackFunction: 'bookBooking', data: id, context: context});
        });
        
		/**
		 * Booking confirmation page
		 */
		this.get('#/booking/confirm', function() {
			$('div#booking-form').restController({jsFile: 'components/booking/confirm.js', callbackFunction: 'confirmBooking'});
		});

		/**
		 * Save booking in database
		 */
		this.post('#/booking', function() {

			alert('Not supported yet!');

		});

		/**
		 * User authentication
		 */
		this.post('#/user/check', function(context) {
			$('#content').resthubAuth({context: context});
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
})(jQuery);
