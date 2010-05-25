$.widget("identity.listUsers", {
    options: {
        data : {},
        template : 'components/user/list.html',
        context : null
    },
    _create: function() {
        
    },
    _init: function() {
		alert($.toJSON(this.options.data.users));
        this.element.render(this.options.template, this.options.data.users);
    },
    destroy: function() {
        $.Widget.prototype.destroy.call( this );
    }
});