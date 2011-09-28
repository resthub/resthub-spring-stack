define([ 'text!header.html', 'lib/oauth2client', 'user/login', 'user/register', 'user/edit' ], function( tmpl, OAuth2Client ) {

	/* BEGIN EVENTS */
	
	$.subscribe('route-run', function() {
		var user = $.storage.get('user');		
                $('#header').empty().append($.tmpl(tmpl, {user : user}));
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
		$.pnotify('Welcome ' + user.username + ' !');
	});

	$.subscribe('user-logged-out', function() {
		$.pnotify('See ya !');
		OAuth2Client.logout();
		$.storage.set('user', null);
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
