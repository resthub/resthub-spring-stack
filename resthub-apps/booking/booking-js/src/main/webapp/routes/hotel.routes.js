define([ 'resthub.route', 'hotel/view' ], function() {

	/* BEGIN EVENTS */

	$.subscribe('hotel-search', function() {
		$('#content').home();
		$('#search-value').focus();
	});

	/* END EVENTS */

	/**
	 * View hotel
	 */
	$.route('#/hotel/:id', function(params) {
		$('#content').viewHotel({
			id : params.id
		});
	});

});
