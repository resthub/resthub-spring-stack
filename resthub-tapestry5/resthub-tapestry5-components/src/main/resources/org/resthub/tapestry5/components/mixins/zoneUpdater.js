// A class that updates a zone on any client-side event.
// Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
// and some help from Inge Solvoll.

function addRequestParameter(name, value, url) {
	if (url.indexOf('?') < 0) {
		url += '?';
	} else {
		url += '&';
	}
	value = escape(value);
	url += name + '=' + value;
	return url;
}

var ZoneUpdater = Class.create({

	initialize : function(spec) {
		this.element = $(spec.elementId);
		this.url = spec.url;
		$T(this.element).zoneId = spec.zone;
		if (spec.event) {
			this.event = spec.event;
			this.element.observe(this.event, this.updateZone.bindAsEventListener(this));
		}
	},
	
	updateZone : function() {
		var zoneManager = Tapestry.findZoneManager(this.element);
		
		if (!zoneManager) {
			return;
		}

		var updatedUrl = this.url;
		
		if (this.element.value) {
			var param = this.element.value;
			if (param) {
				updatedUrl = addRequestParameter('param', param, updatedUrl);
			}
		}
		zoneManager.updateFromURL(updatedUrl);
	}
	
});
