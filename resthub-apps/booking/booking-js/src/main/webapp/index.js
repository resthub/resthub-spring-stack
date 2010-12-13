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

		/* BEGIN EVENTS */

		this.bind('run-route', function() {
			var user = this.session('user');
			$('#header').render('components/header.html', {user: user});
		});

		this.bind('end-of-booking', function() {
			var booking = this.session('booking');
			$.pnotify('Thank you, ' + booking.user.fullname + ', your confimation number for ' + booking.hotel.name + ' is ' + booking.id + '.');
			this.store('session').clear('booking');
		});

		this.bind('user-logged-in', function() {
			var user = this.session('user');
			$.pnotify('Welcome ' + user.fullname + ' !');
		});

		this.bind('user-logged-out', function() {
			$.pnotify('See ya !');
			this.store('session').clearAll();
			this.redirect('#/');
		});

		this.bind('hotel-search', function() {
			$('#content').home({context: this});
			$('#search-value').focus();
		});

		this.bind('user-registered', function(e, user) {
			$.pnotify('Your are now registered ' + user.fullname + ' !');
			this.redirect('#/');
		});

		this.bind('password-updated', function() {
			$.pnotify('Your password has been updated.');
			this.redirect('#/home');
		});

		this.bind('booking-deleted', function() {
			$.pnotify('Your booking has been deleted.');
			this.redirect('#/home');
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
			var booking = {hotel: {id: this.params['id']}};
			$('#content').bookBooking({booking: booking, context: context, mode: 'edit'});
        }, 'components/booking/book.js');

		/**
		 * Booking confirmation
		 */
		 this.get('#/booking/confirm', function(context) {
			$('#content').bookBooking({context: context, mode: 'view'});
        }, 'components/booking/book.js');

		/**
		 * Delete booking
		 */
		this.get('#/booking/del/:id', function(context) {
			$('#content').deleteBooking({id: this.params['id'], context: context});
		}, 'components/booking/delete.js');

		/**
		 * Update user
		 */
		this.get('#/settings', function(context) {
			$('#content').editUser({context: context});
		}, 'components/user/edit.js');
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
