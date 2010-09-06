/**
 *
 * Zoomimage
 * Author: Stefan Petre www.eyecon.ro
 * 
 */

(function($){
	EYE.extend({
		
		zoomimage: {
			libs: {},
			types: /\.jpg|\.jpeg|\.png|\.gif|\.bmp/g,
			current: null,
			moved: false,
			pointer: {x:0,y:0},
			diff: {x:0, y:0},
			trackKey: false,
			
			//default options (many options are controled via CSS)
			defaults: {
				opacity: 0.3, //caption opacity
				border: 0, // border arround the image
				shadow: 6, // shadow size
				duration: 300, // animation duration
				prevent: 14, // pixels to move the mouse before the image is dragged
				controls: true, // display controls
				caption: true, // display caption
				hideSource: false,
				centered: false,
				className: false,
				onLoad: function(){return false},
				beforeZoomIn: function(){return false},
				onZoomIn: function(){return false},
				beforeZoomOut: function(){return false},
				onZoomOut: function(){return false},
				onFocus: function(){return false},
				controlsTrigger: 'focus',
				easing: 'linear',
				preload: 'click'
			},
			
			// the template for the image's box
			template: [
				'<div class="zoomimage">',
					'<div class="zoomimage_s">',
						'<div class="zoomimage_st">',
							'<div class="zoomimage_stl"></div>',
							'<div class="zoomimage_stc"></div>',
							'<div class="zoomimage_str"></div>',
						'</div>',
						'<div class="zoomimage_sc">',
							'<div class="zoomimage_scl"></div>',
							'<div class="zoomimage_scc"></div>',
							'<div class="zoomimage_scr"></div>',
						'</div>',
						'<div class="zoomimage_sb">',
							'<div class="zoomimage_sbl"></div>',
							'<div class="zoomimage_sbc"></div>',
							'<div class="zoomimage_sbr"></div>',
						'</div>',
					'</div>',
					'<img src="" />',
					'<div class="zoomimage_controls">',
						'<a href="#" class="zoomimage_prev"></a>',
						'<a href="#" class="zoomimage_next"></a>',
					'</div>',
					'<div class="zoomimage_caption"></div>',
					'<div class="zoomimage_loading"></div>',
				'</div>'
			],
			
			// handle click on the trigger
			click: function(e) {
				var el = this;
				el.blur();
				// if the image was not preloaded yet then wait
				if (el.zoomimageCfg.loading === true) {
					return false;
				}
				//zoom it in if not zoomed already
				if (el.zoomimageCfg.zoomed == false) {
					EYE.zoomimage.zoomIn(el);
				//else zoom it out
				} else {
					EYE.zoomimage.zoomOut(el, false);
				}
				return false;
			},
			
			// zoom in the image
			zoomIn: function(el) {
				//if the image was not loaded yet then wait
				if (el.zoomimageCfg.loaded === false) {
					//if the image is not preloading then start preloading
					if (el.zoomimageCfg.loading != true) {
						el.zoomimageCfg.loading = true;
						EYE.zoomimage.preload(el);
					}
					return;
				}
				//if the image is zoomed in then just focus it
				if (el.zoomimageCfg.zoomed == true) {
					EYE.zoomimage.focus(el);
					return;
				}
				el.zoomimageCfg.beforeZoomIn.apply(el,[el.zoomimageCfg.box]);
				
				var elPos = EYE.getPosition(el, true);
				var elHeight = el.offsetHeight;
				var elWidth = el.offsetWidth;
				var pos = EYE.getScroll();
				var borderAndShadow = el.zoomimageCfg.border + el.zoomimageCfg.shadow;
				var width = el.zoomimageCfg.width + borderAndShadow * 2; 
				var height = el.zoomimageCfg.height + borderAndShadow * 2;
				var screenRatio = pos.iw/pos.ih;
				var imageRatio = el.zoomimageCfg.width/el.zoomimageCfg.height;
				
				// if the image is bigger then the viewport then resize the image to fit
				if (screenRatio > imageRatio) {
					if (height > pos.ih) {
						height = pos.ih;
						width = parseInt(height * imageRatio,10);
					}
				} else if (width > pos.iw) {
					width = pos.iw;
					height = parseInt(width / imageRatio, 10);
				}
				//if the image should be centered then do that, else center to trigger's position but do not leave the viewport
				var top = el.zoomimageCfg.centered ? 
							pos.t + parseInt((pos.ih - height)/2, 10)
							: 
							Math.min(
								Math.max(
									pos.t, 
									elPos.y + (elHeight - height)/2 - borderAndShadow
								), 
								pos.t + pos.ih - height
							);
				var left = el.zoomimageCfg.centered ? 
							pos.l + parseInt((pos.iw - width)/2, 10)
							:
							Math.min(
								Math.max(
									pos.l, 
									elPos.x + (elWidth - width)/2 - borderAndShadow 
								), 
								pos.l + pos.iw - width
							);
				var imgWidth = width - borderAndShadow * 2;
				var imgHeight = height - borderAndShadow * 2;
				
				if(el.zoomimageCfg.hideSource === true) {
					el.style.visibility = 'hidden';
				}
				
				//move the image's box and animated it
				$('#' + el.zoomimageCfg.box)
					.css({
						top: elPos.y + 'px',
						left: elPos.x + 'px',
						width: elWidth + 'px',
						height: elHeight + 'px'
					})
					.find('>div')
						.hide()
						.end()
					.find('img')
						.attr('src', el.zoomimageCfg.src)
						.css({
							top: 0,
							left: 0,
							width: '100%',
							height: '100%',
							display: 'block',
							borderWidth: '0px'
						})
						.end()
					.animate({
							width: imgWidth,
							height: imgHeight,
							top: top + borderAndShadow,
							left: left + borderAndShadow
						}, 
						el.zoomimageCfg.duration,
						el.zoomimageCfg.easing, 
						function(){
							$(this)
								.css({
									top: top + 'px',
									left: left + 'px',
									width: width + 'px',
									height: height + 'px'
								})
								.find('img')
									.css({
										top: el.zoomimageCfg.shadow + 'px',
										left: el.zoomimageCfg.shadow + 'px',
										width: imgWidth + 'px',
										height: imgHeight + 'px',
										borderWidth: el.zoomimageCfg.border + 'px'
									})
									.end()
								.find('>div:first')
									.find('div.zoomimage_sc')
										.css('height', height - el.zoomimageCfg.shadow*2 + 'px')
										.end()
									.show();
								el.zoomimageCfg.zoomed = true;
								EYE.zoomimage.focus(el);
								el.zoomimageCfg.onZoomIn.apply(el,[el.zoomimageCfg.box]);
						});
			},
			
			//focus image and show gallery controls if it is part of a gallery
			showControls: function(el) {
				if(el == undefined)
					return;
				if (el.zoomimageCfg == undefined) {
					el = $('#' + $(el).attr('zoomimage')).get(0);
				}
				var height,
					imgWidth,
					borderAndShadow = el.zoomimageCfg.border + el.zoomimageCfg.shadow;
				$('#' + el.zoomimageCfg.box)
					.find('img')
						.each(function(){
							imgWidth = parseInt($.curCSS(this, 'width'),10);
						})
						.end()
					.get(0).zoomimageControls = true;
				// if it has caption then display it
				if(el.zoomimageCfg.caption) {
					$('#' + el.zoomimageCfg.box)
					.find('>div:eq(2)')
						.stop()
						.css({
							bottom: borderAndShadow + 'px',
							left: borderAndShadow + 'px',
							width: imgWidth + 'px'
						})
						.show()
						.each(function() {
							this.style.height = 'auto';
							height = this.offsetHeight;
							this.style.height = '0';
						})
						.animate({height: height}, el.zoomimageCfg.duration);
				}
				//if it has controls then show them
				if(el.zoomimageCfg.controls) {
					// show controls only if it is part of a gallery
					if (EYE.zoomimage.libs[el.zoomimageCfg.lib] > 1) {
						$('#' + el.zoomimageCfg.box)
							.find('>div:eq(1)')
								.show()
								.each(function(){
									if (!el.zoomimageCfg.controlsHeight) {
										el.zoomimageCfg.controlsHeight = this.offsetHeight;
									}
									this.style.height = '0';
								})
								.css({
									top: borderAndShadow + 'px',
									left: borderAndShadow + 'px',
									width: imgWidth + 'px'
								})
								.animate({height: el.zoomimageCfg.controlsHeight}, el.zoomimageCfg.duration);
					}
				}
			},
			
			//zoom out the image and go to the next/previous if any
			zoomOut: function(el, goToNext) {
				var boxEl, elPos, borderAndShadow, elSize;
				// if the action was started by the trigger
				if (el.zoomimageCfg) {
					if (el.zoomimageCfg.zoomed === false) {
						return;
					}
					el.zoomimageCfg.beforeZoomOut.apply(el,[el.zoomimageCfg.box]);
					boxEl = document.getElementById(el.zoomimageCfg.box);
					
				// else try to find a link that has the same href as the image src
				} else {
					boxEl = el;
					el = $('a[href=' + $('img',boxEl).attr('src') + ']').get(0);
				}
				// the trigger was found so scale to image to trigger's size
				if (el) {
					elPos = EYE.getPosition(el, true);
					el.zoomimageCfg.zoomed = false;
					borderAndShadow = el.zoomimageCfg.border + el.zoomimageCfg.shadow;
					elSize = {
						width: el.offsetWidth,
						height: el.offsetHeight
					};
				// the trigger was not found so scale the image to its center
				} else {
					borderAndShadow = EYE.zoomimage.defaults.border + EYE.zoomimage.defaults.shadow;
					elSize = {
						width: 0,
						height: 0
					};
					elPos = EYE.getPosition(boxEl, true);
					elPos.y += parseInt(boxEl.offsetHeight/2, 10);
					elPos.x += parseInt(boxEl.offsetWidth/2, 10);
				}
				$(boxEl)
					.css({
						top: boxEl.offsetTop + borderAndShadow + 'px',
						left: boxEl.offsetLeft + borderAndShadow + 'px',
						width: boxEl.offsetWidth - borderAndShadow*2 + 'px',
						height: boxEl.offsetHeight - borderAndShadow*2 + 'px'
					})
					.find('>div')
						.stop()
						.hide()
						.end()
					.find('img')
						.css({
							top: 0,
							left: 0,
							width: '100%',
							height: '100%',
							borderWidth: '0px'
						})
						.end()
					.animate(
						{
							top: elPos.y + 'px',
							left: elPos.x + 'px',
							width: elSize.width + 'px',
							height: elSize.height + 'px'
						}, 
						// if the trigger was not found the use the default duration
						el ? el.zoomimageCfg.duration : EYE.zoomimage.defaults.duration,
						el.zoomimageCfg.easing,
						function() {
							//hide image and remove focus
							EYE.zoomimage.blur();
							$(this).hide();
							// if the trigger was found then aply callback and try to focus the next one zoomed
							if (el) {
								if(el.zoomimageCfg.hideSource === true) {
									el.style.visibility = 'visible';
								}
								el.zoomimageCfg.onZoomOut.apply(el,[el.zoomimageCfg.box]);
								if (!goToNext) {
									EYE.zoomimage.focus($('div.zoomimage:visible:last').not(':animated').get(0));
								}
							//the trigger was not found so remove the image since no trigger is present in the page
							} else {
								$(boxEl).stop().remove();
							}
						}
					);
			},
			
			mouseOver: function(e) {
				var triggerEl = document.getElementById($(this).attr('zoomimage'));
				if (triggerEl.zoomimageCfg.zoomed === true && this.zoomimageControls == false) {
					EYE.zoomimage.showControls(triggerEl);
				}
				return false;
			},
			
			mouseOut: function(e) {
				if ( !EYE.isChildOf(this, e.relatedTarget, this) ) {
					$(this)
						.find('>div:not(:first)')
							.stop()
							.hide();
					this.zoomimageControls  = false;
				}
				return false;
			},
			
			// prepare for possible drag
			mouseDown: function(e) {
				// find the trigger
				var triggerEl = document.getElementById($(this).attr('zoomimage'));
				//if the trigger was found then prepare informations for drag
				if (triggerEl) {
					$.extend(EYE.zoomimage,{
						current: this,
						prevent: triggerEl.zoomimageCfg.prevent,
						moved: false,
						diff: {
							x: e.pageX - this.offsetLeft,
							y: e.pageY - this.offsetTop
						},
						pointer: {
							x: e.pageX ,
							y: e.pageY
						}
					});
					$(document)
						.bind('mousemove', EYE.zoomimage.mouseMove)
						.bind('mouseup', EYE.zoomimage.mouseUp);
				// if the trigger was not found then it is an orphan and zoom it out
				} else {
					$(this).zoomimageClear();
				}
				return false;
			},
			
			//do the drag if prevent distance was overtake
			mouseMove: function(e) {
				var diffX = Math.abs(EYE.zoomimage.pointer.x - e.pageX);
				var diffY = Math.abs(EYE.zoomimage.pointer.y - e.pageY);
				//the prevent distance was not reached yet so we check if it is reached already
				if (EYE.zoomimage.moved === false) {
					if ( diffX > EYE.zoomimage.prevent|| diffY > EYE.zoomimage.prevent) {
						EYE.zoomimage.moved = true;
						$(EYE.zoomimage.current).addClass('zoomimage_move');
						if (!$(EYE.zoomimage.current).is('.zoomimage_focused')) {
							EYE.zoomimage.focus(EYE.zoomimage.current);
						}
					}
				// the prevent distance was overtake so the element can be moved
				} else {
					EYE.zoomimage.current.style.top = e.pageY - EYE.zoomimage.diff.y + 'px';
					EYE.zoomimage.current.style.left = e.pageX - EYE.zoomimage.diff.x + 'px';
				}
				return false;
			},
			
			//the drag stops
			mouseUp: function (e) {
				$(EYE.zoomimage.current).removeClass('zoomimage_move');
				EYE.zoomimage.current = null;
				$(document)
					.unbind('mousemove', EYE.zoomimage.mouseMove)
					.unbind('mouseup', EYE.zoomimage.mouseUp);
				return false;
			},
			
			// click on image
			imageClick: function(e) {
				$(document)
					.unbind('mousemove', EYE.zoomimage.mouseMove)
					.unbind('mouseup', EYE.zoomimage.mouseUp);
				var el = document.getElementById($(this).attr('zoomimage'));
				// if the trigger was found
				if (el) {
					//if the image was not moved but was focused
					if (EYE.zoomimage.moved === false && $(this).is('.zoomimage_focused')) {
						// if the event target is a link then it was a click on one of the controls and go to the next image
						if ($(e.target).is('a')) {
							EYE.zoomimage.zoomNext(el, e.target.className == 'zoomimage_next' ? 1 : -1);
							var goToNext = true;
						// else just zoom it out
						} else {
							EYE.zoomimage.zoomOut(el, goToNext||false);
						}
					// just focus the image
					} else if(!$(this).is('.zoomimage_focused')) {
						EYE.zoomimage.focus(this);
					}
				//the trigger was not found so the image is orphan and zoom it out
				} else {
					$(this).zoomimageClear();
				}
				return false;
			},
			
			//zoom out any opened image and clear orphan images
			clear: function() {
				var subject = this;
				if (subject.size() == 0) {
					subject = $('div.zoomimage');
				}
				return subject.each(function(){
					var triggerEl = document.getElementById($(this).attr('zoomimage'));
					if (triggerEl) {
						EYE.zoomimage.zoomOut(triggerEl, false);
					} else {
						EYE.zoomimage.zoomOut(this, false);
					}
				});
			},
			
			// zoom the next image in gallery
			zoomNext: function(el, dir) {
				if(el.zoomimageCfg.zoomed === false) {
					return;
				}
				EYE.zoomimage.zoomOut(el, true);
				var nextImg = el.zoomimageCfg.iteration + dir;
				var lib = $(el).attr('zoomimage');
				var maxImg = EYE.zoomimage.libs[lib];
				if (nextImg < 0) {
					nextImg = maxImg - 1;
				} else if (nextImg >= maxImg) {
					nextImg = 0;
				}
				EYE.zoomimage.zoomIn($('a[zoomimage="' + lib + '"]').get(nextImg));
			},
			
			//hande any key pressed
			keyPressed: function(e) {
				var el = $('div.zoomimage_focused');
				if (el.size() == 1) {
					var pressedKey = e.charCode || e.keyCode || -1;
					el = $('#' + $(el).attr('zoomimage')).get(0);
					var lib = $(el).attr('zoomimage');
					switch (pressedKey)
					{
						//end
						case 35:
							// go to the last image in the gallery
							if (EYE.zoomimage.libs[lib] > 1 &&  EYE.zoomimage.libs[lib] - 1 != el.zoomimageCfg.iteration) {
								EYE.zoomimage.zoomNext(el, EYE.zoomimage.libs[lib] - el.zoomimageCfg.iteration - 1);
								return false;
							}
						break;
						//home
						case 36:
							// go to the first image in the gallery
							if (EYE.zoomimage.libs[lib] > 1 && el.zoomimageCfg.iteration != 0) {
								EYE.zoomimage.zoomNext(el, - el.zoomimageCfg.iteration);
								return false;
							}
						break;
						//down;
						case 40:
						//left
						case 37:
						//backspace
						case 8:
						//page up
						case 33:
						//p
						case 80:
						case 112:
							// go to the previous image in the gallery
							if (EYE.zoomimage.libs[lib] > 1) {
								EYE.zoomimage.zoomNext(el, -1);
								return false;
							}
						break;
						//up
						case 38:
						//right
						case 39:
						//page down
						case 34:
						//space
						case 32:
						//n
						case 110:
						case 78:
							// go to the next image in the gallery
							if (EYE.zoomimage.libs[lib] > 1) {
								EYE.zoomimage.zoomNext(el, 1);
								return false;
							}
						break;
						//escape
						case 27:
							// well zoome out the curent image
							EYE.zoomimage.zoomOut(el, false);
							return false;
						break;
					}
				}
			},
			
			
			// focus on image
			focus: function(el) {
				if(el == undefined)
					return;
				if (el.zoomimageCfg == undefined) {
					el = $('#' + $(el).attr('zoomimage')).get(0);
				} else {
					var showControls = true;
				}
				// if another image is focused then remove focus
				EYE.zoomimage.blur(el);
						
				$('#' + el.zoomimageCfg.box)
					.not('.zoomimage_focused')
					.addClass('zoomimage_focused');
				el.zoomimageCfg.onFocus.apply(el,[el.zoomimageCfg.box]);
				if (el.zoomimageCfg.controlsTrigger == 'focus' || showControls) {
					EYE.zoomimage.showControls(el);
				}
			},
			
			//blur image
			blur: function(el) {
				$('div.zoomimage_focused')
					.not('#' + (el == undefined ? 'fakezoomimage' : el.zoomimageCfg.box))
					.removeClass('zoomimage_focused')
					.each(function(){
						this.zoomimageControls = false;
					})
					.find('>div:not(:first)')
						.stop()
						.hide();
			},
			
			preload: function(el) {
				// place the loading aimation on top
				var boxEl = $('#' + el.zoomimageCfg.box).show();
				boxEl.find('>div, img').hide();
				var elPos = EYE.getPosition(el, true);
				boxEl
					.find('>div:last')
						.show()
						.end()
					.css({
						top: elPos.y + 'px',
						left: elPos.x + 'px',
						width: el.offsetWidth + 'px',
						height: el.offsetHeight + 'px'
					});
				// preload the image
				var preld= new Image();
				preld.src = el.href;
				//if the image was laoded already
				if (preld.complete) {
					EYE.zoomimage.markPreloaded(preld, el);
				// else place a callback
				} else {
					preld.onload = function() {
						EYE.zoomimage.markPreloaded(preld, el);
					};
				}
			},
			
			markPreloaded: function(preld, el) {
				//mark image as loaded and remember the size and source
				$.extend(el.zoomimageCfg,{
					loaded: true,
					width: preld.width,
					height: preld.height,
					src: preld.src
				});
				// hide loading animation
				$('#' + el.zoomimageCfg.box)
					.find('div.zoomimage_loading')
					.hide();
				//if the image waits to be enlarged then zoom in
				if (el.zoomimageCfg.loading) {
					el.zoomimageCfg.loading = false;
					EYE.zoomimage.zoomIn(el);
				}
				el.zoomimageCfg.onLoad.apply(el,[el.zoomimageCfg.box]);
			},
			
			//constructor
			init: function(opt) {
				//generate a library key
				var libKey = parseInt(Math.random()*2000,10);
				//store the number of images in the library
				EYE.zoomimage.libs[libKey] = 0;
				opt = $.extend({lib:libKey}, EYE.zoomimage.defaults, opt||{});
				return this.each(function(){
					var jQEl = $(this);
					var el = this;
					//consider only the links pointing to an image
					if (el.href /*&& el.href.toLowerCase().match(EYE.zoomimage.types)*/ != null) {
						//store library options
						el.zoomimageCfg = $.extend({}, opt, {
							zoomed: false,
							loading: false,
							loaded: false,
							animated: false,
							src: el.href,
							iteration: EYE.zoomimage.libs[libKey],
							box: 'zoomimage_' + parseInt(Math.random() * 2000, 10) + ''
						});
						//increment the number of images in the library
						EYE.zoomimage.libs[libKey]++;
						jQEl
							.bind('click', EYE.zoomimage.click)
							.attr('zoomimage', libKey)
							.attr('zoomimageBox', el.zoomimageCfg.box);
						var currId = jQEl.attr('id');
						if (!currId) {
							currId = el.zoomimageCfg.box + '_trigger';
							jQEl.attr('id', currId);
						}
						var titleAttr = $(el).attr('title');
						if (titleAttr == '' || titleAttr == false) {
							el.zoomimageCfg.caption = false;
						}
						// generate the HTML for the image's box
						$(EYE.zoomimage.template.join(''))
							.attr('id', el.zoomimageCfg.box)
							.attr('zoomimage', currId)
							.addClass(el.zoomimageCfg.className)
							.appendTo(document.body)
							.bind('mousedown', EYE.zoomimage.mouseDown)
							.bind('click', EYE.zoomimage.imageClick)
							.each(function(){
								this.zoomimageControls = false;
								if (el.zoomimageCfg.controlsTrigger != 'focus') {
									$(this)
										.bind('mouseover', EYE.zoomimage.mouseOver)
										.bind('mouseout', EYE.zoomimage.mouseOut);
								}
							})
							.find('>div')
								.not(':first')
									.css('opacity', el.zoomimageCfg.opacity)
									.end()
								.filter('div:eq(2)')
									.html('<p>' + titleAttr + '</p>');
						if (el.zoomimageCfg.preload == 'load') {
							EYE.zoomimage.preload(el);
						}
						if (EYE.zoomimage.trackKey === false) {
							EYE.zoomimage.trackKey = true;
							$(document).bind('keydown', EYE.zoomimage.keyPressed);
						}
					}
				});
			}
		}
	});
	
	$.fn.extend({
	
		/**
		 * Open all images found in 'href' attribute from each element specified in the selection. The images are grouped in galleries. The images are preloaded before any user interation.
		 * @name zoomimage
		 * @description Open all images found in 'href' attribute from each element specified in the selection. The images are grouped in galleries
		 * @param 	Hash		options			A hash of parameters. All parameters are optional.
		 * @option	float 		opacity			The opacity for the caption and controls. Default: 0.3
		 * @option	int			border			Image's border. Default: 0
		 * @option	int			duration		Animation duration. Default 300
		 * @option	int			prevent			Pixes to move the mouse before the images is dragged (prevents accidental dragging). Default: 14
		 * @option	boolean		controls		Whatever if the controls are displayed (if the image is not part of an libriry then the controls are not displayed)
		 * @option	boolean		caption			Whatever if the caption is displayed (the caption text is the text from 'title' atribute. Default: true
		 * @option	boolean		centered		Whatever if the image should be centered in the viewport or to the trigger. Default: false
		 * @option	string		easing			Animation easing. Default: linear
		 * @option	boolean		hideSource		Whatever to hide source when the image is opened. Default: false
		 * @option	string		className		CSS class to add to image's box. Default: false
		 * @option	string		controlsTrigger	'focus' to show caption and controls when the box is focused or 'mouseover' to show controls and caption on mouse over. Default: 'focus'
		 * @option	string		preload			'click' to preload the image when the trigger is clicked or 'load' to preload the image on document load. Default: 'click'
		 * @option	function	onLoad			Callback function triggered when the image was loaded
		 * @option	function	beforeZoomIn	Callback function triggered before the image is zoomed in
		 * @option	function	onZoomIn		Callback function triggered when the image is zooms in
		 * @option	function	beforeZoomOut	Callback function triggered before the image is zoomed out
		 * @option	function	onZoomOut		Callback function triggered when the image is zooms out
		 * @option	function	onFocus			Callback function triggered when the image is focused
		 */
		zoomimage: EYE.zoomimage.init,
		
		/**
		 * Zooms out all opened images and removes orphans (when the trigger was not found)
		 * To clear specific images use for slector 'div.zooimage[whatever]', else all the images are processed
		 */
		zoomimageClear: EYE.zoomimage.clear
	});
})(jQuery);