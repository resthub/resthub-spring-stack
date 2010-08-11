$.widget("identity.listGroups", {
    options: {
        data : {},
        template : 'components/group/list.html',
        context : null
    },
    _create: function() {
        this.element.addClass('im-group-list');
    },
    _init: function() {
		var self = this;
        this.element.render(this.options.template, {groups: this.options.data});

		this.element.find('tr.group-item').click(function() {
            var id = $(this).attr('id').split("-")[1];
            self.options.context.redirect('#/group', id);
        });
    },
    destroy: function() {
		this.element.removeClass('rt-group-list');
        $.Widget.prototype.destroy.call( this );
    }
});