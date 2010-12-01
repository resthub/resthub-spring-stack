(function($){
    /**Used to display list of Users*/
    var userPermissionsList = {
        options: {
            template: URLS["templateUserPermissionsList"],
            context: null,
            page: 0,
            userName: null,
            result: null
        },
        /**Init's the swichPage mode*/
        _init: function(){
            self = this;
            this._switchPage(this.options.page);
        },
        /**Displays and renders the List
         *
         * @param {Object} result
         * Result with the list of users in result.element
         */
        _displayPermissions: function(result){
            this.element.render(this.options.template, {
                result: result,
				name : this.options.userName
            });
            
            self.options.result = result;
            $('span.page-switcher').each(function(index, element){
                $(element).click(function(){
                    var page = $(this).attr('id').split("-")[1];
                    self._switchPage(page);
                });
            });
            
            $("table tr:nth-child(even)").addClass("striped");
            
            $('div#delete').click(function(){
                self._deletePermission();
            });
            
            $('span.remove-permission').each(function(index, element){
                $(element).click(function(){
                    self._deleteThisPermission($(element).attr('id'));
                });
            });
			
			$('input#entityAddPermissionButton').click(
			function(){
				self._addThisPermission($('input#entityAddPermission').val())		
			}
			);
            
            
        },
        /**Swiths to the page
         * @param {Integer} page
         * the page to display
         */
        _switchPage: function(page){
            this.options.page = page;
            this._securedGet(URLS["apiUserName"] + this.options.userName + URLS["apiUserPermissionsList"], this._displayPermissions);
        },
        
		 /** 
         * Add a permission to the user
         *
         * @param {String} permission
         * the permission to be added
         */
        _addThisPermission: function(permission){
            var groups = this.options.result;
            
            var answer = confirm(l("confirmUserPermissionAddBegin") + permission + l("confirmUserPermissionAddEnd"));
            if (answer) {
                var accessToken = this.options.context.session('accessToken');
                $.oauth2Ajax({
                    url: URLS["apiUserName"] + this.options.userName + URLS["apiUserPermissionsList"] + permission,
                    type: 'PUT',
                    complete: function(){
                        self._switchPage(self.options.page);
                    },
                }, accessToken);
            }
        },
        /** 
         * Deletes 1 user, the one on which there was a click
         * It asks a confirmation before the deletion
         *
         * @param {Integer} index
         * the index of the user to delete
         */
        _deleteThisPermission: function(index){
            var permissions = this.options.result;
            
            var answer = confirm(l("confirmUserPermissionDeletionBegin") + permissions[index]+ l("confirmUserPermissionDeletionEnd"));
            if (answer) {
                var accessToken = this.options.context.session('accessToken');
                $.oauth2Ajax({
                    url: URLS["apiUserName"] + this.options.userName + URLS["apiUserPermissionsList"] + permissions[index],
                    type: 'DELETE',
                    complete: function(){
                        self._switchPage(self.options.page);
                    },
                }, accessToken);
            }
        },
        
        /** Deletes some users, the ones which have been checked in the form 
         * It asks a confirmation before the deletion
         */
        _deletePermission: function(){
            var permissions = this.options.result;
            var permissionsToDelete = [];
            $('input.permission-checkbox').each(function(index, element){
                if (element.checked) {
                    permissionsToDelete.push(index);
                }
            });
            var message;
            message = (permissionsToDelete.length > 1) ? l("confirmUserPermissionDeletionBegin") : l("confirmUserPermissionDeletionBegin");
            
            for (element in permissionsToDelete) {
                message = message.concat(permissions[permissionsToDelete[element]]+ ",");
            }
            message = message.concat(l("confirmUserPermissionDeletionEnd"));
            var answer = confirm(message);
            
            
            if (answer) {
                /* there is actually one request for each user to delete*/
                for (element in permissionsToDelete) {
                    var accessToken = this.options.context.session('accessToken');
                    
                    /* We delete the user */
                    $.oauth2Ajax({
                        url: URLS["apiUserName"] + this.options.userName + URLS["apiUserPermissionsList"] + permissions[permissionsToDelete[element]],
                        type: 'DELETE',
                        complete: function(){
                            self._switchPage(self.options.page);
                        },
                    }, accessToken);
                }
            }
        }
    };
	/** Needed for context propagation*/
    var self;
	/** Variable used for text localization with l10n */
    var l = function(string){
        return string.toLocaleString()
    };
    $.widget("identity.userPermissionsList", $.resthub.resthubController, userPermissionsList);
})(jQuery);
