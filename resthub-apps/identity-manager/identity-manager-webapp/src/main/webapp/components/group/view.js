(function($) {

var viewGroup =
{
	options: {
		groupName: null,
		template: 'components/group/view.html',
		context: null
	},
	_init: function() {
		this._get('api/group/name/' + this.options.groupName, this._displayGroup);
	},
	_displayGroup: function(group) {
		this.element.render(this.options.template, {group: group});
	}
};

$.widget("identity.viewGroup", $.resthub.resthubController, viewGroup);
})(jQuery);