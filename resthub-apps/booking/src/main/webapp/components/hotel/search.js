(function($) {

var searchHotels =
{
	options: {
		searchVal: null,
		size: 5,
		template : 'components/hotel/search.html',
		context : null,
		delay : 1000,
		searching: null
	},
	_init: function() {

		this.element.render(this.options.template);
		
		var self = this;
		$('#search-submit').bind('click', function() {
			self.options.context.session('search-offset', 0);
			self.options.context.trigger('hotel-search');
		});

		$('#search-value').bind('keyup', function() {
			clearTimeout( self.options.searching );
			self.options.searching = setTimeout(function() {
				self.options.context.session('search-offset', 0);
				self.options.context.trigger('hotel-search');
			}, self.options.delay );
		});

		$('#search-size').bind('change', function() {
			self.options.context.session('search-offset', 0);
			self.options.context.trigger('hotel-search');
		});

		$('#search-size option[value='+ this.options.size +']').attr('selected', 'selected');
		$('#search-value').attr('value', this.options.searchVal);

		if(this.options.searchVal != '#home') {
			dominoes('components/hotel/list.js', function() {
				$('#result').listHotels({
					searchVal: self.options.searchVal,
					size: self.options.size,
					context: self.options.context
				});
			});
		}
	}
};

$.widget("booking.searchHotels", $.resthub.resthubController, searchHotels);
})(jQuery);