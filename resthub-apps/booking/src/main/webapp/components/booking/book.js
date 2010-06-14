(function($) {

var bookBooking =
{
    options: {
		id: null,
        data : {},
        template : 'components/booking/book.html',
        context : null
    },
    _create: function() {
		this.element.addClass('bd-booking-book');
    },
    _init: function() {
		var hotelId = this.options.id;
		var context = this.options.context;

		this.element.find('h1:first').html("Book hotel");
		$('a#cancel-request').attr('href', '/#/hotel/'+ hotelId);
		$('a#cancel-request').html('Cancel');
		$('input#book-request').attr('value', 'Proceed');

		$('div#booking-form-fields').load('components/booking/book.html', function() {
			$('input[name=checkinDate]').datepicker({ dateFormat: 'yy-mm-dd' });
			$('input[name=checkoutDate]').datepicker({ dateFormat: 'yy-mm-dd' });
			$('form#booking-form').validate({errorElement: 'span'});
		});
		
		$('input#book-request').unbind();
		$('input#book-request').bind('click', function() {
			var validForm = $('form#booking-form').validate({errorElement: 'span'}).form();
			
			if (validForm) {
				
				var booking = context.session('booking');
				booking.checkinDate = $('input[name=checkinDate]').val();
				booking.checkoutDate = $('input[name=checkoutDate]').val();
				booking.beds = $('select[name=beds] option:selected').val();
				booking.smoking = ($('input:checked[name=smoking]').val() == 'true') ? true : false;
				booking.creditCard = $('input[name=creditCard]').val();
				booking.creditCardName = $('input[name=creditCardName]').val();
				booking.creditCardExpiryMonth = $('select[name=creditCardExpiryMonth] option:selected').val();
				booking.creditCardExpiryYear = $('select[name=creditCardExpiryYear] option:selected').val();
				context.session('booking', booking);
				
				context.redirect('#/booking/confirm');
			}
		});
    },
    destroy: function() {
        this.element.removeClass('bd-booking-book');
        $.Widget.prototype.destroy.call( this );
    }
};

$.widget("booking.bookBooking", $.resthub.resthubController, bookBooking);
})(jQuery);