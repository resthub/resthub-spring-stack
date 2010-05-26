$.widget("identity.listUsers", {
    options: {
        data : {},
        template : 'components/user/list.html',
        context : null
    },
    _create: function() {
        this.element.addClass('im-user-list');
    },
    _init: function() {
		var self = this;
		//alert($.toJSON(users));
        this.element.render(this.options.template, {users: this.options.data});

		this.element.find('tr.user-item').click(function() {
            var id = $(this).attr('id').split("-")[1];
            self.options.context.redirect('#/user', id);
        });
    },
    destroy: function() {
		this.element.removeClass('rt-user-list');
        $.Widget.prototype.destroy.call( this );
    }
});