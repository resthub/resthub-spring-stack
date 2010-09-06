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
		
		$('table tr:nth-child(even)').addClass('striped');

		var self = this;
		/* Link 'remove' action on _removeUser function */
		$('span.remove-user').each( function(index, element) {
			$(element).click( function() { self._removeUser( $(element).attr('id') ); });
		});
	},
	_removeUser: function(userLogin) {
		this._delete('api/group/' + this.options.groupName + '/user/' + userLogin, this._userRemoved);
	},
	_userRemoved: function(updatedGroup) {
		$.pnotify ({
			pnotify_title: 'Information',
			pnotify_text: 'User deleted successfully from group ' + updatedGroup.name + '.'
		});
		this._displayGroup(updatedGroup);
	}
};

$.widget("identity.viewGroup", $.resthub.resthubController, viewGroup);
})(jQuery);