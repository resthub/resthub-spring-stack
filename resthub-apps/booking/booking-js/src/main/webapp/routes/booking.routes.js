define(['booking/book','booking/delete'], function() {

BookingRoutes = function(app) { with(app) {

		/* BEGIN EVENTS */
		
		$.subscribe('end-of-booking', function() {
			var booking = $.storage.getJSONItem('booking');
			$.pnotify('Thank you, ' + booking.user.fullname + ', your confirmation number for ' + booking.hotel.name + ' is ' + booking.id + '.');
			$.storage.removeItem('booking');
		});
		
		$.subscribe('booking-deleted', function() {
			$.pnotify('Your booking has been deleted.');
			$('#content').home();
		});
		
		/* END EVENTS */
		
		/**
		 * Book hotel identified by 'id'
		 */
		get('#/booking/hotel/:id', function() {
			var booking = {hotel: {id: this.params['id']}};
			$('#content').bookBooking({booking: booking, mode: 'edit'});
        });

		/**
		 * Booking confirmation
		 */
		get('#/booking/confirm', function() {
			$('#content').bookBooking({mode: 'view'});
        });

		/**
		 * Delete booking
		 */
		get('#/booking/del/:id', function() {
			$('#content').deleteBooking({id: this.params['id']});
		});
		
}};
});