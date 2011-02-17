define({
	// root is mandatory.
	'root': {
	    'titles': {
	        'login': 'Connexion',
	        'home': 'Accueil',
	        'confirmUserRemoval': 'Confirmation de la suppression',
	        'usersManagement': 'Gestion des utilisateurs',
	        'serverError': 'Problème de communication',
	        'confirmGroupRemoval': 'Confirmation de la suppression',
	        'groupsManagement': 'Gestion des groupes'
	    },
	    'labels': {
	    	'yes': 'Oui',
	    	'no': 'Non',
	    	'cancel': 'Annuler',
	        'username': 'Identifiant : ',
	        'password': 'Mot de passe : ',
	        'editPassword': 'Changement du mot de passe',
	        'newPassword': 'Nouvelle valeur : ',
	        'confirmPassword': 'Confirmation : ',
	        'home' : 'Accueil',
	        'usersManagement': 'Gestion des utilisateurs',
	    	'firstName': 'Prénom : ',
	    	'lastName': 'Nom : ',
	    	'email': 'E-mail : ',
		    'userPermissions': 'Permissions directes',
		    'addPersission': 'Nouvelle permission : ',
		    'usersList': 'Liste des utilisateurs',
		    'userDetails': 'Détails',
		    'noPermissions': 'Aucune permission',
		    'groupsManagement': 'Gestion des groupes',
		    'name': 'Nom : ',
			'groupsList': 'Liste des groupes',
			'groupDetails': 'Détails',
		    'groupPermissions': 'Permissions directes'
	    },
	    'buttons':  {
	        'login': 'Entrer',
	        'logout': 'Se déconnecter',
	        'changePassword': 'Modifier',
	        'saveUser':'Enregistrer',
	        'createUser':'Créer',
	        'removePermission':'Supprimer',
	        'previousPage': 'Précédente',
	        'nextPage': 'Suivante',
	        'editUser': 'Editer',
	        'removeUser': 'Supprimer',
	        'newUser': 'Réinitialiser',
	        'addPermission': 'Ajouter',
	        'saveGroup':'Enregistrer',
	        'createGroup':'Créer',
	        'editGroup': 'Editer',
	        'removeGroup': 'Supprimer',
	        'newGroup': 'Réinitialiser'
	    },
	    'columns': {
	    	'firstName': 'Prénom',
	    	'lastName': 'Nom',
	    	'login': 'Identifiant',
	    	'email': 'E-mail',
	    	'actions': 'Actions',
		    'name': 'Nom'
	    },
	    'texts': {
	    	'confirmUserDeletion': 'Voulez vous vraiment supprimer l\'utilisateur %s %s ?',
	    	'welcome': 'Bienvenue %s %s !',
		    'confirmGroupDeletion': 'Voulez vous vraiment supprimer le groupe %s ?'
	    },
	    'notifications': {
	    	'passwordSaved':'Mot de passe enregistré !',
	    	'wrongCredentials': 'Identifiants inconnus !',
		    'permissionRemoved': 'Permission supprimée !',
		    'permissionAdded':'Permission ajoutée !',
		    'userSaved': 'L\'utilisateur %s %s à été enregistré.',
		    'userRemoved': 'L\'utilisateur %s %s à été supprimé.',
		    'welcome': 'Bienvenue %s %s !',
		    'groupSaved': 'Le groupe %s à été enregistré.',
		    'groupRemoved': 'Le groupe %s à été supprimé.'
	    },
	    'errors': {
		    'tooWeakPassword': 'Votre mot de passe n\'est pas assez sécurisé : il doit contenir entre 5 et 15 caractères, dont une majuscule, une minuscule et un chiffre.',
		    'passwordConfirmation': 'La confirmation est différente du mot de passe.',
	    	'sessionExpired': 'Votre session a expiré',
	    	'serverError': 'L\'action ne peut être réalisée :\n'
	    }
	}
});	