(function($){

    var listUsers = {
        options: {
            template: URLS["templateUserList"],
            context: null,
            page: 0,
            result: null
        },
        _init: function(){
            self = this;
            this._switchPage(this.options.page);
        },
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
        _switchPage: function(page){
            this.options.page = page;
            this._securedGet(URLS["apiUser"] + '?page=' + this.options.page, this._displayUsers);
        },
        
        /* 
         * this methode allow to delete 1 user, the one on which there was a click
         * It asks a confirmation before the deletion
         */
        _deleteThisUser: function(index){
            var users = this.options.result.elements;
            
            var answer = confirm(l("confirmUserDeletionBegin") + users[index].login + l("confirmUserDeletionEnd"));
            if (answer) {
                var accessToken = this.options.context.session('accessToken');
                $.oauth2Ajax({
                    url: URLS["apiUser"] + users[index].id,
                    type: 'DELETE',
                    complete: function(){
                        self._securedGet(URLS["apiUser"] + '?page=0', self._displayUsers);
                    },
                }, accessToken);
            }
        },
        
        /* this method allow to delete some users, the ones which have been checked 
         * It asks a confirmation before the deletion
         */
        _deleteUser: function(){
            var users = this.options.result.elements;
            var userToDelete = [];
            $('input.user-checkbox').each(function(index, element){
                if (element.checked) {
                    userToDelete.push(index);
                }
            });
            var message;
            message = (userToDelete.length > 1) ? l("confirmUsersDeletionBegin") : l("confirmUserDeletionBegin");
            
            for (element in userToDelete) {
                message = message.concat(users[userToDelete[element]].login + ",");
            }
            message = message.concat(l("confirmUserDeletionEnd"));
            var answer = confirm(message);
            
            
            if (answer) {
                /* there is actually one request for each user to delete*/
                for (element in userToDelete) {
                    var accessToken = this.options.context.session('accessToken');
                    
                    /* We delete the user */
                    $.oauth2Ajax({
                        url: URLS["apiUser"] + users[userToDelete[element]].id,
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
    $.widget("identity.listUsers", $.resthub.resthubController, listUsers);
})(jQuery);
