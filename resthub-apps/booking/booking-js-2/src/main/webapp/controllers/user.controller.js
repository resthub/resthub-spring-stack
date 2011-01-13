define(['components/user/login','components/user/register', 'components/user/edit'], function() {

UserController = function(app) { with(app) {

		/* BEGIN EVENTS */
		
		bind('run-route', function() {
			var user = this.session('user');
			$('#header').render('/components/header.html', {user: user});
		});
		
		bind('user-registered', function(e, user) {
			$.pnotify('Your are now registered ' + user.fullname + ' !');
			this.redirect('#/');
		});

		bind('password-updated', function() {
			$.pnotify('Your password has been updated.');
			this.redirect('#/home');
		});
		
		bind('user-logged-in', function() {
			var user = this.session('user');
			$.pnotify('Welcome ' + user.fullname + ' !');
		});

		bind('user-logged-out', function() {
			$.pnotify('See ya !');
			this.store('session').clearAll();
			this.redirect('#/');
		});
		
		/* END EVENTS */
		
		/**
		 * User register
		 */
		get('#/register', function(context) {
			$('#content').userRegister({context: context});
		});
		
		/**
		 * User authentication
		 */
		post('#/user/check', function(context) {
			$('#header').userLogin({context: context});	
		});
		
		/**
		 * Update user
		 */
		get('#/settings', function(context) {
			$('#content').editUser({context: context});
		});

		/**
		 * User logout
		 */
		get('#/logout', function(context) {
			context.trigger('user-logged-out');
		});
		
}};
});