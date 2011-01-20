define([ 'user/login', 'user/register', 'user/edit' ], function() {

	/* BEGIN EVENTS */

	route('user-registered').bind(function() {
		$.pnotify('Your are now registered ' + user.fullname + ' !');
		location.hash = '#/';
	});

	route('password-updated').bind(function() {
		$.pnotify('Your password has been updated.');
		location.hash = '#/home';
	});

	route('user-logged-in').bind(function() {
		var user = this.session('user');
		$.pnotify('Welcome ' + user.fullname + ' !');
	});

	route('user-logged-out').bind(function() {
		$.pnotify('See ya !');
		this.store('session').clearAll();
		location.hash = '#/';
	});

	/* END EVENTS */

	/**
	 * User register
	 */
	route('#/register').bind(function() {
		$('#content').userRegister();
	});

	/**
	 * Update user
	 */
	route('#/settings', function() {
		$('#content').editUser();
	});

	/**
	 * User logout
	 */
	route('#/logout', function() {
		$.publish('user-logged-out');
	});

});
