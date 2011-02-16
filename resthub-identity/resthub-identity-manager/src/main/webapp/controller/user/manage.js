define([ 
        'lib/oauth2controller',
        'repositories/user.repository',
        'lib/jqueryui/button',
        'controller/user/utils',
        'lib/jqueryui/dialog'
    ], function(OAuth2Controller, UserRepository) {
	
	/**
	 * Class ManageController
	 * 
	 * This controller is able to manage users: create, remove and list them (with pagination).
	 * Performs user edition, and permissions management at user level.
	 */
	return OAuth2Controller.extend("ManageController", {
		
		// -------------------------------------------------------------------------------------------------------------
		// Public attributes

		/**
		 * Controller's template
		 */
		template : 'controller/user/manage.html',

		// -------------------------------------------------------------------------------------------------------------
		// Private attributes
		
		/**
		 * Currently edited user.
		 */
		_edited: null,
		
		/**
		 * Currently removed user.
		 */
		_removed: null,
		
		/**
		 * List of displayed users (related to the current page).
		 */
		_users: null,
		
		/**
		 * Current displayed page.
		 */
		_page: 0,
		
		/**
		 * Total page number. 
		 */
		_totalPages: 0,
		
		// -------------------------------------------------------------------------------------------------------------
		// Private methods

		/**
		 * Load list of users.
		 * this._listHandler() will be invoked on success.
		 */
		_loadList: function(newPage) {
			// Send server request.
			$.loading(true);
			var page = newPage === null || newPage === undefined ? this._page : newPage < 0 ? 0 : newPage;
			UserRepository.list($.proxy(this, '_listHandler'), page);			
		}, // _loadList().
		
		_fillForm: function() {
			$('#userDetails input[name="firstName"]').val(this._edited.firstName);
			$('#userDetails input[name="lastName"]').val(this._edited.lastName);	
			$('#userDetails input[name="login"]').val(this._edited.login);	
			$('#userDetails input[name="email"]').val(this._edited.email);	
			
			$('#userDetails .submit').button("option", {label: this._edited.id ? 'Save' : 'Create'});
			
			this._refreshPermissionsTable();
		}, // _fillForm().
		
		_fillEdited: function() {
			this._edited.firstName = $('#userDetails input[name="firstName"]').val();
			this._edited.lastName = $('#userDetails input[name="lastName"]').val();	
			this._edited.login = $('#userDetails input[name="login"]').val();	
			this._edited.email = $('#userDetails input[name="email"]').val();
			if(!this._edited.id) {
				this._edited.password = $('#userDetails input[name="password"]').val();
			}
		}, // _fillEdited().
				
		_refreshPermissionsTable: function() {
			var permTable = $('#userPermissions');
			$('#userPermissions tr').remove();
			if (this._edited) {
				for (var idx in this._edited.permissions) {
					permTable.append('<tr><td>'+this._edited.permissions[idx]+'</td>'+
							'<td><a data-idx="'+idx+'" class="permissionRemove" href="#">'+
							'Remove</a></td></tr>');
				}
				if (this._edited.permissions.length == 0) {
					permTable.append('<tr><td>No permission yet</td></tr>');
				} else {
					$('#userPermissions .permissionRemove').button().click($.proxy(this, 
							'_removePermissionButtonHandler'));
				}
			}
			this._permissionKeyUpHandler();
		}, // _refreshPermissionsTable().
		
			// ---------------------------------------------------------------------------------------------------------
			// UI handlers

		_newButtonHandler: function() {
			// Empty all fields.
			this._edited = {
	            groups: [],
	            permissions: [],
				firstName: '',
				lastName:'',
				login:'',
				password:'',
				email:''
			};
			$('#divPassword').show();
			this._fillForm();
			return false;
		}, // _newButtonHandler().
		
		_saveButtonHandler: function() {
			$.loading(true);
			this._fillEdited();
			UserRepository.save($.proxy(this, '_userSavedHandler'), $.toJSON(this._edited));
			// Do not post form.
			return false;
		}, // _saveButtonHandler().
		
		_editButtonHandler: function(event) {
			var idx = $(event.target.parentNode).data('idx');
			this._edited = this._users[idx];
			$('#divPassword').hide();
			this._fillForm();
			var password = $('#userDetails #divPassword');
			if(password) {
				password.remove();
			}
			
			return false;
		}, // _editButtonHandler().
		
		_nextPageButtonHandler: function(event) {
			var button = $(event.target.parentNode).data().button;
			if(!button.options.disabled) {
				this._loadList(this._page+1);
			}
			return false;
		}, // _nextPageButtonHandler().
		
		_previousPageButtonHandler: function(event) {
			var button = $(event.target.parentNode).data().button;
			if(!button.options.disabled) {
				this._loadList(this._page-1);		
			}
			return false;
		}, // _previousPageButtonHandler().
		
		_removeButtonHandler: function(id) {
			$.loading(true);
			UserRepository.remove($.proxy(this, '_userRemovedHandler'), id);
			return false;
		}, // _removeButtonHandler().
		
		_removePermissionButtonHandler: function(event) {
			var idx = $(event.target.parentNode).data('idx');
			var removed = this._edited.permissions.splice(idx, 1);
			this._refreshPermissionsTable();
			$.loading(true);
			UserRepository.removePermission($.proxy(this, '_permissionRemovedHandler'), 
					this._edited, removed);
			return false;
		}, // _removePermissionButtonHandler().
		
		_addPermissionButtonHandler: function(idx) {
			var added = $("input[name=addedPermission]").val();
			this._edited.permissions.push(added);
			this._refreshPermissionsTable();
			if(this._edited.id) {
				// it's an edition of a user so one can add permission right now
				$.loading(true);
				UserRepository.addPermission($.proxy(this, '_permissionAddedHandler'),this._edited, added);
			} else {
				// it's a creation so the user does not exists on the server
				// we just add the permission to the local js object, showing it in the list
				// if the user save the user he's creating, the permissions will be saved too.
			}
					
			return false;
		}, // _addPermissionButtonHandler().
		
		_permissionKeyUpHandler: function() {
			$('#permissions .submit').button('option', 'disabled', !this._edited || !this._edited.id || 
					$('#permissions input[name=addedPermission]').val().length == 0);
		}, // _permissionKeyUpHandler().
		
			// ---------------------------------------------------------------------------------------------------------
			// Server handlers

		_userSavedHandler: function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			this._edited = data;
			$.pnotify('User ' + this._edited.firstName + ' ' + this._edited.lastName + ' was saved.');
			this._loadList();
		}, // _userSavedHandler().
		
		_userRemovedHandler: function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			if (this._edited && this._edited.id == this._removed.id ) {
				this._edited = null;
			}
			$.pnotify('User ' + this._removed.firstName + ' ' + this._removed.lastName + ' was removed.');
			this._loadList();			
		}, // _userRemovedHandler().
		
		_permissionRemovedHandler:  function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			$.pnotify('Permission removed !');			
		}, // _permissionRemovedHandler().
		
		_permissionAddedHandler:  function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			$.pnotify('Permission added !');			
		}, // _permissionAddedHandler().
		
		_listHandler: function(data, textStatus, XMLHttpRequest) {
			this._users = data.elements;
			this._totalPages = data.totalPages;
			this._page = data.number;		
			this.render({
				users: this._users, 
				page: this._page, 
				totalPages: this._totalPages, 
				edited:this._edited
			});
			$.connectLogoutButton();
			document.title = 'Users management';
			
			$('.userEdit').button().click($.proxy(this, '_editButtonHandler'));
			var self = this;
			$('.userRemove').button().click(function(event) {
				var idx = $(event.target.parentNode).data('idx');
				self.removed = self.users[idx];
				$('#userDetails').append('<div id="removeConfirm" title="User removal confirmation">'+
						'Do you really want to delete user '+ self.removed.firstName + ' ' + 
						self.removed.lastName +'</div>');
				$("#removeConfirm").dialog({
					resizable: false,
					modal: true,
					buttons: {
						Yes: function() {
							$('#removeConfirm').dialog("close").remove();
							$.publish('delete-user', self.removed.id);
						},
						Cancel: function() {
							$('#removeConfirm').dialog("close").remove();
						}
					}
				});	
				return false;
			});
			$('#newUser').button().click($.proxy(this, '_newButtonHandler'));
			$('#userDetails .submit').button().click($.proxy(this, '_saveButtonHandler'));
			$('#permissions .submit').button().click($.proxy(this, '_addPermissionButtonHandler'));
			
			$('#permissions input[name=addedPermission]').keyup($.proxy(this, '_permissionKeyUpHandler'));
			
			if (this._edited == null) {
				this._newButtonHandler();
			} else {
				$('#divPassword').hide();
				this._fillForm();
			}
			
			$('.buttonBar .previousPage').button({disabled: this._page == 0})
				.click($.proxy(this, '_previousPageButtonHandler'));
			$('.buttonBar .nextPage').button({disabled: this._page == this._totalPages-1})
				.click($.proxy(this, '_nextPageButtonHandler'));
			$.loading(false);
		}, // _listHandler().
		
		// -------------------------------------------------------------------------------------------------------------
		// Constructor
		
		init : function() {
			this._loadList();
			$.subscribe('delete-user', $.proxy(this, '_removeButtonHandler'));				
		} // init().
		
	}); // Class ManageController
});
