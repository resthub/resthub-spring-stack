define(['hotel/view'], function() {

return function(app) { with(app) {

		/* BEGIN EVENTS */
		
		bind('hotel-search', function(cx) {
			$('#content').home({cx: cx});
			$('#search-value').focus();
		});
		
		/* END EVENTS */
		
		/**
		 * View hotel
		 */
		get('#/hotel/:id', function(cx) {
			$('#content').viewHotel({id: this.params['id'], cx: cx});
        });
		
		
}};
});
