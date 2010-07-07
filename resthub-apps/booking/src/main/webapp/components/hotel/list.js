(function($) {

var listHotels =
{
	options: {
		searchVal: null,
		off: 0,
		lim: 5,
		template: 'components/hotel/list.html',
		context: null
	},
	_init: function() {
		var url;
		if(this.options.searchVal) {
			url = 'api/hotel/search?q=' + this.options.searchVal + '&off=' + this.options.off + '&lim=' + this.options.lim;
		} else {
			url = 'api/hotel/search?off=' + this.options.off + '&lim=' + this.options.lim;
		}
		this._get(url, this._displayHotels);
	},
	_displayHotels: function(hotels) {
		this.element.render(this.options.template, {hotels: hotels});

		var self = this;
		$('#search-next').bind('click', function() {
			self.options.off++;
			self._init();
		});
	}
};

$.widget("booking.listHotels", $.resthub.resthubController, listHotels);
})(jQuery);