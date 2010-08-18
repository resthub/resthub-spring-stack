(function($) {

var listHotels =
{
	options: {
		searchVal: null,
		page: 0,
		size: 5,
		template: 'components/hotel/list.html',
		context: null
	},
	_init: function() {
		var url;
		if(this.options.searchVal) {
			url = 'api/hotel/search?q=' + this.options.searchVal + '&page=' + this.options.page + '&size=' + this.options.size;
		} else {
			url = 'api/hotel/search?page=' + this.options.page + '&size=' + this.options.size;
		}
		this._get(url, this._displayHotels);
	},
	_displayHotels: function(result) {
		this.element.render(this.options.template, {result: result});

		var self = this;
		$('#search-next').bind('click', function() {
			self.options.page++;
			self._init();
		});
	}
};

$.widget("booking.listHotels", $.resthub.resthubController, listHotels);
})(jQuery);