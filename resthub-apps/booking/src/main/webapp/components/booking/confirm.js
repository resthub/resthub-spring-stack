$.widget("booking.confirmBooking", {
    options: {
        data : {},
        template : 'components/booking/confirm.html',
        context : null
    },
    _create: function() {
		this.element.addClass('bd-booking-confirm');
    },
    _init: function() {
		this.element.find('h1:first').html("Confirm hotel booking");

		var booking = $.session.getJSONItem('booking');
        this.element.render(this.options.template, {booking: booking});
		
		this.element.find('#cancel-request').attr('href', '#');
    },
    destroy: function() {
        this.element.removeClass('bd-booking-confirm');
        $.Widget.prototype.destroy.call( this );
    }
});