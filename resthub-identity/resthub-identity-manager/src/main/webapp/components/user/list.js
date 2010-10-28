(function($){

    var listUsers = {
        options: {
            template: 'components/user/list.html',
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
        },
        _switchPage: function(page){
            this.options.page = page;
            this._get('api/user?page=' + this.options.page, this._displayUsers);
        },
        
        _deleteUser: function(){
            var users = this.options.result.elements;
            $('input.user-checkbox').each(function(index, element){
                if (element.checked) {
                    var answer = confirm(l("confirmUserDeletionBegin")+ users[index].login +l("confirmUserDeletionEnd"));
                }
                if (answer) {
                    /* We delete the user */
                    $.ajax({
                        url: 'api/user/' + users[index].id,
                        type: 'DELETE',
                        success: function(){
                            self._get('api/user?page=0', self._displayUsers);
                        }
                    });
                }
            });
            
        }
    };
    var self;
    $.widget("identity.listUsers", $.resthub.resthubController, listUsers);
})(jQuery);
