(function($){
    /**Used to display list of Users*/
    var groupGroupsList = {
        options: {
            template: URLS["templateGroupGroupsList"],
            context: null,
            page: 0,
            groupName: null,
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
        _displayGroups: function(result){
            this.element.render(this.options.template, {
                result: result,
				name : this.options.groupName
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
                self._removeGroup();
            });
            
            $('span.remove-user').each(function(index, element){
                $(element).click(function(){
                    self._removeThisGroup($(element).attr('id'));
                });
            });
            
        },
        /**Swiths to the page
         * @param {Integer} page
         * the page to display
         */
        _switchPage: function(page){
            this.options.page = page;
            this._securedGet(URLS["apiGroupName"] + this.options.groupName + URLS["apiGroupGroupsList"], this._displayGroups);
        },
        
        /** 
         * Deletes 1 user, the one on which there was a click
         * It asks a confirmation before the deletion
         *
         * @param {Integer} index
         * the index of the user to delete
         */
        _removeThisGroup: function(index){
            var groups = this.options.result;
            
            var answer = confirm(l("confirmGroupGroupDeletionBegin") + groups[index].name + l("confirmGroupGroupDeletionEnd"));
            if (answer) {
                var accessToken = this.options.context.session('accessToken');
                $.oauth2Ajax({
                    url: URLS["apiGroupName"] + this.options.groupName + URLS["apiGroupGroupsList"] + groups[index].name,
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
        _removeGroup: function(){
            var groups = this.options.result;
            var groupToDelete = [];
            $('input.group-checkbox').each(function(index, element){
                if (element.checked) {
                    groupToDelete.push(index);
                }
            });
            var message;
            message = (groupToDelete.length > 1) ? l("confirmUserGroupsDeletionBegin") : l("confirmUserGroupDeletionBegin");
            
            for (element in groupToDelete) {
                message = message.concat(groups[groupToDelete[element]].name + ",");
            }
            message = message.concat(l("confirmUserGroupDeletionEnd"));
            var answer = confirm(message);
            
            
            if (answer) {
                /* there is actually one request for each user to delete*/
                for (element in groupToDelete) {
                    var accessToken = this.options.context.session('accessToken');
                    
                    /* We delete the user */
                    $.oauth2Ajax({
                        url: URLS["apiGroupName"] + this.options.groupName + URLS["apiGroupGroupsList"] + groups[groupToDelete[element]].name,
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
    $.widget("identity.groupGroupsList", $.resthub.resthubController, groupGroupsList);
})(jQuery);
