(function($) {

var confirmBooking =
{
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

		var booking = this.options.context.session('booking');
        var daysBetween = this._daysBetween(booking.checkinDate, booking.checkoutDate);
		var total = daysBetween * booking.hotel.price;

		this.element.render(this.options.template, {booking: booking, total: total});
		
		$('#cancel-request').attr('href', '#/hotel/' + booking.hotel.id);
		$('input#book-request').attr('value', 'Confirm');
		$('<input id="book-revise" value="Revise" type="button">').insertAfter('input#book-request');

		var self = this;
		$('input#book-request').bind('click', function() {
			self._post('api/booking', self, '_endOfBooking', $.toJSON(booking));
		});
    },
	_daysBetween: function(checkinDate, checkoutDate) {

		try {
			var checkinDateTimestamp = $.datepicker.parseDate('yy-mm-dd', checkinDate).getTime();
			var checkoutDateTimestamp = $.datepicker.parseDate('yy-mm-dd', checkoutDate).getTime();
		} catch(err) {
			console.log('Bad date format...');
		}

		var secondsBetween = (checkoutDateTimestamp - checkinDateTimestamp) / 1000;
		return secondsBetween / 86400;
	},
	_endOfBooking: function(booking) {
		this.options.context.session('booking', booking);
		this.options.context.redirect('#/home');
		this.options.context.trigger('end-of-booking');
	},
    destroy: function() {
        this.element.removeClass('bd-booking-confirm');
        $.Widget.prototype.destroy.call( this );
    }
};

$.widget("booking.confirmBooking", $.resthub.resthubController, confirmBooking);
})(jQuery);