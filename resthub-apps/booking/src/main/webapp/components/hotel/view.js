(function($) {

var viewHotel =
{
	options: {
		id: null,
		template: 'components/hotel/view.html',
		context: null
	},
	_init: function() {
		if(!isNaN(this.options.id)) {
			this._get('api/hotel/' + this.options.id, this, '_displayHotel');
		}
	},
	_displayHotel: function(hotel) {
		this.element.render(this.options.template, {hotel: hotel, only_data: false});
		
		var id = hotel.id;
		var context = this.options.context;
		$('input#book-request').bind('click', function() {
			var booking = {hotel: hotel, user: context.session('user')};
			context.session('booking', booking);
			context.redirect('#/booking/hotel', id);
		});
	}
};

$.widget("booking.viewHotel", $.resthub.resthubController, viewHotel);
})(jQuery);