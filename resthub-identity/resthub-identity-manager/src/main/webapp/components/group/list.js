(function($){
    /**Used when listing the Groups*/
    var listGroups = {
        options: {
            template: URLS["templateGroupList"],
            context: null,
            page: 0
        },
        /**Init's the swichPage mode*/
        _init: function(){
            this._switchPage(this.options.page);
        },
        /**Displays and renders group list
         * 
         * 
         * @param {Object} result
         * Response with inside the list of groups
         */
        _displayGroups: function(result){
            this.element.render(this.options.template, {
                result: result
            });
            
			/**needed to propagate the context in callbacks function*/
            var self = this;
            /**To deal with Pagination of results*/
			$('span.page-switcher').each(function(index, element){
                $(element).click(function(){
                    var page = $(this).attr('id').split("-")[1];
                    self._switchPage(page);
                });
            });
			/**to delete severals users*/
             $('div#delete').click(function(){
                self._deleteGroups();
            });
			/**to delete one user*/
			  $('span.remove-group').each(function(index, element){
                $(element).click(function(){
                    self._deleteThisGroup($(element).attr('id'));
                });
            });
            
            $("table tr:nth-child(even)").addClass("striped");
        },
        /**
         * Switchs the page
         *
         * @param {Integer} page
         * the page to be displayed
         */
        _switchPage: function(page){
            this.options.page = page;
            this._securedGet(URLS["apiGroup"] + '?page=' + this.options.page, this._changedResult);
        },
    
	/**
     * Callback function called when the result list has been changed
     *
     * @param result
     * 		the result object 
     */
    _changedResult: function(result){
		this.options.result =  result;
		this._displayGroups(result);
	},
	/** 
         * Deletes 1 user, the one on which there was a click
         * It asks a confirmation before the deletion
         *
         * @param {Integer} index
         * the index of the user to delete
         */
        _deleteThisGroup:function(index){
			var groups = this.options.result.elements;
			var self = this;
            
            var answer = confirm(l("confirmGroupDeletionBegin") + groups[index].name + l("confirmGroupsDeletionEnd"));
            if (answer) {
                var accessToken = this.options.context.session('accessToken');
                $.oauth2Ajax({
                    url: URLS["apiGroup"] + groups[index].id,
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
		_deleteGroups: function(){						
		var self = this;
		var groups = this.options.result.elements;
            var groupsToDelete = [];
            $('input.group-checkbox').each(function(index, element){
                if (element.checked) {
                    groupsToDelete.push(index);
                }
            });
            var message;
            message = (groupsToDelete.length > 1) ? l("confirmGroupsDeletionBegin") : l("confirmGroupDeletionBegin");
            
            for (element in groupsToDelete) {
                message = message.concat(groups[groupsToDelete[element]].name + ",");
            }
            message = message.concat(l("confirmGroupDeletionEnd"));
            var answer = confirm(message);
            
            
            if (answer) {
                /** there is actually one request for each user to delete*/
                for (element in groupsToDelete) {
                    var accessToken = this.options.context.session('accessToken');
                    
                    /** We delete the user */
                    $.oauth2Ajax({
                        url: URLS["apiGroup"] + groups[groupsToDelete[element]].id,
                        type: 'DELETE',
                        complete: function(){
                      		 self._switchPage(self.options.page);
					    },
                    }, accessToken);
                }
            }
        }
		};
    
	/**Variable used for text localization with l10n*/
	var l = function(string){ return string.toLocaleString()};
	
    $.widget("identity.listGroups", $.resthub.resthubController, listGroups);
})(jQuery);
