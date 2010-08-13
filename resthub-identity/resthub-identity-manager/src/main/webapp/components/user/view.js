(function($) {

var viewUser =
{
	options: {
		userLogin: null,
		template: 'components/user/view.html',
		context: null
	},
	_init: function() {
		this._get('api/user/login/' + this.options.userLogin, this._displayUser);
	},
	_displayUser: function(user) {
		this.element.render(this.options.template, {user: user});
	}
};

$.widget("identity.viewUser", $.resthub.resthubController, viewUser);
})(jQuery);