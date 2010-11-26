/**
 * Routes
 */
(function($){
    /**Intercepts web local request*/
    var app = $.sammy(function(){
    
        this.use(Sammy.Title);
        this.use(Sammy.Session);
        
        /** Home page */
        this.get('#/', function(){
            this.title(l("jsTitleRoot"));
        });
        
        /** List users */
        this.get('#/user/list', function(context){
            this.title(l("jsTitleUserList"));
            $('#content').listUsers({
                context: context
            });
        }, URLS["jsUserList"]);
        
        /** Create user */
        this.get('#/user/create', function(context){
            this.title(l("jsTitleUserCreate"));
            $('#content').editUser({
                context: context,
                mode: 'create'
            });
        }, URLS["jsUserEdit"]);
		
		 /** Change User password */
        this.get('#/user/password', function(context){
            this.title(l("jsTitleUserPassword"));
            $('#content').editPassword({
                context: context,
            });
        }, URLS["jsUserPassword"]);
        
        /** View user details */
        this.get('#/user/details/:login', function(context){
            this.title(l("jsTitleUserDetails"));
            $('#content').viewUser({
                context: context,
                userLogin: this.params.login
            });
        }, URLS["jsUserView"]);
        
        /** List groups */
        this.get('#/group/list', function(context){
            this.title(l("jsTitleGroupList"));
            $('#content').listGroups({
                context: context
            });
        }, URLS["jsGroupList"]);
        
        /** View list group from user  */
        this.get('#/user/groups/:name', function(context){
            this.title(l("jsTitleUserGroupDetails"));
            $('#content').userGroupsList({
                context: context,
                userName: this.params.name
            });
        }, URLS["jsUserGroupsList"]);
        
        /** Create group*/
        this.get('#/group/create', function(context){
            this.title(l("jsTitleGroupCreate"));
            $('#content').editGroup({
                context: context,
                mode: 'create'
            });
        }, URLS["jsGroupEdit"]);
        
        /** Get Login page*/
        this.get('#/user/login', function(context){
            this.title(l("jsTitleUserLogin"));
            $('#content').loginUsers({
                context: context
            });
        }, URLS["jsUserLoginForm"]);
        
        /** Authenticate user*/
        this.post('#/user/login', function(ev){
            this.title(l("jsTitleUserAuthenticate"));
            $('#content').postLoginUsers({
                context: ev,
                userLogin: ev.params['Login'],
                userPassword: ev.params['Password'],
                OAuth2EndPoint: ev.params['OAuth2EndPoint']
            });
        }, URLS["jsUserLoginProcess"]);
        
        this.get('#/user/logout', function(context){
            $('#content').logoutUser({
                context: context
            });
        }, URLS["jsUserLogout"]);
        
        
        /** Cancel all unknown get
         * this has to be keep as last input
         */
        this.get("#(.*)", function(){
            $('#content').html("");
        })
        
        var l = function(string){
            return string.toLocaleString();
        };
    });
    var l = function(string){
        return string.toLocaleString();
    }
    $(function(){
        app.run('#/');
    });
    
    
})(jQuery);


