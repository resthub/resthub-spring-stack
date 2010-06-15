(function($) {

var listHotels =
{
	options: {
		searchVal: null,
		off: 1,
		lim: 5,
		template: 'components/hotel/list.html',
		context: null
	},
	_create: function() {
		this.element.addClass('bd-hotel-list');
	},
	_init: function() {
		var url;
		if(this.options.searchVal) {
			url = 'api/hotel/search?q=' + this.options.searchVal + '&off=' + this.options.off + '&lim=' + this.options.lim;
		} else {
			url = 'api/hotel/search?off=' + this.options.off + '&lim=' + this.options.lim;
		}
		this._get(url, this, '_displayHotels');
	},
	_displayHotels: function(hotels) {
		this.element.render(this.options.template, {hotels: hotels});
		var self = this;
		$('#search-next').bind('click', function() {
			var offset = self.options.context.session('search-offset');
			self.options.context.session('search-offset', offset + 1);
			self.options.context.trigger('hotel-search');
		});
	},
	destroy: function() {
		this.element.removeClass('bd-hotel-list');
		$.Widget.prototype.destroy.call( this );
	}
};

$.widget("booking.listHotels", $.resthub.resthubController, listHotels);
})(jQuery);