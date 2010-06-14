(function($) {

var viewHotel =
{
    options: {
        data : {},
        template : 'components/hotel/view.html',
        context : null
    },
    _create: function() {
		this.element.addClass('bd-hotel-detail');
    },
    _init: function() {
		this._get('api/hotel/' + this.options.data.id, this, '_displayHotel');
    },
	_displayHotel: function(hotel) {
		this.element.render(this.options.template, {hotel: hotel});

		var id = hotel.id;
		var context = this.options.context;
		$('input#book-request').bind('click', function() {
			var booking = { hotel: hotel, user: context.session('user') };
			context.session('booking', booking);
			context.redirect('#/booking/hotel', id);
		});
	},
    destroy: function() {
        this.element.removeClass('bd-hotel-detail');
        $.Widget.prototype.destroy.call( this );
    }
};

$.widget("booking.viewHotel", $.resthub.resthubController, viewHotel);
})(jQuery);