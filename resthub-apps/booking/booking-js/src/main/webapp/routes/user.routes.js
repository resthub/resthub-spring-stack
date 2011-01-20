define(['user/login','user/register', 'user/edit'], function() {

UserRoutes = function(app) { with(app) {

		/* BEGIN EVENTS */
		
		bind('run-route', function() {
			var user = $.storage.getJSONItem('user');
			$('#header').render('header.html', {user: user});
		});
		
		$.subscribe('user-registered', function(e, user) {
			$.pnotify('Your are now registered ' + user.fullname + ' !');
			location.hash = '#/';
		});

		$.subscribe('password-updated', function() {
			$.pnotify('Your password has been updated.');
			location.hash = '#/home';
		});
		
		$.subscribe('user-logged-in', function() {
			var user = $.storage.getJSONItem('user');
			$.pnotify('Welcome ' + user.fullname + ' !');
		});

		$.subscribe('user-logged-out', function() {
			$.pnotify('See ya !');
			$.storage.clear();
			location.hash = '#/';
		});
		
		/* END EVENTS */
		
		/**
		 * User register
		 */
		get('#/register', function() {
			$('#content').userRegister();
		});
		
		/**
		 * User authentication
		 */
		post('#/user/check', function() {
			var user = {
					username: this.params['username'],
					password: this.params['password']
			};
			$('#header').userLogin({user: user});	
		});
		
		/**
		 * Update user
		 */
		get('#/settings', function() {
			$('#content').editUser();
		});

		/**
		 * User logout
		 */
		get('#/logout', function() {
			$.publish('user-logged-out');
		});
		
}};
});