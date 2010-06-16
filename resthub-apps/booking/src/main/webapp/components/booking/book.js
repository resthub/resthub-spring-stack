(function($) {

var bookBooking =
{
	options: {
		hotelId: null,
		booking: {},
		templates : {
			book: 'components/booking/book.html',
			form: 'components/booking/form.html',
			view: 'components/booking/view.html',
			hotel: 'components/hotel/view.html'
		},
		context : null
	},
	_init: function() {
		this.options.booking = this.options.context.session('booking');
		if(this.options.booking == undefined) {
			this._get('api/hotel/' + this.options.hotelId, this, '_initBookingData');
		} else {
			this._displayHotelView(this.options.booking.hotel);
		}
	},
	/**
	 * If there is no booking in session when this widget starts, this function
	 * creates a booking with user and hotel...
	 */
	_initBookingData: function(hotel) {
		this.options.booking = {
			user: this.options.context.session('user'),
			hotel: hotel
		};
		this.options.context.session('booking', this.options.booking);
		this._displayHotelView(this.options.booking.hotel);
	},
	_displayHotelView: function() {
		this.element.render(this.options.templates.book, {hotel: this.options.booking.hotel});
		$('#hotel-data').render(this.options.templates.hotel, {hotel: this.options.booking.hotel, only_data: true});
		this._displayBookingForm();
	},
	/* Display the booking form with potential values from session (ie. after revise button click) */
	_displayBookingForm: function() {
		$('#booking-data').render(this.options.templates.form);
		$('#content h1:first').html("Book hotel");

		$('input#book-revise').attr('style', 'display: none');
		this._sessionToForm();

		$('input[name=checkinDate]').datepicker({dateFormat: 'yy-mm-dd'});
		$('input[name=checkoutDate]').datepicker({dateFormat: 'yy-mm-dd'});
		$('form#booking-form').validate({errorElement: 'span'});

		$('input#book-proceed').attr('value', 'Proceed');
		$('input#book-proceed').unbind();
		$('input#book-proceed').bind('click', $.proxy(this._bookingProceed, this));
	},
	/* Check form data, calculate final price and display data before confirmation */
	_bookingProceed: function() {
		
		var validForm = $('form#booking-form').validate({errorElement: 'span'}).form();

		if (validForm) {
			
			var daysBetween = this._daysBetween();
			/* Valid dates and checkinDate > checkoutDate */
			if (daysBetween) {

				this._formToSession();
				
				var total = daysBetween * this.options.booking.hotel.price;

				$('#content h1:first').html("Confirm hotel booking");
				$('#booking-data').render(this.options.templates.view, {booking: this.options.booking, total: total});

				$('input#book-revise').attr('style', '');
				$('input#book-revise').bind('click', $.proxy(this._displayBookingForm, this));

				$('input#book-proceed').attr('value', 'Confirm');
				$('input#book-proceed').unbind();
				$('input#book-proceed').bind('click', $.proxy(this._sendBooking, this));
			}
		}
	},
	/* Put form data in session */
	_formToSession: function() {
		var booking = this.options.context.session('booking');
		booking.checkinDate = $('input[name=checkinDate]').val();
		booking.checkoutDate = $('input[name=checkoutDate]').val();
		booking.beds = $('select[name=beds] option:selected').val();
		booking.smoking = ($('input[name=smoking]:checked').val() == 'true') ? true : false;
		booking.creditCard = $('input[name=creditCard]').val();
		booking.creditCardName = $('input[name=creditCardName]').val();
		booking.creditCardExpiryMonth = $('select[name=creditCardExpiryMonth] option:selected').val();
		booking.creditCardExpiryYear = $('select[name=creditCardExpiryYear] option:selected').val();
		this.options.context.session('booking', booking);
		this.options.booking = booking;
	},
	/* Display session data in booking form (after reload or revise button click) */
	_sessionToForm: function() {
		var booking = this.options.context.session('booking');
		$('input[name=checkinDate]').val(booking.checkinDate);
		$('input[name=checkoutDate]').val(booking.checkoutDate);
		$('select[name=beds] option[value='+ booking.beds +']').attr('selected', 'selected');
		$('input[name=smoking][value='+ booking.smoking +']').attr('checked', 'checked');
		$('input[name=creditCard]').val(booking.creditCard);
		$('input[name=creditCardName]').val(booking.creditCardName);
		$('select[name=creditCardExpiryMonth] option[value='+ booking.creditCardExpiryMonth +']').attr('selected', 'selected');
		$('select[name=creditCardExpiryYear] option[value='+ booking.creditCardExpiryYear +']').attr('selected', 'selected');
	},
	_sendBooking: function() {
		this._post('api/booking', this, '_endOfBooking', $.toJSON(this.options.booking));
	},
	/* Go back home page and trigger end-of-booking event */
	_endOfBooking: function(booking) {
		this.options.context.session('booking', booking);
		this.options.context.redirect('#/home');
		this.options.context.trigger('end-of-booking');
	},
	/* Calculate the number of days between checkinDate and checkoutDate */
	_daysBetween: function() {

		var checkinDate = this.options.booking.checkinDate;
		var checkoutDate = this.options.booking.checkoutDate

		if(checkinDate >= checkoutDate) { return false; }

		try {
			var checkinDateTimestamp = $.datepicker.parseDate('yy-mm-dd', checkinDate).getTime();
			var checkoutDateTimestamp = $.datepicker.parseDate('yy-mm-dd', checkoutDate).getTime();
		} catch(err) {
			return false;
		}

		var secondsBetween = (checkoutDateTimestamp - checkinDateTimestamp) / 1000;
		return secondsBetween / 86400;
	}
};

$.widget("booking.bookBooking", $.resthub.resthubController, bookBooking);
})(jQuery);