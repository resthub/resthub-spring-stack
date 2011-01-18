define([ 'jquery.controller', 'models/booking.model' ], function() {
	(function($) {

		$.widget("booking.deleteBooking", $.ui.controller, {
			options : {
				id : null
			},
			_init : function() {
				Booking.remove($.proxy(this, '_bookingDeleted'), this.options.id);
			},
			_bookingDeleted : function() {
				this.cx().trigger('booking-deleted');
			}
		});
	})(jQuery);
});