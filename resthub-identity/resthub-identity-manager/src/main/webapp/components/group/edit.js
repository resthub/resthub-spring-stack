(function($) {

var editGroup =
{
	options: {
		template: 'components/group/edit.html',
		context: null,
		users: null
	},
	_init: function() {
		this._prepareData();
	},
	_prepareData: function() {
		this._securedGet( 'api/user/all' , this._setGroups );
	},
	_setGroups: function(users) {
		this.options.users = users;
		this._displayGroupForm();
	},
	_displayGroupForm: function() { 

		var group = this.options.context.session('tempGroup');

		this.element.render( this.options.template, {
					users: this.options.users,
					group: group
				});

		$('#content h1:first').html("Create group");

		this._sessionToForm();
		
		$('form#group-form').validate({errorElement: 'span'});

		$('input#group-proceed').unbind();
		$('input#group-proceed').bind('click', $.proxy(this._sendGroupData, this));
		
		
	},
	
		/* Tests if the form is filled correctly and sends the data */
	_sendGroupData: function() {
		var validForm = $('form#group-form').validate({errorElement: 'span'}).form();
		if (validForm) {
			this._formToSession();
			var group = this.options.context.session('tempGroup');
			this._securedPost( 'api/group', this._endOfProcess, $.toJSON(group));
		}
	},
	/* Puts form data in session */
	_formToSession: function() {
		var group = {
			users: [],
			permissions: []
		};
		
		group.name = $('input[name=name]').val();
		$('input[name=usergroup]:checked').each(function(index, element) {
			group.users.push({
				id: $(element).attr('value')
			});
		});
		
		this.options.context.session('tempGroup', group);
	},
	/* Displays session data in user form */
	_sessionToForm: function() {
		var group = this.options.context.session('tempGroup');
		if(group != null) {
			$('input[name=name]').val(group.name);
			for( var index in group.users ) {
				$('input[value=' + group.users[index].id + ']').attr('checked', true);
			}
		}
	},
	_endOfProcess: function(group) {
		//console.log(user);
		// Cleans the tempUser in session
		this.options.context.session('tempGroup', null);
		this.options.context.redirect('#/group/details/' + group.name);
	}
};

$.widget("booking.editGroup", $.resthub.resthubController, editGroup);
})(jQuery);