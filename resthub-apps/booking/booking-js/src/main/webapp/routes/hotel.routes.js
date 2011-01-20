define(['hotel/view'], function() {

HotelRoutes = function(app) { with(app) {

		/* BEGIN EVENTS */
		
		$.subscribe('hotel-search', function() {
			$('#content').home();
			$('#search-value').focus();
		});
		
		/* END EVENTS */
		
		/**
		 * View hotel
		 */
		get('#/hotel/:id', function() {
			$('#content').viewHotel({id: this.params['id']});
        });
		
		
}};
});