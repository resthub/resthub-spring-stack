$.widget("booking.bookBooking", {
    options: {
        data : {},
        template : 'components/booking/book.html',
        context : null
    },
    _create: function() {
		this.element.addClass('bd-booking-book');
    },
    _init: function() {

		var id = this.options.data;
		var context = this.options.context;

		$('a#cancel-request').attr('href', '/#/hotel/'+ id);
		$('a#cancel-request').html('Cancel');
		$('input#book-request').attr('value', 'Proceed');
		$('div#booking-form').load('components/booking/book.html');
		$('input#book-request').bind('click', function() {
			//console.log();
			var booking = {
				hotelId: id,
				checkinDate: $('input[name=checkinDate]').val(),
				checkoutDate: $('input[name=checkoutDate]').val(),
				beds: $('input:selected[name=beds]').val(),
				total: 1600,
				smoking: $('input:checked[name=smoking]').val(),
				creditCard: $('input[name=creditCard]').val(),
				creditCardName: $('input[name=creditCardName]').val(),
				creditCardExpiryMonth: $('input[name=creditCardExpiryMonth]').val(),
				creditCardExpiryYear: $('input[name=creditCardExpiryYear]').val()
			}
			$.session.setJSONItem('booking', booking);
			console.log(booking);
			context.redirect('#/booking/confirm');
		});
    },
    destroy: function() {
        this.element.removeClass('bd-booking-book');
        $.Widget.prototype.destroy.call( this );
    }
});