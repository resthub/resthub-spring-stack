define(['user/login','user/register', 'user/edit'], function() {
return function(app) { with(app) {

		/* BEGIN EVENTS */
		
		bind('run-route', function() {
			var user = this.session('user');
			$('#header').render('header.html', {user: user});
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
		get('#/register', function(cx) {
			$('#content').userRegister({cx: cx});
		});
		
		/**
		 * User authentication
		 */
		post('#/user/check', function(cx) {
			$('#header').userLogin({cx: cx});	
		});
		
		/**
		 * Update user
		 */
		get('#/settings', function(cx) {
			$('#content').editUser({cx: cx});
		});

		/**
		 * User logout
		 */
		get('#/logout', function(cx) {
			cx.trigger('user-logged-out');
		});
		
}};
});
