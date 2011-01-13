/**
 * Routes
 */
define(["sammy", "sammy.storage", "sammy.title", "jquery-ejs", "jquery.pnotify", "resthub.controller"], function() {
	
	var app = $.sammy(function() {

		this.use('Title');
		this.use('Session');

		/**
		 * Load app managed events
		 */

		/* BEGIN EVENTS */

		this.bind('run-route', function() {
			var user = this.session('user');
			$('#header').render('/components/header.html', {user: user});
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
				$('#content').render('/components/user/login.html', {});
			}
		});

		/**
		 * User authentication
		 */
		this.post('#/user/check', function(context) {
			require(['/components/user/login.js'], function() {
				$('#header').userLogin({context: context});
			});
		});

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
			require(['/components/user/register.js'], function() {
				$('#content').userRegister({context: context});
			});
		});

		/**
		 * Home page after authentication
		 */
		this.get('#/home', function(context) {
			require(['/components/home.js'], function() {
				$('#content').home({context: context});
			});
		});

		/**
		 * View hotel
		 */
		this.get('#/hotel/:id', function(context) {
			require(['/components/hotel/view.js'], function() {
				$('#content').viewHotel({id: this.params['id'], context: context});
			});
        });

		/**
		 * Book hotel identified by 'id'
		 */
		this.get('#/booking/hotel/:id', function(context) {
			require(['/components/booking/book.js'], function() {
				var booking = {hotel: {id: this.params['id']}};
				$('#content').bookBooking({booking: booking, context: context, mode: 'edit'});
			});
        });

		/**
		 * Booking confirmation
		 */
		this.get('#/booking/confirm', function(context) {
			require(['/components/booking/book.js'], function() {
				$('#content').bookBooking({context: context, mode: 'view'});
			});
        });

		/**
		 * Delete booking
		 */
		this.get('#/booking/del/:id', function(context) {
			require(['/components/booking/delete.js'], function() {
				$('#content').deleteBooking({id: this.params['id'], context: context});
			});
		});

		/**
		 * Update user
		 */
		this.get('#/settings', function(context) {
			require(['/components/user/edit.js'], function() {
				$('#content').editUser({context: context});
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
