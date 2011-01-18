define([ 'jquery.model' ], function() {

	Model.extend("User", {
		init : function() {
			this.root = 'api/user/';
		},
		check: function(callback, data) {
			this._post(this.root + 'check/', callback, data);
		}

	}, {});
});