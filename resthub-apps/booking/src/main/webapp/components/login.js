$.widget("booking.login", {
    options: {
        data : {},
        template : 'components/login.html',
        context : null
    },
    _create: function() {
        
    },
    _init: function() {
        var self = this;
        this.element.render(this.options.template, this.options.data);
        this.element.find("#formLogin").submit(function() {
            $.ajax({
                url: 'api/user/check/',
                dataType: 'json',
                success: function(poll) {
                   self.options.data = poll;
                   self._init();
                }
            });
        });

    },
    destroy: function() {
        $.Widget.prototype.destroy.call( this );
    }
});

