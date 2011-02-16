define([
        'controller/user/utils',
        'controller/user/login',
        'controller/user/home',
        'controller/user/manage',
        'lib/jquery/jquery.pnotify'
    ], function() {

	// -------------------------------------------------------------------------------------------------------------
	// Events

	/**
	 * When user is logged, display a welcome notification
	 */
	$.subscribe('user-logged-in', function() {
		var user = $.storage.get(Constants.USER_KEY);
		$.pnotify('Welcome ' + user.firstName + ' ' + user.lastName + ' !');
	});

	// -------------------------------------------------------------------------------------------------------------
	// Routes

	/**
	 * #/login route.
	 * Displays login controller and removes previously stored user.
	 */
	$.route('#/login', function() {
		$.storage.remove(Constants.USER_KEY);
		$('#navbar-content *').remove();
		$('#wrapper').login();
	});

	/**
	 * #/home route.
	 * Displays home controller
	 */
	$.route('#/home', function() {
		$.redirectIfNotLogged();
		$('#wrapper').home();
	});
	
	/**
	 * #/manage-users route.
	 * Displays users management controller.
	 */
	$.route('#/manage-users', function() {
		$.redirectIfNotLogged();
		$('#wrapper').manage();
	});

}); 
