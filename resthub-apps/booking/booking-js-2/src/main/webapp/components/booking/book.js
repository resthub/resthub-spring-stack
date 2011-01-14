define(["components/booking/view", "components/booking/edit"], function() {
(function($) {
	$.widget("booking.bookBooking", $.ui.controller, {
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
		
		$('#hotel-data').viewHotel({
			id: self.options.booking.hotel.id,
			only_data: true
		});
	
		
		if(this.options.mode == 'edit') {
			this._switchToEdit();
		} else {
			this._switchToView();
		}
	},
	_switchToEdit: function() {

		Sammy.log('Booking workflow : edit mode.');
		
		$('#booking-data').editBooking({
			booking: this.options.booking,
			context: this.options.context
		});
		
	},
	_switchToView: function() {
		var self = this;
		Sammy.log('Booking workflow : confirmation mode.');
		$('#booking-data').viewBooking({
			booking: self.options.booking,
			context: self.options.context,
			mode: 'confirm'
		});
	}
});
})(jQuery);
});