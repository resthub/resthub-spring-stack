$.widget("booking.listHotels", {
	options: {
		data : {},
		template : 'components/hotel/list.html',
		context : null
	},
	_create: function() {
		this.element.addClass('bd-hotel-list');
	},
	_init: function() {
		this.element.render(this.options.template, {hotels: this.options.data});
	},
	destroy: function() {
		this.element.removeClass('bd-hotel-list');
		$.Widget.prototype.destroy.call( this );
	}
});