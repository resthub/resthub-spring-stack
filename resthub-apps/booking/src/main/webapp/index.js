/**
 * Routes
 */
(function($) {
	
	var app = $.sammy(function() {

		this.use(Sammy.Title);
		this.use(Sammy.Session);

		this.bind('run-route', function() {
			var user = this.session('user');
			$('#header').render('components/header.html', {user: user});
		});

		this.bind('end-of-booking', function() {
			var booking = this.session('booking');
			$.pnotify('Thank you, ' + booking.user.name + ', your confimation number for ' + booking.hotel.name + ' is ' + booking.id + '.');
			this.store('session').clear('booking');
		});

		this.bind('user-logged-in', function() {
			var user = this.session('user');
			$.pnotify('Welcome ' + user.name + ' !');
		});

		this.bind('user-logged-out', function() {
			$.pnotify('See ya !');
		});

		/**
		 * Login page
		 */
		this.get('#/', function(context) {
			if(this.session('user') != null) {
				context.redirect('#/home');
			} else {
				this.title('Login');
				$('#content').render('components/login.html', {})
			}
		}, 'components/login.js');

		/**
		 * User authentication
		 */
		this.post('#/user/check', function(context) {
			$('#header').login({context: context});
		}, 'components/login.js');

		/**
		 * User logout
		 */
		this.get('#/logout', function(context) {
			context.trigger('user-logged-out');
			context.store('session').clearAll();
			context.redirect('#/');
		}, 'components/login.js');

		/**
		 * User register
		 */
		this.get('#/register', function() {
			this.title('Register');
			$('#content').render('components/register.html', {});
		});

		/**
		 * Home page after authentication
		 */
		this.get('#/home', function(context) {
			this.title('Home');
			var user = this.session('user');
                         
            $('#header').render('components/header.html', {user: user});
			$('#content').listBookings({data: {user: user}, context: context});			
		}, 'components/booking/list.js');

		/**
		 * Search hotels
		 */
		this.get('#/hotel/search', function() {
			$('#result').listHotels({data: {searchVal: this.params['q']}});
		}, 'components/hotel/list.js');

		/**
		 * List bookings
		 */
		this.get('#/booking/list', function() {
			$('#content').listBookings();
		}, 'components/booking/list.js');

		/**
		 * List hotels
		 */
		this.get('#/hotel/list', function() {
			$('#result').listHotels();
		}, 'components/hotel/list.js');

		/**
		 * View hotel
		 */
		this.get('#/hotel/:id', function(context) {
			$('#content').viewHotel({data: {id: this.params['id']}, context: context});
        }, 'components/hotel/view.js');

		/**
		 * Book hotel identified by 'id'
		 */
		 this.get('#/booking/hotel/:id', function(context) {
			$('#content').bookBooking({id: this.params['id'], context: context});
        }, 'components/booking/book.js');
        
		/**
		 * Booking confirmation page
		 */
		this.get('#/booking/confirm', function(context) {
			$('div#booking-form-fields').confirmBooking({context: context});
		}, 'components/booking/confirm.js');

		/**
		 * Delete booking
		 */
		this.get('#/booking/del/:id', function(context) {
			$('#content').deleteBooking({id: this.params['id'], context: context});
		}, 'components/booking/delete.js');
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
