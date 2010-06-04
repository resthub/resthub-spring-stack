$.widget("booking.viewHotel", {
    options: {
        data : {},
        template : 'components/hotel/view.html',
        context : null
    },
    _create: function() {
		this.element.addClass('bd-hotel-detail');
    },
    _init: function() {
		var self = this;
		this.element.render(this.options.template, {hotel: this.options.data});
		
		$('input#book-request').bind('click', function() {
			self.options.context.redirect('#/booking/hotel', self.options.data.id)
		});
    },
    destroy: function() {
        this.element.removeClass('bd-hotel-detail');
        $.Widget.prototype.destroy.call( this );
    }
});