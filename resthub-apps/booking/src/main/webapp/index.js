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
			dominoes("components/booking/list.js", function() {
				var user = $.session.getJSONItem('user', {});
				$.ajax({
					url: 'api/lucene/rebuild',
					dataType: 'json',
					type: 'POST'
				});
				$.ajax({
					url: 'api/booking/user/' + user.id,
					dataType: 'json',
					success: function(data) {
						console.log($.toJSON(data));
						$('#content').listBookings({
							data: data,
							context: context
						});
						
						$('#search-submit').bind('click', function() {
							var searchVal = $('#search-value').val();
							dominoes("components/hotel/list.js", function() {
								$.ajax({
									url: 'api/hotel/search?q=' + searchVal,
									dataType: 'json',
									success: function(data) {
										console.log('Hotel search...');
										$('#result').listHotels({
											data: data,
											context : context
										});
									},
									error: function() {
										$('#result').html('<span class="error">No results</span>');
									}
								})
							})
						});
					},
					error: function() {
						$("#content").html('<span class="error">Disconnected</span>');
					}
				})
			});
		});

		/*
		 * List bookings
		 */
		this.get('#/booking/list', function(context) {
			dominoes("components/booking/list.js", function() {
				$.ajax({
					url: 'api/booking/',
					dataType: 'json',
					success: function(data) {
						console.log($.toJSON(data));
						$('#content').listBookings({
							data: data,
							context: context
						});
					}
				});
			});
		});

		/*
		 * List hotels
		 */
		this.get('#/hotel/list', function() {
			dominoes("components/hotel/list.js", function() {
				$.ajax({
					url: 'api/hotel/',
					dataType: 'json',
					success: function(data) {
						console.log($.toJSON(data));
						$('#result').listHotels({
							data: data
						});
					}
				});
			});
		});

		/**
		 * View hotel
		 */
		 this.get('#/hotel/:id', function(context) {
			var id = this.params['id'];
            dominoes("components/hotel/view.js", function() {
                $.ajax({
                    url: 'api/hotel/' + id,
                    dataType: 'json',
                    success: function(data) {
                        $('#content').viewHotel({
							data: data,
							context: context
						});
                    }
                });
            });
        });

		/**
		 * Book hotel identified by 'id'
		 */
		 this.get('#/booking/hotel/:id', function(context) {
            $('#content h1:first').html("Book hotel");
			var id = this.params['id'];
			dominoes("components/booking/book.js", function() {
				$('#content').bookBooking({
					data: id,
					context: context
				});
			});
        });
        
		/**
		 * Save booking before confirmation page
		 */
		this.get('#/booking/confirm', function() {
			$('#content h1:first').html("Confirm hotel booking");
			dominoes("components/booking/confirm.js", function() {
				var booking = $.session.getJSONItem('booking');
				$('div#booking-form').confirmBooking({
					data: booking
				});
			});
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
