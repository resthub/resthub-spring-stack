$.widget("booking.confirmHotel", {
    options: {
        data : {},
        template : 'components/booking/confirm.html',
        context : null
    },
    _create: function() {
		this.element.addClass('bd-booking-confirm');
    },
    _init: function() {

        this.element.render(this.options.template, {hotel: this.options.data});

		/*this.element.find('input#book-request').bind('click', function() {
			$('form#booking-form').attr('action', '#/booking/confirm' + self.options.data.id);
			$('a#cancel-request').replaceWith('<a id="cancel-request" href="/#/hotel/'+ self.options.data.id +'">Cancel</a>');
			$('input#book-request').replaceWith('<input id="book-request" value="Proceed" type="submit">');
			$('div#booking-form').load('components/booking/create.html');
		})*/
    },
    destroy: function() {
        this.element.removeClass('bd-booking-confirm');
        $.Widget.prototype.destroy.call( this );
    }
});