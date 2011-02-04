define(['lib/jquery'], function () {
(function($){
	  	  
	$.redirectIfNotLogged = function() {	
		var user = $.storage.get(Constants.USER_KEY);
		$('#navbar-content *').remove();
		if (user == null) {
			$.route("#/login");
		} else {
			if (user.permissions.indexOf('IM-ADMIN') != -1) {
				$('#navbar-content').append('<li><a href="#/home">Home</a></li>'+
						'<li><a href="#/manage-users">Users Management</a></li>');
			}
		}
	};
	
	$.connectLogoutButton = function() {
		$('.logout').button().click(function() {
			$.logout();
		});
	};
	
	$.logout = function() {
		$.storage.remove(Constants.USER_KEY);
		$.route('#/login');
	};

})(jQuery);
});