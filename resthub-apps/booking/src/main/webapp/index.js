/**
 * Routes
 */
(function($) {
	
	var app = $.sammy(function() {

		this.use(Sammy.Title);

		this.get('#/', function() {
			this.title('Login');
			$('#header').render('components/header.html', {user: $.session.getJSONItem('user')});
			$('#content').render('components/login.html', {})
		});

		this.get('#/logout', function(context) {
			$.session.clear();
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
			var user = $.session.getJSONItem('user', {});
			$.executeRoute({bloc: '#content', url: 'api/booking/user/' + user.id, type: 'get', format: 'json', jsFile: 'components/booking/list.js', callbackFunction: 'listBookings', context: context});
		});

		/*
		 * Search hotels
		 */
		this.get('#/hotel/search', function() {
			var searchVal = this.params['q'];
			$.executeRoute({bloc: '#result', url: 'api/hotel/search?q=' + searchVal, type: 'get', format: 'json', jsFile: 'components/hotel/list.js', callbackFunction: 'listHotels'});
		});

		/*
		 * List bookings
		 */
		this.get('#/booking/list', function() {
			$.executeRoute({bloc: '#content', url: 'api/booking', type: 'get', format: 'json', jsFile: 'components/booking/list.js', callbackFunction: 'listBookings'});
		});

		/*
		 * List hotels
		 */
		this.get('#/hotel/list', function() {
			$.executeRoute({bloc: '#result', url: 'api/hotel', type: 'get', format: 'json', jsFile: 'components/hotel/list.js', callbackFunction: 'listHotels'});
		});

		/**
		 * View hotel
		 */
		this.get('#/hotel/:id', function(context) {
			var id = this.params['id'];
			$.executeRoute({bloc: '#content', url: 'api/hotel/'+id, type: 'get', format: 'json', jsFile: 'components/hotel/view.js', callbackFunction: 'viewHotel', context: context});
        });

		/**
		 * Book hotel identified by 'id'
		 */
		 this.get('#/booking/hotel/:id', function(context) {
			var id = this.params['id'];
			$.executeRoute({bloc: '#content', jsFile: 'components/booking/book.js', callbackFunction: 'bookBooking', data: id, context: context});
        });
        
		/**
		 * Booking confirmation page
		 */
		this.get('#/booking/confirm', function() {
			$.executeRoute({bloc: 'div#booking-form', jsFile: 'components/booking/confirm.js', callbackFunction: 'confirmBooking'});
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
			$.session.clear();
			var user = {
				username: this.params['username'],
				password: this.params['password'],
				name: ''
			}
			$.ajax({
				url: 'api/user/check/',
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				dataType: 'json',
				data: $.toJSON(user),
				processData: false,
				success: function(user) {
					$.session.setJSONItem('user', user);
					// TODO replace with a less intrusive strategy based on listening events
					$('#header').render('components/header.html', {user: $.session.getJSONItem('user')});
					$.ajax({
						url: 'api/lucene/rebuild',
						dataType: 'json',
						type: 'POST'
					});
					context.redirect('#/home');
				},
				error: function() {
					$("#formLogin p.messages").html('<span class="error">Bad credentials</span>');
				}
			});
		});
	});

	$(function() {
		$.session.setJSONItem('user', {});
		app.run('#/');
	});
})(jQuery);
