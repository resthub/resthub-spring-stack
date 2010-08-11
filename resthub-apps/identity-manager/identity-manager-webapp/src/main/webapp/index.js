
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {

        this.use(Sammy.Title);
        
        this.get('#/', function() {
            this.title('Welcome to Identity Manager');
            $('#content').render('components/home.html', {})
        });

		/**
         * View users.
         */
        this.get('#/user/list', function(context) {
			this.title('Identity Manager - User list');
			dominoes("components/user/list.js", function() {
				$.ajax({
					url: 'api/user/',
					dataType: 'json',
					success: function(data) {
						console.log($.toJSON(data));
						$('#content').listUsers({
							data: data,
							context: context
						});
					}
				});
			});
        });

		/**
         * View user.
         */
        this.get('#/user/:id', function(context) {
			this.title('Identity Manager - User details');
            var id = this.params['id'];
			dominoes("components/user/view.js", function() {
				$.ajax({
					url: 'api/user/'+ id,
					dataType: 'json',
					success: function(data) {
						$('#content').viewUser({
							data: data,
							context: context
						});
					}
				});
			});
        });

		/**
         * View groups.
         */
        this.get('#/group/list', function(context) {
			this.title('Identity Manager - Group list');
			dominoes("components/group/list.js", function() {
				$.ajax({
					url: 'api/group/',
					dataType: 'json',
					success: function(data) {
						console.log($.toJSON(data));
						$('#content').listGroups({
							data: data,
							context: context
						});
					}
				});
			});
        });

		/**
         * View group.
         */
        this.get('#/group/:id', function(context) {
			this.title('Identity Manager - Group details');
            var id = this.params['id'];
			dominoes("components/group/view.js", function() {
				$.ajax({
					url: 'api/group/'+ id,
					dataType: 'json',
					success: function(data) {
						console.log($.toJSON(data));
						$('#content').viewGroup({
							data: data,
							context: context
						});
					}
				});
			});
        });
    });

    $(function() {
        app.run('#/');
    });
})(jQuery);
