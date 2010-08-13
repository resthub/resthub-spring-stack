(function($) {

var listUsers =
{
	options: {
		template: 'components/user/list.html',
		context: null,
		page: 0
	},
	_init: function() {
		this._switchPage( this.options.page );
	},
	_displayUsers: function(result) {
		this.element.render(this.options.template, {result: result});

		var self = this;
		$( 'span.page-switcher' ).each( function(index, element) {
			$(element).click( function() {
				var page = $(this).attr('id').split("-")[1];
				self._switchPage(page);
			});
		});

		$("table tr:nth-child(even)").addClass("striped");
	},
	_switchPage: function(page) {
		this.options.page = page;
		this._get( 'api/user?page=' + this.options.page, this._displayUsers );
	}
};

$.widget("identity.listUsers", $.resthub.resthubController, listUsers);
})(jQuery);