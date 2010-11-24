(function($){
    /**Used to display list of Users*/
    var userGroupsList = {
        options: {
            template: URLS["templateUserGroupList"],
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
        _displayUsers: function(result){
            this.element.render(this.options.template, {
                result: result
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
                self._deleteUser();
            });
            
            $('span.remove-user').each(function(index, element){
                $(element).click(function(){
                    self._deleteThisUser($(element).attr('id'));
                });
            });
            
        },
        /**Swiths to the page
         * @param {Integer} page
         * the page to display
         */
        _switchPage: function(page){
            this.options.page = page;
              this._securedGet(URLS["apiUserName"] + this.options.userName + URLS["apiUserGroupsList"], this._displayUsers);
		},
        
        /** 
         * Deletes 1 user, the one on which there was a click
         * It asks a confirmation before the deletion
         *
         * @param {Integer} index
         * the index of the user to delete
         */
        _deleteThisUser: function(index){
            var groups = this.options.result;
            
            var answer = confirm(l("confirmUserDeletionBegin") + groups[index].name + l("confirmUserDeletionEnd"));
            if (answer) {
                var accessToken = this.options.context.session('accessToken');
                $.oauth2Ajax({
                    url:URLS["apiUserName"] + this.options.userName+URLS["apiUserGroupsList"]+ groups[index].name ,
                    type: 'DELETE',
                    complete: function(){
                        self._securedGet(URLS["apiUser"] + '?page=0', self._displayUsers);
                    },
                }, accessToken);
            }
        },
        
        /** Deletes some users, the ones which have been checked in the form 
         * It asks a confirmation before the deletion
         */
        _deleteUser: function(){
            var groups = this.options.result;
            var groupToDelete = [];
            $('input.group-checkbox').each(function(index, element){
                if (element.checked) {
                    groupToDelete.push(index);
                }
            });
            var message;
            message = (groupToDelete.length > 1) ? l("confirmUsersDeletionBegin") : l("confirmUserDeletionBegin");
            
            for (element in groupToDelete) {
                message = message.concat(groups[groupToDelete[element]].name + ",");
            }
            message = message.concat(l("confirmUserDeletionEnd"));
            var answer = confirm(message);
            
            
            if (answer) {
                /* there is actually one request for each user to delete*/
                for (element in groupToDelete) {
                    var accessToken = this.options.context.session('accessToken');
                    
                    /* We delete the user */
                    $.oauth2Ajax({
                        url: URLS["apiUserName"] + this.options.userName+URLS["apiUserGroupsList"]+ groups[groupToDelete[element]].name,
                        type: 'DELETE',
                        complete: function(){
                            self._securedGet(URLS["apiUser"] + '?page=0', self._displayUsers);
                        },
                    }, accessToken);
                }
            }
        }
    };
    var self;
    var l = function(string){
        return string.toLocaleString()
    };
    $.widget("identity.userGroupsList", $.resthub.resthubController, userGroupsList);
})(jQuery);
