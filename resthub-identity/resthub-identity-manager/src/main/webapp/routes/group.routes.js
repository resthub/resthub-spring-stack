define([
        'i18n!nls/labels',
        'controller/utils',
        'controller/group/manage',
        'lib/jquery/jquery.pnotify',
        'lib/jquery/jquery.sprintf'
    ], function(i18n) {

	// -------------------------------------------------------------------------------------------------------------
	// Routes
	
	/**
	 * #/manage-groups route.
	 * Displays groups management controller.
	 */
	$.route('#/manage-groups', function() {
		$.redirectIfNotLogged();
		$('#wrapper').group_manage();
	});

}); 
