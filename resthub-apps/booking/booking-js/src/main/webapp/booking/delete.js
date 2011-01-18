define([ 'jquery', 'jquery.controller', 'models/booking.dao' ], function($, Controller, BookingDao) {
	return $.widget("booking.deleteBooking", $.ui.controller, {
		options : {
			id : null
		},
		_init : function() {
			BookingDao.remove($.proxy(this, '_bookingDeleted'), this.options.id);
		},
		_bookingDeleted : function() {
			this.cx().trigger('booking-deleted');
		}
	});
});
