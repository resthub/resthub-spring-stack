(function($)
{
	var editUser =
	{
		_init: function() {
			this.options.context.title('Settings');
			this.element.render(this.options.template);
			$('#save-password').bind('click', $.proxy(this._changePassword, this));
		},
		_changePassword: function() {
			if( $('input[name=password]').val() == $('input[name=verifyPassword]').val() )
			{
				this.options.user = this.options.context.session('user');
				this.options.user.password = $('input[name=password]').val();
				this._put('api/user/' + this.options.user.id, this._passwordUpdated, $.toJSON(this.options.user));
			}
		},
		_passwordUpdated: function() {
			this.options.context.session('user', this.options.user);
			this.options.context.trigger('password-updated');
		},
		options: {
			context: null,
			template: 'components/user/edit.html',
			user: null
		}
	};

	$.widget("booking.editUser", $.resthub.resthubController, editUser);
})(jQuery);