define([ 'booking/book', 'booking/delete' ], function() {

	/* BEGIN EVENTS */

	$.subscribe('end-of-booking', function() {
		var booking = $.storage.get('booking');
		$.pnotify('Thank you, ' + booking.user.fullname
				+ ', your confirmation number for ' + booking.hotel.name
				+ ' is ' + booking.id + '.');
		$.storage.remove('booking');
	});

	$.subscribe('booking-deleted', function() {
		$.pnotify('Your booking has been deleted.');
		$.route('#/home');
	});

	/* END EVENTS */

	/**
	 * Book hotel identified by 'id'
	 */
	$.route('#/booking/hotel/:id', function(params) {
		console.debug('#/booking/hotel/:id' + params.id);
		var booking = {
			hotel : {
				id : params.id
			}
		};
		$('#content').book_booking({
			booking : booking,
			mode : 'edit'
		});
	});

	/**
	 * Booking confirmation
	 */
	$.route('#/booking/confirm', function() {
		$('#content').book_booking({
			mode : 'view'
		});
	});

	/**
	 * Delete booking
	 */
	$.route('#/booking/del/:id', function(params) {
		console.debug('#/booking/del/:id' + params.id);
		$('#content').delete_booking(params);
	});

});
