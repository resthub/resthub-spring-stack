define([ 
        'lib/oauth2controller',
        'repositories/user.repository',
        'lib/jqueryui/button',
        'controller/user/utils',
        'lib/jqueryui/dialog'
    ], function(OAuth2Controller, UserRepository) {
	
	return OAuth2Controller.extend("ManageController", {
		
		edited: null,
		
		removed: null,
		
		users: null,
		
		template : 'controller/user/manage.html',
		
		init : function() {
			this._loadList();
			$.subscribe('delete-user', $.proxy(this, '_removeButtonHandler'));				
		},
		
		_loadList: function() {
			// TODO waiting 
			$('#wrapper').html('retrieving users, please wait...');
			UserRepository.list($.proxy(this, '_listHandler'));			
		},
		
		_fillForm: function() {
			$('#userDetails input[name="firstName"]').val(this.edited.firstName);
			$('#userDetails input[name="lastName"]').val(this.edited.lastName);	
			$('#userDetails input[name="login"]').val(this.edited.login);	
			$('#userDetails input[name="email"]').val(this.edited.email);	

			$('#userDetails .submit').button("option", {label: this.edited.id ? 'Save' : 'Create'});
			
			this._refreshPermissionsTable();
		},
		
		_fillEdited: function() {
			this.edited.firstName = $('#userDetails input[name="firstName"]').val();
			this.edited.lastName = $('#userDetails input[name="lastName"]').val();	
			this.edited.login = $('#userDetails input[name="login"]').val();	
			this.edited.email = $('#userDetails input[name="email"]').val();
			if(!this.edited.id) {
				this.edited.password = $('#userDetails input[name="password"]').val();
			}
		},
				
		_refreshPermissionsTable: function() {
			var permTable = $('#userPermissions');
			$('#userPermissions tr').remove();
			if (this.edited) {
				for (var idx in this.edited.permissions) {
					permTable.append('<tr><td>'+this.edited.permissions[idx]+'</td>'+
							'<td><a data-idx="'+idx+'" class="permissionRemove" href="#">'+
							'Remove</a></td></tr>');
				}
				if (this.edited.permissions.length == 0) {
					permTable.append('<tr><td>No permission yet</td></tr>');
				} else {
					$('#userPermissions .permissionRemove').button().click($.proxy(this, 
							'_removePermissionButtonHandler'));
				}
			}
			this._permissionKeyUpHandler();
		},
		
		_newButtonHandler: function() {
			// Empty all fields.
			this.edited = {
	            groups: [],
	            permissions: [],
				firstName: '',
				lastName:'',
				login:'',
				password:'',
				email:''
			};
			this._fillForm();
			$('#userDetails .submit').before('<div id="divPassword"><label>Password: </label><input type="text" name="password"/></div>');
			return false;
		},
		
		_saveButtonHandler: function() {
			this._fillEdited();
			UserRepository.save($.proxy(this, '_userSavedHandler'), $.toJSON(this.edited));
			// Do not post form.
			return false;
		},
		
		_editButtonHandler: function(event) {
			var idx = $(event.target.parentNode).data('idx');
			this.edited = this.users[idx];
			this._fillForm();
			if($('#userDetails #divPassword')) $('#userDetails #divPassword').remove();
			
			return false;
		},
		
		_removeButtonHandler: function(id) {
			UserRepository.remove($.proxy(this, '_userRemovedHandler'), id);
			return false;
		},
		
		_removePermissionButtonHandler: function(event) {
			idx=$(event.target.parentNode).data('idx');
			var removed = this.edited.permissions.splice(idx, 1);
			this._refreshPermissionsTable();
			UserRepository.removePermission($.proxy(this, '_permissionRemovedHandler'), 
					this.edited, removed);
			return false;
		},
		
		_addPermissionButtonHandler: function(idx) {
			var added = $("input[name=addedPermission]").val();
			this.edited.permissions.push(added);
			this._refreshPermissionsTable();
			UserRepository.addPermission($.proxy(this, '_permissionAddedHandler'), 
					this.edited, added);
			return false;
		},
		
		_permissionKeyUpHandler: function() {
			$('#permissions .submit').button('option', 'disabled', !this.edited || !this.edited.id || 
					$('#permissions input[name=addedPermission]').val().length == 0);
		},
		
		_userSavedHandler: function(data, textStatus, XMLHttpRequest) {
			this.edited = data;
			$.pnotify('User ' + this.edited.firstName + ' ' + this.edited.lastName + ' was saved.');
			this._loadList();
		},
		
		_userRemovedHandler: function(data, textStatus, XMLHttpRequest) {
			if (this.edited && this.edited.id == this.removed.id ) {
				this.edited = null;
			}
			$.pnotify('User ' + this.removed.firstName + ' ' + this.removed.lastName + ' was removed.');
			this._loadList();			
		},
		
		_permissionRemovedHandler:  function(data, textStatus, XMLHttpRequest) {
			$.pnotify('Permission removed !');			
		},
		
		_permissionAddedHandler:  function(data, textStatus, XMLHttpRequest) {
			$.pnotify('Permission added !');			
		},
		
		_listHandler: function(data, textStatus, XMLHttpRequest) {
			this.users = data.elements;
			this.render({result:data, edited:this.edited});
			$.connectLogutButton();
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
			
			if (this.edited == null) {
				this._newButtonHandler();
			} else {
				this._fillForm();
			}
		}
		
	});
});
