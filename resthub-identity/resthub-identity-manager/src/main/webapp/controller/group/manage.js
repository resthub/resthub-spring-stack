define([ 
        'i18n!nls/labels',
        'lib/oauth2controller',
        'repositories/group.repository',
        'lib/jqueryui/button',
        'controller/utils',
        'lib/jqueryui/dialog',
        'lib/jquery/jquery.sprintf'
    ], function(i18n, OAuth2Controller, GroupRepository) {
	
	/**
	 * Class GroupManageController
	 * 
	 * This controller is able to manage groups: create, remove and list them (with pagination).
	 * Performs groups edition, and permissions management at groups level.
	 */
	return OAuth2Controller.extend("GroupManageController", {
		
		// -------------------------------------------------------------------------------------------------------------
		// Public attributes

		/**
		 * Controller's template
		 */
		template : 'controller/group/manage.html',

		// -------------------------------------------------------------------------------------------------------------
		// Private attributes
		
		/**
		 * Currently edited group.
		 */
		_edited: null,
		
		/**
		 * Currently removed group.
		 */
		_removed: null,
		
		/**
		 * List of displayed groups (related to the current page).
		 */
		_groups: null,
		
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
		 * Load list of groups.
		 * this._listHandler() will be invoked on success.
		 */
		_loadList: function(newPage) {
			// Send server request.
			$.loading(true);
			var page = newPage === null || newPage === undefined ? this._page : newPage < 0 ? 0 : newPage;
			GroupRepository.list($.proxy(this, '_listHandler'), page);			
		}, // _loadList().
		
		/**
		 * Fills the UI form with values of the edited group.
		 * Therefore, this._edited must not be null.
		 * Refreshs the permission table.
		 */
		_fillForm: function() {
			$('#groupDetails input[name="name"]').val(this._edited.name);
			$('#groupDetails .submit').button("option", {label: this._edited.id ? i18n.buttons.saveGroup : 
				i18n.buttons.createGroup});
			
			this._refreshPermissionsTable();
		}, // _fillForm().
		
		/**
		 * Fills the edited group with values from the UI form.
		 * Therefore, this._edited must not be null.
		 */
		_fillEdited: function() {
			this._edited.name = $('#groupDetails input[name="name"]').val();
		}, // _fillEdited().
				
		/**
		 * Redraw the permission table regarding the current group permissions.
		 * Therefore, this._edited must not be null.
		 */
		_refreshPermissionsTable: function() {
			var permTable = $('#groupPermissions');
			$('#groupPermissions tr').remove();
			if (this._edited) {
				for (var idx in this._edited.permissions) {
					permTable.append('<tr><td>'+this._edited.permissions[idx]+'</td>'+
							'<td><a data-idx="'+idx+'" class="permissionRemove" href="#">'+
							i18n.buttons.removePermission+'</a></td></tr>');
				}
				if (this._edited.permissions.length == 0) {
					permTable.append('<tr><td>'+i18n.labels.noPermissions+'</td></tr>');
				} else {
					$('#groupPermissions .permissionRemove').button().click($.proxy(this, 
							'_removePermissionButtonHandler'));
				}
			}
			this._permissionKeyUpHandler();
		}, // _refreshPermissionsTable().
		
			// ---------------------------------------------------------------------------------------------------------
			// UI handlers

		/**
		 * Handler connected to the edition form reset.
		 * Empties the form.
		 * 
		 * @returns false to stop event propgation.
		 */
		_newButtonHandler: function() {
			// Empty all fields.
			this._edited = {
	            permissions: [],
				name: ''
			};
			this._fillForm();
			return false;
		}, // _newButtonHandler().
		
		/**
		 * Handler connected to the edition form submission.
		 * Send the edited group to server, after filling it with UI form values.
		 * this._groupSavedHandler() is called on success.
		 * 
		 * @returns false to stop event propgation.
		 */
		_saveButtonHandler: function() {
			$.loading(true);
			this._fillEdited();
			GroupRepository.save($.proxy(this, '_groupSavedHandler'), $.toJSON(this._edited));
			// Do not post form.
			return false;
		}, // _saveButtonHandler().
		
		/**
		 * Handler connected to the edition action link in the group table.
		 * Clears the edition form, changes the edited group, and fills the UI form with current group values.
		 * 
		 * @returns false to stop event propgation.
		 */
		_editButtonHandler: function(event) {
			var idx = $(event.target.parentNode).data('idx');
			this._edited = this._groups[idx];
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
		 * Handler invoked after end-user validation for group removal.
		 * Call the server to remove the removed group.
		 * 
		 * @returns false to stop event propgation.
		 */
		_removeButtonHandler: function(id) {
			$.loading(true);
			GroupRepository.remove($.proxy(this, '_groupRemovedHandler'), id);
			return false;
		}, // _removeButtonHandler().
		
		/**
		 * Handler invoked to remove a permission of the edited group.
		 * Call the server to remove the permissions if the edited group is already existing. 
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
				GroupRepository.removePermission($.proxy(this, '_permissionRemovedHandler'), 
						this._edited, removed);
			}
			return false;
		}, // _removePermissionButtonHandler().
		
		/**
		 * Handler invoked to add a permission of the edited group.
		 * Call the server to add the permissions if the edited group is already existing. 
		 * Permission table is immediately refreshed. 
		 * 
		 * @returns false to stop event propgation.
		 */
		_addPermissionButtonHandler: function(idx) {
			var added = $("input[name=addedPermission]").val();
			this._edited.permissions.push(added);
			this._refreshPermissionsTable();
			// it's a creation so the group does not exists on the server
			// we just add the permission to the local js object, showing it in the list
			// if the group save the group he's creating, the permissions will be saved too.
			if(this._edited.id) {
				// it's an edition of a group so one can add permission right now
				$.loading(true);
				GroupRepository.addPermission($.proxy(this, '_permissionAddedHandler'),this._edited, added);
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
		 * Handler invoked after group save on server. Reloads entire list.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_groupSavedHandler: function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			this._edited = data;
			$.pnotify($.sprintf(i18n.notifications.groupSaved, this._edited.name));
			this._loadList();
		}, // _groupSavedHandler().
		
		/**
		 * Handler invoked after group removal on server. Displays notification.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_groupRemovedHandler: function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			if (this._edited && this._edited.id == this._removed.id ) {
				this._edited = null;
			}
			$.pnotify($.sprintf(i18n.notifications.groupRemoved, this._removed.name));
			this._loadList();			
		}, // _groupRemovedHandler().
		
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
		 * Updates the current and total page numbers, refresh all rendering (group table, group edition form,
		 * permissions table).
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_listHandler: function(data, textStatus, XMLHttpRequest) {
			this._groups = data.elements;
			this._totalPages = data.totalPages;
			this._page = data.number;		
			// Refresh rendering.
			this.render({
				i18n:i18n,
				groups: this._groups, 
				page: this._page, 
				totalPages: this._totalPages, 
				edited:this._edited
			});
			$.connectLogoutButton();
			
			var self = this;
			// Connect removal buttons, with confirmation.
			$('.groupRemove').button().click(function(event) {
				var idx = $(event.target.parentNode).data('idx');
				self._removed = self._groups[idx];
				$('#groupDetails').append('<div id="removeConfirm" title="'+i18n.titles.confirmGroupRemoval+'">'+
						$.sprintf(i18n.texts.confirmGroupDeletion, self._removed.name)+'</div>');
				$("#removeConfirm").dialog({
					resizable: false,
					modal: true,
					buttons: [
						{text: i18n.labels.cancel, click: function() {
							$('#removeConfirm').dialog("close").remove();
						}},
			        	{text: i18n.labels.yes, click: function() {
							$('#removeConfirm').dialog("close").remove();
							$.publish('delete-group', self._removed.id);
						}}
					]
				});	
				return false;
			});
			// Connect buttons (new, edit, save, add permission, next/previous pages)
			$('#newGroup').button().click($.proxy(this, '_newButtonHandler'));
			$('.groupEdit').button().click($.proxy(this, '_editButtonHandler'));
			$('#groupDetails .submit').button().click($.proxy(this, '_saveButtonHandler'));
			$('#permissions .submit').button().click($.proxy(this, '_addPermissionButtonHandler'));
			$('.buttonBar .previousPage').button({disabled: this._page == 0}).click(
					$.proxy(this, '_previousPageButtonHandler'));
			$('.buttonBar .nextPage').button({disabled: this._page == this._totalPages-1}).click(
					$.proxy(this, '_nextPageButtonHandler'));
			// Connect keyup handlers (add permission)		
			$('#permissions input[name=addedPermission]').keyup($.proxy(this, '_permissionKeyUpHandler'));
			// Refresh group edition.
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
		 * Subscribe to group deletion event.
		 */
		init : function() {
			this._loadList();
			document.title = i18n.titles.groupsManagement;
			$.subscribe('delete-group', $.proxy(this, '_removeButtonHandler'));				
		} // init().
		
	}); // Class GroupManageController
});
