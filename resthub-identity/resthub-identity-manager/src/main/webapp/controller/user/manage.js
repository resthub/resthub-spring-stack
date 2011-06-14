define([ 
        'i18n!nls/labels',
        'lib/controller',
        'repositories/user.repository',
        'lib/jqueryui/button',
        'controller/utils',
        'lib/jqueryui/dialog',
        'lib/jquery/jquery.sprintf'
    ], function(i18n, Controller, UserRepository) {
	
	/**
	 * Class UserManageController
	 * 
	 * This controller is able to manage users: create, remove and list them (with pagination).
	 * Performs user edition, and permissions management at user level.
	 */
	return Controller.extend("UserManageController", {
		
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
		
		/**
		 * Fills the UI form with values of the edited user.
		 * Therefore, this._edited must not be null.
		 * Refreshs the permission table.
		 */
		_fillForm: function() {
			$('#userDetails input[name="firstName"]').val(this._edited.firstName);
			$('#userDetails input[name="lastName"]').val(this._edited.lastName);	
			$('#userDetails input[name="login"]').val(this._edited.login);	
			$('#userDetails input[name="email"]').val(this._edited.email);	
			
			$('#userDetails .submit').button("option", {label: this._edited.id ? i18n.buttons.saveUser : 
				i18n.buttons.createUser});
			
			this._refreshPermissionsTable();
		}, // _fillForm().
		
		/**
		 * Fills the edited user with values from the UI form.
		 * Therefore, this._edited must not be null.
		 */
		_fillEdited: function() {
			this._edited.firstName = $('#userDetails input[name="firstName"]').val();
			this._edited.lastName = $('#userDetails input[name="lastName"]').val();	
			this._edited.login = $('#userDetails input[name="login"]').val();	
			this._edited.email = $('#userDetails input[name="email"]').val();
			if(!this._edited.id) {
				this._edited.password = $('#userDetails input[name="password"]').val();
			}
		}, // _fillEdited().
				
		/**
		 * Redraw the permission table regarding the current user permissions.
		 * Therefore, this._edited must not be null.
		 */
		_refreshPermissionsTable: function() {
			var permTable = $('#userPermissions');
			$('#userPermissions tr').remove();
			if (this._edited) {
				for (var idx in this._edited.permissions) {
					permTable.append('<tr><td>'+this._edited.permissions[idx]+'</td>'+
							'<td><a data-idx="'+idx+'" class="permissionRemove" href="#">'+
							i18n.buttons.removePermission+'</a></td></tr>');
				}
				if (this._edited.permissions.length == 0) {
					permTable.append('<tr><td>'+i18n.labels.noPermissions+'</td></tr>');
				} else {
					$('#userPermissions .permissionRemove').button().click($.proxy(this, 
							'_removePermissionButtonHandler'));
				}
			}
			this._permissionKeyUpHandler();
		}, // _refreshPermissionsTable().
		
			// ---------------------------------------------------------------------------------------------------------
			// UI handlers

		/**
		 * Handler connected to the edition form reset.
		 * Empties the form and show the clear password field.
		 * 
		 * @returns false to stop event propgation.
		 */
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
		
		/**
		 * Handler connected to the edition form submission.
		 * Send the edited user to server, after filling it with UI form values.
		 * this._userSavedHandler() is called on success.
		 * 
		 * @returns false to stop event propgation.
		 */
		_saveButtonHandler: function() {
			$.loading(true);
			this._fillEdited();
			UserRepository.save($.proxy(this, '_userSavedHandler'), $.toJSON(this._edited));
			// Do not post form.
			return false;
		}, // _saveButtonHandler().
		
		/**
		 * Handler connected to the edition action link in the user table.
		 * Clears the edition form, changes the edited user, and fills the UI form with current user values.
		 * 
		 * @returns false to stop event propgation.
		 */
		_editButtonHandler: function(event) {
			var idx = $(event.target.parentNode).data('idx');
			this._edited = this._users[idx];
			$('#divPassword').hide();
			this._fillForm();
			return false;
		}, // _editButtonHandler().
		
		/**
		 * If the next button is enabled, goes to the next page.
		 * 
		 * @returns false to stop event propgation.
		 */
		_nextPageButtonHandler: function(event) {
			var button = $(event.target.parentNode).data().button;
			if(!button.options.disabled) {
				this._loadList(this._page+1);
			}
			return false;
		}, // _nextPageButtonHandler().
		
		/**
		 * If the previous button is enabled, goes to the previous page.
		 * 
		 * @returns false to stop event propgation.
		 */
		_previousPageButtonHandler: function(event) {
			var button = $(event.target.parentNode).data().button;
			if(!button.options.disabled) {
				this._loadList(this._page-1);		
			}
			return false;
		}, // _previousPageButtonHandler().
		
		/**
		 * Handler invoked after user validation for user removal.
		 * Call the server to remove the removed user.
		 * 
		 * @returns false to stop event propgation.
		 */
		_removeButtonHandler: function(id) {
			$.loading(true);
			UserRepository.remove($.proxy(this, '_userRemovedHandler'), id);
			return false;
		}, // _removeButtonHandler().
		
		/**
		 * Handler invoked to remove a permission of the edited user.
		 * Call the server to remove the permissions if the edited user is already existing. 
		 * Permission table is immediately refreshed. 
		 * 
		 * @returns false to stop event propgation.
		 */
		_removePermissionButtonHandler: function(event) {
			var idx = $(event.target.parentNode).data('idx');
			var removed = this._edited.permissions.splice(idx, 1);
			this._refreshPermissionsTable();
			if(this._edited.id) {
				$.loading(true);
				UserRepository.removePermission($.proxy(this, '_permissionRemovedHandler'), 
						this._edited, removed);
			}
			return false;
		}, // _removePermissionButtonHandler().
		
		/**
		 * Handler invoked to add a permission of the edited user.
		 * Call the server to add the permissions if the edited user is already existing. 
		 * Permission table is immediately refreshed. 
		 * 
		 * @returns false to stop event propgation.
		 */
		_addPermissionButtonHandler: function(idx) {
			var added = $("input[name=addedPermission]").val();
			this._edited.permissions.push(added);
			this._refreshPermissionsTable();
			// it's a creation so the user does not exists on the server
			// we just add the permission to the local js object, showing it in the list
			// if the user save the user he's creating, the permissions will be saved too.
			if(this._edited.id) {
				// it's an edition of a user so one can add permission right now
				$.loading(true);
				UserRepository.addPermission($.proxy(this, '_permissionAddedHandler'),this._edited, added);
			}
			
			return false;
		}, // _addPermissionButtonHandler().
		
		/**
		 * Handler invoked when typing into the permission textbox, to enable or not the permission add button.
		 */
		_permissionKeyUpHandler: function() {
			$('#permissions .submit').button('option', 'disabled', !this._edited || !this._edited.id || 
					$('#permissions input[name=addedPermission]').val().length == 0);
		}, // _permissionKeyUpHandler().
		
			// ---------------------------------------------------------------------------------------------------------
			// Server handlers

		/**
		 * Handler invoked after user save on server. Reloads entire list.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_userSavedHandler: function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			this._edited = data;
			$.pnotify($.sprintf(i18n.notifications.userSaved, this._edited.firstName, this._edited.lastName));
			this._loadList();
		}, // _userSavedHandler().
		
		/**
		 * Handler invoked after user removal on server. Displays notification.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_userRemovedHandler: function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			if (this._edited && this._edited.id == this._removed.id ) {
				this._edited = null;
			}
			$.pnotify($.sprintf(i18n.notifications.userRemoved, this._removed.firstName, this._removed.lastName));
			this._loadList();			
		}, // _userRemovedHandler().
		
		/**
		 * Handler invoked after permission removal on server. Displays notification.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_permissionRemovedHandler:  function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			$.pnotify(i18n.notifications.permissionRemoved);			
		}, // _permissionRemovedHandler().
		
		/**
		 * Handler invoked after permission addition on server. Displays notification.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_permissionAddedHandler:  function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			$.pnotify(i18n.notifications.permissionAdded);			
		}, // _permissionAddedHandler().
		
		/**
		 * Handler invoked after list retrieval.  
		 * Updates the current and total page numbers, refresh all rendering (user table, user edition form, permissions
		 * table).
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_listHandler: function(data, textStatus, XMLHttpRequest) {
			this._users = data.elements;
			this._totalPages = data.totalPages;
			this._page = data.number;		
			// Refresh rendering.
			this.render({
				i18n:i18n,
				users: this._users, 
				page: this._page, 
				totalPages: this._totalPages, 
				edited:this._edited
			});
			$.connectLogoutButton();
			
			var self = this;
			// Connect removal buttons, with confirmation.
			$('.userRemove').button().click(function(event) {
				var idx = $(event.target.parentNode).data('idx');
				self._removed = self._users[idx];
				$('#userDetails').append('<div id="removeConfirm" title="'+i18n.titles.confirmUserRemoval+'">'+
						$.sprintf(i18n.texts.confirmUserDeletion, self._removed.firstName, self._removed.lastName)
						+'</div>');
				$("#removeConfirm").dialog({
					resizable: false,
					modal: true,
					buttons: [
						{text: i18n.labels.cancel, click: function() {
							$('#removeConfirm').dialog("close").remove();
						}},
			        	{text: i18n.labels.yes, click: function() {
							$('#removeConfirm').dialog("close").remove();
							$.publish('delete-user', self._removed.id);
						}}
					]
				});	
				return false;
			});
			// Connect buttons (new, edit, save, add permission, next/previous pages)
			$('#newUser').button().click($.proxy(this, '_newButtonHandler'));
			$('.userEdit').button().click($.proxy(this, '_editButtonHandler'));
			$('#userDetails .submit').button().click($.proxy(this, '_saveButtonHandler'));
			$('#permissions .submit').button().click($.proxy(this, '_addPermissionButtonHandler'));
			$('.buttonBar .previousPage').button({disabled: this._page == 0}).click(
					$.proxy(this, '_previousPageButtonHandler'));
			$('.buttonBar .nextPage').button({disabled: this._page == this._totalPages-1}).click(
					$.proxy(this, '_nextPageButtonHandler'));
			// Connect keyup handlers (add permission)		
			$('#permissions input[name=addedPermission]').keyup($.proxy(this, '_permissionKeyUpHandler'));
			// Refresh user edition.
			$('#divPassword').hide();
			if (!this._edited || !this._edited.id) {
				this._newButtonHandler();
			} else {
				this._fillForm();
			}
			$.loading(false);
		}, // _listHandler().
		
		// -------------------------------------------------------------------------------------------------------------
		// Constructor
		
		/**
		 * Constructor. 
		 * Subscribe to user deletion event.
		 */
		init : function() {
			this._loadList();
			document.title = i18n.titles.usersManagement;
			$.subscribe('delete-user', $.proxy(this, '_removeButtonHandler'));				
		} // init().
		
	}); // Class UserManageController
});
