/**
 * Routes
 */
(function($) {
	
	var app = $.sammy(function() {

		this.use(Sammy.Title);

		/* Home page */
		this.get('#/', function() {
			this.title('Welcome to Identity Manager');
		});

		/* List users */
		this.get('#/user/list', function(context) {
			this.title('Identity Manager - User list');
			$('#content').listUsers({context: context});
		}, 'components/user/list.js');

		/* View user details */
		this.get('#/user/:login', function(context) {
			this.title('Identity Manager - User details');
			$('#content').viewUser({ context: context, userLogin: this.params.login });
		}, 'components/user/view.js');


		/* List groups */
		this.get('#/group/list', function(context) {
			this.title('Identity Manager - Group list');
			$('#content').listGroups({context: context});
		}, 'components/group/list.js');

		/* View group details */
		this.get('#/group/:name', function(context) {
			this.title('Identity Manager - Group details');
			$('#content').viewGroup({ context: context, groupName: this.params.name });
		}, 'components/group/view.js');
	});

	$(function() {
		app.run('#/');
	});
})(jQuery);
