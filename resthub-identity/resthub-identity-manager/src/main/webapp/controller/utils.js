define(['i18n!nls/labels', 'lib/jquery'], function (i18n) {
(function($){
	  	  
	/**
	 * All these functions are added to the jQuery namespace.
	 */
	
	// -------------------------------------------------------------------------------------------------------------
	// Functions

	/**
	 * Checks presence of a user in local storate.
	 * Empties the navigation bar, and in case of a current user, fills it with administration links if relevant.
	 * 
	 * If no current user is to be found, redirect to login.
	 */
	$.redirectIfNotLogged = function() {	
		var user = $.storage.get(Constants.USER_KEY);
		$('#navbar-content *').remove();
		if (user == null) {
			$.route("#/login");
		} else {
			if (user.permissions.indexOf('IM-ADMIN') != -1) {
				$('#navbar-content').append('<li><a href="#/home">'+i18n.labels.home+'</a></li>'+
						'<li><a href="#/manage-users">'+i18n.labels.usersManagement+'</a></li>'+
						'<li><a href="#/manage-groups">'+i18n.labels.groupsManagement+'</a></li>');
			}
		}
	}; // redirectIfNotLogged().
	
	/**
	 * Transforms a markup with class 'logout' in a logout button.
	 */
	$.connectLogoutButton = function() {
		$('.logout').button().click(function() {
			$.logout();
		});
	}; // connectLogoutButton().
	
	/**
	 * Logout function: remove the current user in local storage, and redirect to login route.
	 */
	$.logout = function() {
		$.loading(false);
		$.storage.remove(Constants.USER_KEY);
		// Reload entierly the application to avoid reuse of past data.
		$.route('');
	}; // logout().

	/**
	 * Displays or hides the loading indicator.
	 * 
	 * @param display True to display loading indicator, false to hide it.
	 */
	$.loading = function(display) {
		if (display) {
			$("#loading").show();
		} else {
			$("#loading").hide();
		}
	}; // loading().
	
})(jQuery);
});