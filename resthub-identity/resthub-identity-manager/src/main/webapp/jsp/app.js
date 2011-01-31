define(['lib/resthub',
        'lib/jqueryui/button'], function(){	
		
	$(document).ready(function() {
		$('#formLogin input[type=submit]').button();
	});
});