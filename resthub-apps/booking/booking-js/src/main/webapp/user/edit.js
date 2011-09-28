define([ 'text!user/edit.html', 'lib/controller','repositories/user.repository' ], function(tmpl, Controller, UserRepository) {
	Controller.extend("EditUserController", {
		
		user : null,
		template : tmpl,
		
		init : function() {
			document.title = 'Settings';
			this.render();
			$('#save-password').bind('click', $.proxy(this, '_changePassword'));
		},
		_changePassword : function() {
			if ($('input[name=password]').val() == $('input[name=verifyPassword]').val()) {
				this.user = $.storage.get('user');
				this.user.password = $('input[name=password]').val();
				UserRepository.update($.proxy(this, '_passwordUpdated'), this.user.id, $.toJSON(this.user));
			}
		},
		_passwordUpdated : function() {
			$.storage.set('user', this.user);
			$.publish('password-updated');
		}
	});
});
