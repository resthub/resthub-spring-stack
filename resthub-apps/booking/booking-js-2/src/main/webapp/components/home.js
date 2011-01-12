(function($) {

var home =
{
	options: {
		searchVal: null,
		size: 5,
		template : 'components/home.html',
		context : null
	},
	_init: function() {
		this.options.context.title('Home');
		
		this.options.searchVal = $('#search-value').val();
		this.options.size = $('#search-size').val();

		this.element.render(this.options.template, null);

		var self = this;
		dominoes('components/hotel/search.js', function() {
			$('#search').searchHotels({
				searchVal: self.options.searchVal,
				off: self.options.off,
				size: self.options.size,
				context: self.options.context
			});
		});
		
		dominoes('components/booking/list.js', function() {
			$('#booking-list').listBookings({context: self.options.context});
		});
		this.options.context.session('search-offset', 0);
	}
};

$.widget("booking.home", $.resthub.resthubController, home);
})(jQuery);