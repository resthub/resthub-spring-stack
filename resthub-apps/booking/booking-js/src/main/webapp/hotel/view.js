define(['jquery.controller'], function() {
(function($) {

$.widget("booking.viewHotel", $.ui.controller, {
	options: {
		id: null,
		template: 'hotel/view.html',
		context: null,
		only_data: false
	},
	_init: function() {
		if(!isNaN(this.options.id)) {
			this._get('api/hotel/' + this.options.id, this._displayHotel);
		}
	},
	_displayHotel: function(hotel) {
		this._render({hotel: hotel, only_data: this.options.only_data});
		
		var id = hotel.id;
		var context = this.options.context;
		$('input#book-request').bind('click', function() {
			var booking = {hotel: hotel, user: context.session('user')};
			context.session('booking', booking);
			context.redirect('#/booking/hotel', id);
		});
	}
});
})(jQuery);
});