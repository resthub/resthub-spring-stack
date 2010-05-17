$.widget("booking.listBookings", {
    options: {
        data : {bookings: {}},
        template : 'components/booking/list.html',
        context : null
    },
    _create: function() {

    },
    _init: function() {
        var self = this;
        this.element.render(this.options.template, this.options.data);
    },
    destroy: function() {
        $.Widget.prototype.destroy.call( this );
    }
});

