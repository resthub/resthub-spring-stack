define(['booking/book','booking/delete'], function() {

BookingRoutes = function(app) { with(app) {

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
		get('#/booking/hotel/:id', function(context) {
			var booking = {hotel: {id: this.params['id']}};
			$('#content').bookBooking({booking: booking, context: context, mode: 'edit'});
        });

		/**
		 * Booking confirmation
		 */
		get('#/booking/confirm', function(context) {
			$('#content').bookBooking({context: context, mode: 'view'});
        });

		/**
		 * Delete booking
		 */
		get('#/booking/del/:id', function(context) {
			$('#content').deleteBooking({id: self.params['id'], context: context});
		});
		
}};
});