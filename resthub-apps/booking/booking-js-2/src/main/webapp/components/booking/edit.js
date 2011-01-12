(function($) {

var editBooking =
{
	options: {
		booking: {},
		template: 'components/booking/edit.html',
		context: null
	},
	_init: function() {
		this._displayBookingForm();
	},
	/* Display the booking form with potential values from session (ie. after revise button click) */
	_displayBookingForm: function() {

		this.element.render(this.options.template, {booking: this.options.booking});

		$('#content h1:first').html("Book hotel");

		this._sessionToForm();

		$('input[name=checkinDate]').datepicker({dateFormat: 'yy-mm-dd'});
		$('input[name=checkoutDate]').datepicker({dateFormat: 'yy-mm-dd'});
		$('form#booking-form').validate({errorElement: 'span'});
		
		$('input#book-proceed').unbind();
		$('input#book-proceed').bind('click', $.proxy(this._bookingProceed, this));
	},
	/* Check form data, calculate final price and display data before confirmation */
	_bookingProceed: function() {

		var validForm = $('form#booking-form').validate({errorElement: 'span'}).form();
		if (validForm) {

			this._formToSession();
			var daysBetween = this._daysBetween();
			
			// Valid dates and checkinDate > checkoutDate
			if (daysBetween) {
				this.options.context.session('daysBetween', daysBetween);
				this.options.context.redirect('#/booking/confirm');
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
		booking.creditCardType = $('select[name=creditCardType] option:selected').val();
		booking.creditCardNumber = $('input[name=creditCardNumber]').val();
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
		$('select[name=creditCardType] option[value='+ booking.creditCardType +']').attr('selected', 'selected');
		$('input[name=creditCardNumber]').val(booking.creditCardNumber);
		$('input[name=creditCardName]').val(booking.creditCardName);
		$('select[name=creditCardExpiryMonth] option[value='+ booking.creditCardExpiryMonth +']').attr('selected', 'selected');
		$('select[name=creditCardExpiryYear] option[value='+ booking.creditCardExpiryYear +']').attr('selected', 'selected');
	},
	/* Calculate the number of days between checkinDate and checkoutDate */
	_daysBetween: function() {

		var checkinDate = this.options.booking.checkinDate;
		var checkoutDate = this.options.booking.checkoutDate;

		if(checkinDate >= checkoutDate) { return false; }

		try {
			var checkinDateTimestamp = $.datepicker.parseDate('yy-mm-dd', checkinDate).getTime();
			var checkoutDateTimestamp = $.datepicker.parseDate('yy-mm-dd', checkoutDate).getTime();
		} catch(err) {
			Sammy.log('function _daysBetween : error (' + err + ')');
			return false;
		}

		var secondsBetween = (checkoutDateTimestamp - checkinDateTimestamp) / 1000;
		return secondsBetween / 86400;
	}
};

$.widget("booking.editBooking", $.resthub.resthubController, editBooking);
})(jQuery);