define([ 'lib/oauth2repository' ], function(OAuth2Repository) {
	return OAuth2Repository.extend("BookingRepository", {
		root : 'api/booking/'
	}, {});
});
