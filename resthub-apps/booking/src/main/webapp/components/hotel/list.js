$.widget("identity.listHotels", {
	options: {
		data : {},
		template : 'components/hotel/list.html',
		context : null
	},
	_create: function() {
		this.element.addClass('bd-hotel-list');
	},
	_init: function() {
		var self = this;
		this.element.render(this.options.template, {hotels: this.options.data});
		
		this.element.find('tr.hotel-item').click(function() {
			var id = $(this).attr('id').split("-")[1];
			self.options.context.redirect('#/hotel', id);
		});
	},
	destroy: function() {
		this.element.removeClass('bd-hotel-list');
		$.Widget.prototype.destroy.call( this );
	}
});