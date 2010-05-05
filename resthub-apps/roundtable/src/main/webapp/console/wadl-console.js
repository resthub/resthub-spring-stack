$.widget("resthub.wadlConsole", {
    options: {
		url: 'api/application.wadl',
		rootPath: ''
	},
	
	_init: function()  {
		var self = this;
		
		var ui = '<form id="wadl-console-form">' +
        	'<label>WADL URL :</label>' +
            '<input id="wadl-console-url" type="text" value="' + this.options.url + '"/><input value="Load" type="submit"/>' +
        	'</form>' +
			'<br />' +
			'<div id="resultPanel" class="results-wrapper ui-widget-content ui-corner-all">' +
			'<h3 class="ui-widget-header ui-corner-all">WADL</h3>' +
			'<pre id="results">No WADL loaded</pre>' +
			'</div>' +
			'<div id="wadl-console-result"></div>';

		this.element.html(ui);
		
		// Result panel could be open and closed
		$("#resultPanel h3").click(function() {
			$("#results").slideToggle();
		});
		
		$("#wadl-console-form").submit(function(event){
            var url = $("#wadl-console-url").val();
            // Fait un appel AJAX et stope l'événemnet.
            self._loadWadl(url);
			event.preventDefault();
			event.stopPropagation();
        });

	},
	
	
	_loadWadl: function (url){
		var self = this;
        if (url) {
            $.get(url, null, 
				// Process ajax response
				function(xml, status, xhr){
					if (status != "success") {
						alert("URL '" + url + "' is unreacheable !");
					} else {
						self.options.rootPath = url.substring(0, url.lastIndexOf("/"));
                        // Display response un the debug panel
						$("#results").text(xhr.responseText);
						// Build form
						self._parseWadl(xml);	
					}
                });
        } else {
            alert("URL is empty !");
        }
    },
	
	_parseWadl : function(wadlXmlData, resultElement) {
		var self = this;
		$(wadlXmlData).each(function(i) {
			// For each available ressource
			$("resources > resource", this).each(function(idx, resource) {
				// Extract resource informations
				var resourceName= $(resource).attr("path");
				$("#wadl-console-result").append($("<br/><div class='ui-widget-header " +
						"ui-corner-all resource'>" + resourceName + "</div>"));
				// Generate buttons for GET methods
				self.generateGet(resource, self.options.rootPath);
			});
		});
	},
	 
		/**
		 * Private function that generate a GET buttonfor each GET ressource methods  
		 * 
		 * @param resource
		 *     XML node of the resource to parse
		 */
	generateGet : function (resource, rootPath) {
			$("method", resource).each(function(idx, method) {
				// Retreive path
				var path = "";
				$(method).parents().each(function (i, parent) {
					var parentPath = $(parent).attr("path");
					if(parentPath) {
						path = parentPath + path;
					}
				});
				// Extract method information
				var methodType = $(method).attr("name");
				var methodName = $(method).attr("id");
				var form = $("<div class='method'><div>"+path+"</div></div>");
				var id = path.replace("/", "") + "-" + methodName;

				// If needed, generate a combobox for reponse format choice
				var respFormat = $("> response > representation", method);
				if (respFormat.size() >= 1) {
					var listRespFormat = "<span>Output format : ";
					if (respFormat.size() > 1) {
						listRespFormat += "<select id='"+id+"resp'>";
						respFormat.each(function(i, format) {
							listRespFormat += "<option>" + $(format).
								attr("mediaType") + "</option>";
						});
						listRespFormat += "</select></span>";
					} else {
						listRespFormat += $(respFormat.get(0)).attr("mediaType") 
							+ "</span>";
					}
					form.append($(listRespFormat)).append($("<br/>"));
				}

				// Generate a button with method ID
				var b = $("<div/>").button({
					label: methodType
				}).click(function(){
					// Open a popup to display results
					var popup = $("<pre></pre>").dialog({
						modal:true,
						title: methodType + " on " + id,
						width: "60%",
						height: "400"
					});
					$.ajax({
						url: rootPath + path,
						type: methodType,
						beforeSend: function(xhr) {
							// Retreive presentation
							var accept = $("#"+id+"resp", form).val();
							// Modify headers before the call
							xhr.setRequestHeader("Accept", accept);
						},
						success: function(xml, status, xhr){
							// Insert result in the popup
							popup.text(xhr.responseText.replace( />/g, ">\n" ));
						}
					});
				});
				// Add this button in the result
				$("#wadl-console-result").append(form.append(b));
			}); 
		}

});
