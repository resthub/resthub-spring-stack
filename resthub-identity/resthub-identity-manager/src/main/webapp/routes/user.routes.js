define([
        'controller/user/utils',
        'controller/user/login',
        'controller/user/home',
        'controller/user/manage',
        'lib/jquery/jquery.pnotify'
    ], function() {

	$.subscribe('user-logged-in', function() {
		var user = $.storage.get(Constants.USER_KEY);
		$.pnotify('Welcome ' + user.firstName + ' ' + user.lastName + ' !');
	});

	$.route('#/login', function() {
		$.storage.remove(Constants.USER_KEY);
		$('#navbar-content *').remove();
		$('#wrapper').login();
	});

	$.route('#/home', function() {
		$.redirectIfNotLogged();
		$('#wrapper').home();
	});
	
	$.route('#/manage-users', function() {
		$.redirectIfNotLogged();
		$('#wrapper').manage();
	});

});
