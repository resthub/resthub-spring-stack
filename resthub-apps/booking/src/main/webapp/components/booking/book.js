(function($) {

var bookBooking =
{
	options: {
		hotelId: null,
		booking: {},
		template: 'components/booking/book.html',
		context: null,
		mode: 'edit'
	},
	_init: function() {

		this.element.render(this.options.template, {hotelId: this.options.hotelId});
		
		this.options.booking = this.options.context.session('booking');
		
		if(this.options.booking == undefined) {
			this._get('api/hotel/' + this.options.hotelId, this._initBookingData);
		} else {
			this._displayBookingView(this.options.booking);
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
		this._displayBookingView(this.options.booking);
	},
	_displayBookingView: function() {

		var self = this;
		dominoes('components/hotel/view.js', function() {
			$('#hotel-data').viewHotel({
				id: self.options.booking.hotel.id,
				only_data: true
			});
		});
		
		if(this.options.mode == 'edit') {
			this._switchToEdit();
		} else {
			this._switchToView();
		}
	},
	_switchToEdit: function() {

		var self = this;

		Sammy.log('Booking workflow : edit mode.');
		dominoes('components/booking/edit.js', function() {
			$('#booking-data').editBooking({
				booking: self.options.booking,
				context: self.options.context
			});
		});
	},
	_switchToView: function() {

		var self = this;

		Sammy.log('Booking workflow : confirmation mode.');
		dominoes('components/booking/view.js', function() {
			$('#booking-data').viewBooking({
				booking: self.options.booking,
				context: self.options.context,
				mode: 'confirm'
			});
		});
	}
};

$.widget("booking.bookBooking", $.resthub.resthubController, bookBooking);
})(jQuery);