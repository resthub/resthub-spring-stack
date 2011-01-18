define([ 'jquery.dao'], function(CrudDao) {
	return CrudDao
		.define("Booking")
		.supplement({ root: 'api/booking/' });
});
