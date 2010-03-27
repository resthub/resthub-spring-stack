(function($) {

	/**
	 * Fonction qui parse le WADL pour générer des formulaire de get sur les
	 * méthodes du webService.
	 * 
	 * Doit s'appliquer à un document XML. 
	 * 
	 * TODO Prend en paramètre :
	 * - result: un noeud DOM où injecter les formulaires générés.
	 */
	$.fn.wadlParse = function(options) {
		// Merge les paramètre passés et les valeurs par défaut.
		var opts = $.extend({}, $.fn.wadlParse.defaults, options);
		
		this.each(function(i) {
			// Récupère les différentes Resources racine du WebService.
			$("resources > resource", this).each(function(idx, resource) {
				// Extrait les infos de la resource.
				var resourceName= $(resource).attr("path");
				// Pour chaque resource disponible :
				opts.result.append($("<br/><div class='ui-widget-header " +
						"ui-corner-all resource'>" + resourceName + "</div>"));
				// Génère les boutons pour les méthodes GET.
				generateGet(resource);
			});
		});
	 
		/**
		 * Méthode privée qui génère un bouton de get pour chaque méthode
		 * GET d'une resource donnée. Si la méthode a des besoins de
		 * paramètre, les input de formulaire sont généré aussi.
		 * 
		 * @param resource
		 *            	le noeud XML de la resource que l'on parse.
		 */
		function generateGet(resource) {
			$("method", resource).each(function(idx, method) {
				// Récupère le path
				var path = "";
				$(method).parents().each(function (i, parent) {
					var parentPath = $(parent).attr("path");
					if(parentPath) {
						path = parentPath + path;
					}
				});
				// Extrait les informations de la méthode.
				var methodType = $(method).attr("name");
				var methodName = $(method).attr("id");
				var form = $("<div class='method'><div>"+path+"</div></div>");
				var id = path.replace("/", "") + "-" + methodName;

				// Si besoin, génère un combobox pour le choix du fromat de 
				// réponse.
				var respFormat = $("> response > representation", method);
				if (respFormat.size() >= 1) {
					var listRespFormat = "<span>Format de sortie : ";
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

				// Génère un bouton avec l'id de la méthode.
				var b = $("<div/>").button({
					label: methodType
				}).click(function(){
					// Ouvre une popup pour afficher le résultat.
					var popup = $("<pre></pre>").dialog({
						modal:true,
						title: methodType + " sur " + id,
						width: "60%",
						height: "50%"
					});
					$.ajax({
						url: opts.rootPath + path,
						type: methodType,
						beforeSend: function(xhr) {
							// Récupère le représentation
							var accept = $("#"+id+"resp", form).val();
							// Modification des headers avant l'appel.
							xhr.setRequestHeader("Accept", accept);
						},
						success: function(xml, status, xhr){
							// Insère dans la popup le résultat de la requête.
							popup.text(xhr.responseText);
						}
					});
				});
				// Ajoute ce bouton dans le résultat.
				opts.result.append(form.append(b));
			}); 
		};
	     
		return this;
	 
	};
	   
	$.fn.wadlParse.defaults = {
			// Element dans lequel on générera les formulaire de test.
			result:null,
			// Racine de l'url du webService RESTFull.
			rootPath:""
	};
	
})(jQuery);
