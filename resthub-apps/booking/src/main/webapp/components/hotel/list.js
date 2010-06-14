(function($) {

var listHotels =
{
	options: {
		data : {},
		template : 'components/hotel/list.html',
		context : null
	},
	_create: function() {
		this.element.addClass('bd-hotel-list');
	},
	_init: function() {
		var url;
		if(this.options.data.searchVal) {
			url = 'api/hotel/search?q=' + this.options.data.searchVal;
		} else {
			url = 'api/hotel';
		}
		this._get(url, this, '_displayHotels');
	},
	_displayHotels: function(hotels) {
		this.element.render(this.options.template, {hotels: hotels});
	},
	destroy: function() {
		this.element.removeClass('bd-hotel-list');
		$.Widget.prototype.destroy.call( this );
	}
};

$.widget("booking.listHotels", $.resthub.resthubController, listHotels);
})(jQuery);