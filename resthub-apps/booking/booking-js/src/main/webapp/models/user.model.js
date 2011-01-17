define(['jsmodel'], function() {
	var User = Model("user", {
		persistence: Model.RestPersistence("api/user")
		
		// Class methods
		}, {
		// Instance methods
	});
});