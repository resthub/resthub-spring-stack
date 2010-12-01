(function($){
    /** Used for edition of Group in the webapps */
    var editGroup = {
        options: {
            template: URLS["templateGroupEdit"],
            context: null,
        },
        _init: function(){
            this._securedGet(URLS["apiGroupList"], this._setGroups);
        },
        _setGroups: function(groups){
            this.options.groups = groups;
            this._displayGroupForm();
        },
        /**Displays and render the Group form*/
        _displayGroupForm: function(){
            var group = this.options.context.session('tempGroup');
            var perm = {};
            if (!$.isEmptyObject(group) && !$.isEmptyObject(group.permissions)) {
                perm = group.permissions;
            }
            this.element.render(this.options.template, {
                group: group,
                groups: this.options.groups
            });
            
            $('#content h1:first').html(l("GroupCreate"));
            
            this._sessionToForm();
            
			/**render the part linked to permissions */
            $("div#permissionCheckBoxDiv").render(URLS["templateGenericPermissionsCheckBox"], {
                    permissions: perm
                });
			
			$('form#group-form').validate({
                errorElement: 'span'
            });
            
			
			
            $('input#group-proceed').unbind();
            $('input#group-proceed').bind('click', $.proxy(this._sendGroupData, this));
			
			 $('input#entityAddPermissionButton').click(function(){
				var val = $('input#entityAddPermission').val();
				if(val!=null &&val!=""){
				                var perms = [];
                $('input[name=entityPermission]:checked').each(function(index, element){
                    perms.push($(element).attr('value') );
                });
                
                perms.push($('input#entityAddPermission').val());
				
                $("div#permissionCheckBoxDiv").render(URLS["templateGenericPermissionsCheckBox"], {
                    permissions: perms
                });
				
				$('input#entityAddPermissionButton').click(arguments.callee);}
            });
            
        },
        
        /** Tests if the form is filled correctly and sends the data */
        _sendGroupData: function(){
            var validForm = $('form#group-form').validate({
                errorElement: 'span'
            }).form();
            if (validForm) {
                this._formToSession();
                var group = this.options.context.session('tempGroup');
                this._securedPost(URLS["apiGroup"], this._endOfProcess, $.toJSON(group));
            }
        },
        /** Puts form data in session */
        _formToSession: function(){
            var group = {
				groups:[],
                permissions: []
            };
            group.name = $('input[name=name]').val();
			$('input[name=groupgroup]:checked').each(function(index, element){
                group.groups.push({
                    id: $(element).attr('value')
                });
            });
			$('input[name=entityPermission]:checked').each(function(index, element){
                    group.permissions.push($(element).attr('value') );
                });
            this.options.context.session('tempGroup', group);
        },
        /** Displays session data in user form */
        _sessionToForm: function(){
            var group = this.options.context.session('tempGroup');
            if (group != null) {
                $('input[name=name]').val(group.name);
            }
        },
        /**
         * Cleans the temporary objects when everything done and redirect to the group details view
         * @param {Group} group
         * the edited Group
         */
        _endOfProcess: function(group){
            //console.log(user);
            // Cleans the tempUser in session
            this.options.context.session('tempGroup', null);
            this.options.context.redirect('#/group/list');
        }
    };
    /**Variable used for text localization with l10n */
    var l = function(string){
        return string.toLocaleString()
    };
    
    $.widget("booking.editGroup", $.resthub.resthubController, editGroup);
})(jQuery);
