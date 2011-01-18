define([ 'jquery', 'models/user.dao', 'jquery.controller', 'jquery.json' ], function($, UserDao, Controller) {
	$.widget("booking.editUser", $.ui.controller, {
		options : {
			user : null,
			template : 'user/edit.html'
		},
		_init : function() {
			this.cx().title('Settings');
			this._render();
			$('#save-password').bind('click', $.proxy(this, '_changePassword'));
		},
		_changePassword : function() {
			if ($('input[name=password]').val() == $('input[name=verifyPassword]').val()) {
				this.options.user = this.cx().session('user');
				this.options.user.password = $('input[name=password]').val();
				UserDao.update($.proxy(this, '_passwordUpdated'), this.options.user.id, $.toJSON(this.options.user));
			}
		},
		_passwordUpdated : function() {
			this.cx().session('user', this.options.user);
			this.cx().trigger('password-updated');
		}
	});
});
