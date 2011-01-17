define(['jquery.controller'], function() {
(function($)
{
	
	$.widget("booking.editUser", $.ui.controller, {
		_init: function() {
			this.options.context.title('Settings');
			$('#save-password').bind('click', $.proxy(this._changePassword, this));
			this._render();
		},
		_changePassword: function() {
			if( $('input[name=password]').val() == $('input[name=verifyPassword]').val() )
			{
				this.options.user = this.options.context.session('user');
				this.options.user.password = $('input[name=password]').val();
				this._put('api/user/' + this.options.user.id, this._passwordUpdated, JSON.stringify(this.options.user));
			}
		},
		_passwordUpdated: function() {
			this.options.context.session('user', this.options.user);
			this.options.context.trigger('password-updated');
		},
		options: {
			user: null,
			template : 'user/edit.html'
		}
	});
})(jQuery);
});