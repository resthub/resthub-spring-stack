define([ 'jquery.tinypubsub', 'route', 'user/login', 'user/register', 'user/edit' ], function() {

	/* BEGIN EVENTS */
	
	$.subscribe('run-route', function() {
		var user = $.storage.getJSONItem('user');
		$('#header').render('header.html', {
			user : user
		});
	});

	$.subscribe('user-registered', function() {
		$.pnotify('Your are now registered ' + user.fullname + ' !');
		route('#').run();
	});

	$.subscribe('password-updated',  function() {
		$.pnotify('Your password has been updated.');
		route('#/home').run();
	});

	$.subscribe('user-logged-in', function() {
		var user = $.storage.getJSONItem('user');
		$.pnotify('Welcome ' + user.fullname + ' !');
	});

	$.subscribe('user-logged-out', function() {
		$.pnotify('See ya !');
		this.storage.clear();
		route('#').run();
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
