define([ 'user/login', 'user/register', 'user/edit' ], function() {

	/* BEGIN EVENTS */
	
	$.subscribe('route-run', function() {
		var user = $.storage.get('user');
		$('#header').render('header.html', {
			user : user
		});
	});

	$.subscribe('user-registered', function(user) {
		$.pnotify('Your are now registered ' + user.fullname + ' !');
		$.route('#/');
	});

	$.subscribe('password-updated',  function() {
		$.pnotify('Your password has been updated.');
		$.route('#/home');
	});

	$.subscribe('user-logged-in', function() {
		var user = $.storage.get('user');
		$.pnotify('Welcome ' + user.fullname + ' !');
	});

	$.subscribe('user-logged-out', function() {
		$.pnotify('See ya !');
		$.storage.clear();
		$.route('#');
	});

	/* END EVENTS */

	/**
	 * User register
	 */
	$.route('#/register', function() {
		$('#content').user_register();
	});

	/**
	 * Update user
	 */
	$.route('#/settings', function() {
		$('#content').edit_user();
	});

	/**
	 * User logout
	 */
	$.route('#/logout', function() {
		$.publish('user-logged-out');
	});

});
