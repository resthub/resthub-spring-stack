define({
	// root is mandatory.
	'root': {
	    'titles': {
	        'login': 'Connection',
	        'home': 'Home',
	        'confirmUserRemoval': 'User removal confirmation',
	        'usersManagement': 'Users management',
	        'serverError': 'Server problem',
	        'confirmGroupRemoval': 'Group removal confirmation',
	        'groupsManagement': 'Groups management'
	    },
	    'labels': {
	    	'yes': 'Yes',
	    	'no': 'No',
	    	'cancel': 'Cancel',
	        'username': 'Login: ',
	        'password': 'Password: ',
	        'editPassword': 'Password edition',
	        'newPassword': 'New value: ',
	        'confirmPassword': 'Confirmation: ',
	        'home' : 'Home',
	        'usersManagement': 'Users Management',
		    'firstName': 'First Name: ',
		    'lastName': 'Last Name: ',
		    'email': 'E-mail: ',
		    'userPermissions': 'Direct permissions',
		    'addPersission': 'New permission: ',
		    'usersList': 'Users list',
		    'userDetails': 'Details',
		    'noPermissions': 'No permission yet',
		    'groupsManagement': 'Groups Management',
		    'name': 'Name : ',
			'groupsList': 'Groups list',
			'groupDetails': 'Details',
		    'groupPermissions': 'Direct permissions'
	    },
	    'buttons':  {
	        'login': 'Login',
	        'logout': 'Logout',
	        'changePassword': 'Change password',
	        'saveUser':'Save',
	        'createUser':'Create',
	        'removePermission':'Remove',
	        'previousPage': 'Previous',
	        'nextPage': 'Next',
	        'editUser': 'Edit',
	        'removeUser': 'Remove',
	        'newUser': 'Empty fields',
	        'addPermission': 'Add',
	        'saveGroup':'Save',
	        'createGroup':'Create',
	        'editGroup': 'Edit',
	        'removeGroup': 'Remove',
	        'newGroup': 'Empty fields'
	    },
	    'columns': {
	    	'firstName': 'First Name',
	    	'lastName': 'Last Name',
	    	'login': 'Login',
	    	'email': 'E-mail',
	    	'actions': 'Actions',
		    'name': 'Name'
	    },
	    'texts': {
	    	'confirmUserDeletion': 'Do you really want to delete user %s %s ?',
	    	'welcome': 'Welcome %s %s !',
		    'confirmGroupDeletion': 'Do you really want to delete group %s ?'
	    },
	    'notifications': {
	    	'passwordSaved':'Password successfully changed !',
		    'wrongCredentials': 'Wrong credentials !',
		    'permissionRemoved': 'Permission removed !',
		    'permissionAdded':'Permission added !',
		    'userSaved': 'User %s %s has been saved.',
		    'userRemoved': 'User %s %s has been removed.',
		    'welcome': 'Welcome %s %s !',
		    'groupSaved': 'Group %s has been saved.',
		    'groupRemoved': 'Group %s has been removed.'
	    },
	    'errors': {
	    	'tooWeakPassword': 'Your password isn\'t strong enough: must contains between 5 and 15 characters, and at least a lower-case, an upper-case and a digit.',
	    	'passwordConfirmation': 'The confirmation does not match the password.',
	    	'sessionExpired': 'Your session has expired',
	    	'serverError': 'The action cannot be realized:\n'
	    }
	}
});	