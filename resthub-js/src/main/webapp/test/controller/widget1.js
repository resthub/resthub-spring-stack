define(['controller'], function(Controller) {
	Controller.extend("Widget1", {
			
		init: function() {
			this.template = 'widget1.html';
			this.render();			
		}
	});
});
