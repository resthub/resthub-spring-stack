define([ 'booking/book', 'booking/delete' ], function() {

	/* BEGIN EVENTS */

	$.subscribe('end-of-booking', function() {
		var booking = this.session('booking');
		$.pnotify('Thank you, ' + booking.user.fullname
				+ ', your confirmation number for ' + booking.hotel.name
				+ ' is ' + booking.id + '.');
		$.storage.removeItem('booking');
	});

	$.subscribe('booking-deleted', function() {
		$.pnotify('Your booking has been deleted.');
		location.hash = '#/home';
	});

	/* END EVENTS */

	/**
	 * Book hotel identified by 'id'
	 */
	route('#/booking/hotel/:id').bind(function(params) {
		var booking = {
			hotel : {
				id : params.id
			}
		};
		$('#content').bookBooking({
			booking : booking,
			mode : 'edit'
		});
	});

	/**
	 * Booking confirmation
	 */
	route('#/booking/confirm').bind(function() {
		$('#content').bookBooking({
			mode : 'view'
		});
	});

	/**
	 * Delete booking
	 */
	route('#/booking/del/:id').bind(function() {
		$('#content').deleteBooking({
			id : params.id
		});
	});

});
