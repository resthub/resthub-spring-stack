/**
 * Routes
 */
(function($) {
	
	var app = $.sammy(function() {

		this.use(Sammy.Title);
		this.use(Sammy.Session);

		/* Home page */
		this.get('#/', function() {
			this.title(this.l("jsTitleRoot"));
		});

		/* List users */
		this.get('#/user/list', function(context) {
			this.title(l("jsTitleUserList"));
			$('#content').listUsers({context: context});
		}, URLS["jsUserList"]);

		/* Create user */
		this.get('#/user/create', function(context) {
			this.title(l("jsTitleUserCreate"));
			$('#content').editUser({ context: context, mode: 'create' });
		}, URLS["jsUserEdit"]);

		/* View user details */
		this.get('#/user/details/:login', function(context) {
			this.title(l("jsTitleUserDetails"));
			$('#content').viewUser({ context: context, userLogin: this.params.login });
		}, URLS["jsUserView"]);

		/* List groups */
		this.get('#/group/list', function(context) {
			this.title(l("jsTitleGroupList"));
			$('#content').listGroups({context: context});
		}, URLS["jsGroupList"]);

		/* View group details */
		this.get('#/group/details/:name', function(context) {
			this.title(l("jsTitleGroupDetails"));
			$('#content').viewGroup({ context: context, groupName: this.params.name });
		}, URLS["jsGroupView"] );
		
		/* Create group*/
		this.get('#/group/create', function(context) {
			this.title(l("jsTitleGroupCreate"));
			$('#content').editGroup({ context: context, mode: 'create' });
		}, URLS["jsGroupEdit"]);
		
		/* Get Login page*/
		this.get('#/user/login', function(context){
			this.title(l("jsTitleUserLogin"));
			$('#content').loginUsers({context:context});},
			URLS["jsUserLoginForm"]);			
		
		/* Authenticate user*/			
		this.post('#/user/login', function(ev){
			this.title(l("jsTitleUserAuthenticate"));
			$('#content').postLoginUsers({
					context:ev, userLogin :ev.params['Login'],
					userPassword:ev.params['Password'] ,
					OAuth2EndPoint:ev.params['OAuth2EndPoint'] });
			},URLS["jsUserLoginProcess"]);		
		
		this.get('#/user/logout', function(context){
			$('#content').logoutUser({context:context});
		},URLS["jsUserLogout"]);			
		
			
		/* Cancel all unknown get
		 * this has to be keep as last input
		 */
		this.get("#(.*)",function(){
			$('#content').html("");
				})
	});

	$(function() {
		app.run('#/');
	});
})(jQuery);

l = function (string) {
  				  return string.toLocaleString();
			};


