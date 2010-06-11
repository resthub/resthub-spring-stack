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
		$('h1:first').html("Confirm hotel booking");

		var booking = $.session.getJSONItem('booking');
        this.element.render(this.options.template, {booking: booking});
		
		$('#cancel-request').attr('href', '#/hotel/' + booking.hotelId);
		$('input#book-request').attr('value', 'Confirm');
		$('<input id="book-revise" value="Revise" type="button">').insertAfter('input#book-request');
    },
    destroy: function() {
        this.element.removeClass('bd-booking-confirm');
        $.Widget.prototype.destroy.call( this );
    }
});