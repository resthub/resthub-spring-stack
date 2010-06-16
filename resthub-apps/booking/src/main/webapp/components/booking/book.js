(function($) {

var bookBooking =
{
	options: {
		hotelId: null,
		template : 'components/booking/book.html',
		context : null
	},
	_init: function() {
		var booking = this.options.context.session('booking');
		if(booking == undefined) {
			this._get('api/hotel/' + this.options.hotelId, this, '_initBookingData');
		} else {
			this._displayBookingForm(booking.hotel);
		}
	},
	_initBookingData: function(hotel) {
		var booking = {
			user: this.options.context.session('user'),
			hotel: hotel
		};
		this.options.context.session('booking', booking);
		this._displayBookingForm(booking.hotel);
	},
	_displayBookingForm: function(hotel) {
		
		this.element.render(this.options.template, {hotel: hotel});
		$('#hotel-data').render('components/hotel/view.html', {hotel: hotel, display_buttons: false});
		
		$('#content h1:first').html("Book hotel");

		$('input[name=checkinDate]').datepicker({dateFormat: 'yy-mm-dd'});
		$('input[name=checkoutDate]').datepicker({dateFormat: 'yy-mm-dd'});
		$('form#booking-form').validate({errorElement: 'span'});

		$('input#book-proceed').bind('click', $.proxy(this._bookingProceed, this));
	},
	_bookingProceed: function() {

		var validForm = $('form#booking-form').validate({errorElement: 'span'}).form();
		
		if (validForm) {

			var booking = this.options.context.session('booking');
			booking.checkinDate = $('input[name=checkinDate]').val();
			booking.checkoutDate = $('input[name=checkoutDate]').val();
			booking.beds = $('select[name=beds] option:selected').val();
			booking.smoking = ($('input:checked[name=smoking]').val() == 'true') ? true : false;
			booking.creditCard = $('input[name=creditCard]').val();
			booking.creditCardName = $('input[name=creditCardName]').val();
			booking.creditCardExpiryMonth = $('select[name=creditCardExpiryMonth] option:selected').val();
			booking.creditCardExpiryYear = $('select[name=creditCardExpiryYear] option:selected').val();
			this.options.context.session('booking', booking);

			var daysBetween = this._daysBetween(booking.checkinDate, booking.checkoutDate);
			var total = daysBetween * booking.hotel.price;

			$('h1:first').html("Confirm hotel booking");
			$('#booking-data').render('components/booking/confirm.html', {booking: booking, total: total});

			$('#cancel-request').attr('href', '#/hotel/' + booking.hotel.id);
			$('<input id="book-revise" value="Revise" type="button">').insertAfter('input#book-proceed');

			$('input#book-proceed').attr('value', 'Confirm');
			$('input#book-proceed').unbind();
			$('input#book-proceed').bind('click', {booking: booking}, $.proxy(this._sendBooking, this));
		}
	},
	_sendBooking: function(event) {
		this._post('api/booking', this, '_endOfBooking', $.toJSON(event.data.booking));
	},
	_endOfBooking: function(booking) {
		this.options.context.session('booking', booking);
		this.options.context.redirect('#/home');
		this.options.context.trigger('end-of-booking');
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
	}
};

$.widget("booking.bookBooking", $.resthub.resthubController, bookBooking);
})(jQuery);