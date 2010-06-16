/**
 * Routes
 */
(function($) {
	
	var app = $.sammy(function() {

		this.use(Sammy.Title);
		this.use(Sammy.Session);

		/**
		 * Load app managed events
		 */
		// $.get('events.js');

		/* BEGIN EVENTS */

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
			this.store('session').clearAll();
			this.redirect('#/');
		});

		this.bind('hotel-search', function() {
			$('#content').home({context: this});
		});

		this.bind('user-registered', function(e, user) {
			$.pnotify('Your are now registered ' + user.name + ' !');
			this.redirect('#/');
		});

		/* END EVENTS */

		/**
		 * Login page
		 */
		this.get('#/', function(context) {
			if(this.session('user') != null) {
				context.redirect('#/home');
			} else {
				this.title('Login');
				$('#content').render('components/user/login.html', {})
			}
		});

		/**
		 * User authentication
		 */
		this.post('#/user/check', function(context) {
			$('#header').userLogin({context: context});
		}, 'components/user/login.js');

		/**
		 * User logout
		 */
		this.get('#/logout', function(context) {
			context.trigger('user-logged-out');
		});

		/**
		 * User register
		 */
		this.get('#/register', function(context) {
			$('#content').userRegister({context: context});
		}, 'components/user/register.js');

		/**
		 * Home page after authentication
		 */
		this.get('#/home', function(context) {
			$('#content').home({context: context});
		}, 'components/home.js');

		/**
		 * View hotel
		 */
		this.get('#/hotel/:id', function(context) {
			$('#content').viewHotel({id: this.params['id'], context: context});
        }, 'components/hotel/view.js');

		/**
		 * Book hotel identified by 'id'
		 */
		 this.get('#/booking/hotel/:id', function(context) {
			$('#content').bookBooking({hotelId: this.params['id'], context: context});
        }, 'components/booking/book.js');

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
