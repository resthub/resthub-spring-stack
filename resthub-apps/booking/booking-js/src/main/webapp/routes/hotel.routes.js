define([ 'hotel/view' ], function() {

	/**
	 * View hotel
	 */
	$.route('#/hotel/:id', function(params) {
		$('#content').view_hotel({
			id : params.id
		});
	});

});
