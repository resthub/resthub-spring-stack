(function($) {

var listGroups =
{
	options: {
		template: URLS["templateGroupList"],
		context: null,
		page: 0
	},
	_init: function() {
		this._switchPage( this.options.page );
	},
	_displayGroups: function(result) {
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
		this._securedGet( URLS["apiGroup"]+'?page=' + this.options.page, this._displayGroups );
	}
};

$.widget("identity.listGroups", $.resthub.resthubController, listGroups);
})(jQuery);