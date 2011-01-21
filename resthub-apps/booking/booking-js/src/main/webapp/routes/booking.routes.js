define([ 'jquery.tinypubsub', 'route', 'booking/book', 'booking/delete' ], function() {

	/* BEGIN EVENTS */

	$.subscribe('end-of-booking', function() {
		var booking = $.storage.getJSONItem('booking');
		$.pnotify('Thank you, ' + booking.user.fullname
				+ ', your confirmation number for ' + booking.hotel.name
				+ ' is ' + booking.id + '.');
		$.storage.removeItem('booking');
	});

	$.subscribe('booking-deleted', function() {
		$.pnotify('Your booking has been deleted.');
		route('#/home').run();
	});

	/* END EVENTS */

	/**
	 * Book hotel identified by 'id'
	 */
	route('#/booking/hotel/:id').bind(function(params) {
		console.debug('#/booking/hotel/:id' + params.id);
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
	route('#/booking/del/:id').bind(function(params) {
		var options = { id : params.id };
		console.debug('#/booking/del/:id' + params.id);
		$('#content').deleteBooking(options);
	});

});
