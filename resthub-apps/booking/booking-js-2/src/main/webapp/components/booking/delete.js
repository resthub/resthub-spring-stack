(function($) {

var deleteBooking =
{
    options: {
        id : null,
        context : null
    },
    _init: function() {
		this._delete('api/booking/' + this.options.id, this._bookingDeleted);
    },
	_bookingDeleted: function() {
		this.options.context.trigger('booking-deleted');
	}
};

$.widget("booking.deleteBooking", $.resthub.resthubController, deleteBooking);
})(jQuery);