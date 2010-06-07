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
		var context = this.options.context;
		var id = this.options.data.id;
		this.element.render(this.options.template, {hotel: this.options.data});
		
		$('input#book-request').bind('click', function() {
			context.redirect('#/booking/hotel', id)
		});
    },
    destroy: function() {
        this.element.removeClass('bd-hotel-detail');
        $.Widget.prototype.destroy.call( this );
    }
});