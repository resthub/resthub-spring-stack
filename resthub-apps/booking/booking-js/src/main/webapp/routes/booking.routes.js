define(['booking/book','booking/delete'], function() {

return function(app) { with(app) {

		/* BEGIN EVENTS */
		
		bind('end-of-booking', function() {
			var booking = this.session('booking');
			$.pnotify('Thank you, ' + booking.user.fullname + ', your confirmation number for ' + booking.hotel.name + ' is ' + booking.id + '.');
			this.store('session').clear('booking');
		});
		
		bind('booking-deleted', function() {
			$.pnotify('Your booking has been deleted.');
			this.redirect('#/home');
		});
		
		/* END EVENTS */
		
		/**
		 * Book hotel identified by 'id'
		 */
		get('#/booking/hotel/:id', function(cx) {
			var booking = {hotel: {id: this.params['id']}};
			$('#content').bookBooking({booking: booking, cx: cx, mode: 'edit'});
        });

		/**
		 * Booking confirmation
		 */
		get('#/booking/confirm', function(cx) {
			$('#content').bookBooking({cx: cx, mode: 'view'});
        });

		/**
		 * Delete booking
		 */
		get('#/booking/del/:id', function(cx) {
			$('#content').deleteBooking({id: this.params['id'], cx: cx});
		});
		
}};
});
