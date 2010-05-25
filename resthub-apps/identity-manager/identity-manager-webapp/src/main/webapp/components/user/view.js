$.widget("identity.viewUser", {
    options: {
        data : {},
        template : 'components/user/view.html',
        context : null
    },
    _create: function() {
		
    },
    _init: function() {
		alert($.toJSON(this.options.data));
        this.element.render(this.options.template, this.options.data.user);
    },
    destroy: function() {
        $.Widget.prototype.destroy.call( this );
    }
});