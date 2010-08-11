$.widget("identity.viewGroup", {
    options: {
        data : {},
        template : 'components/group/view.html',
        context : null
    },
    _create: function() {
		this.element.addClass('im-group-detail');
    },
    _init: function() {
		console.log($.toJSON(this.options.data));
        this.element.render(this.options.template, {group: this.options.data});
    },
    destroy: function() {
        this.element.removeClass('im-group-detail');
        $.Widget.prototype.destroy.call( this );
    }
});