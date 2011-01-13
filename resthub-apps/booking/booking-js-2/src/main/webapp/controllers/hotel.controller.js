define(['components/hotel/view'], function() {

HotelController = function(app) { with(app) {

		/* BEGIN EVENTS */
		
		bind('hotel-search', function() {
			$('#content').home({context: this});
			$('#search-value').focus();
		});
		
		/* END EVENTS */
		
		/**
		 * View hotel
		 */
		get('#/hotel/:id', function(context) {
			$('#content').viewHotel({id: this.params['id'], context: context});
        });
		
		
}};
});