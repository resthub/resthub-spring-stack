$.widget("identity.listUsers", {
    options: {
        data : {},
        template : 'components/user/list.html',
        context : null
    },
    _create: function() {
        
    },
    _init: function() {
        this.element.render(this.options.template, this.options.data);
    },
    destroy: function() {
        $.Widget.prototype.destroy.call( this );
    }
});