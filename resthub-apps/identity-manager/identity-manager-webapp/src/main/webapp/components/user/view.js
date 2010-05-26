$.widget("identity.viewUser", {
    options: {
        data : {},
        template : 'components/user/view.html',
        context : null
    },
    _create: function() {
		this.element.addClass('im-user-detail');
    },
    _init: function() {
        this.element.render(this.options.template, {user: this.options.data});
    },
    destroy: function() {
        this.element.removeClass('im-user-detail');
        $.Widget.prototype.destroy.call( this );
    }
});