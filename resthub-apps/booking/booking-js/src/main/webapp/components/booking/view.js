(function($) {

var viewBooking =
{
	options: {
		booking: {},
		template: 'components/booking/view.html',
		context: null,
		mode: 'view'
	},
	_init: function() {

		if(this.options.mode == 'view') {
			this._get('api/booking/' + this.options.booking.id, this._displayViewMode);
		} else {
			this._displayConfirmMode();
		}
	},
	_displayViewMode: function() {
		this.element.render(this.options.template, {
			booking: this.options.booking,
			mode: this.options.mode
		});
	},
	_displayConfirmMode: function() {
		this.options.booking = this.options.context.session('booking');
		var daysBetween = this.options.context.session('daysBetween');
		var total = daysBetween * this.options.booking.hotel.price;

		this.element.render(this.options.template, {
			booking: this.options.booking,
			total: total,
			mode: this.options.mode
		});

		$('#content h1:first').html("Confirm hotel booking");

		$('input#book-revise').unbind();
		$('input#book-revise').bind('click', $.proxy(this._reviseBooking, this));

		$('input#book-confirm').unbind();
		$('input#book-confirm').bind('click', $.proxy(this._sendBooking, this));
	},
	_reviseBooking: function() {
		this.options.context.redirect('#/booking/hotel', this.options.booking.hotel.id);
	},
	_sendBooking: function() {
		this._post('api/booking', this._endOfBooking, $.toJSON(this.options.booking));
	},
	/* Go back home page and trigger end-of-booking event */
	_endOfBooking: function(booking) {
		this.options.context.session('booking', booking);
		this.options.context.redirect('#/home');
		this.options.context.trigger('end-of-booking');
	}
};

$.widget("booking.viewBooking", $.resthub.resthubController, viewBooking);
})(jQuery);