/*
 * jquery-ejs, a jQuery plugin for EmbeddedJS
 *
 * Copyright (C) 2010 Nicolas Trangez  <eikke eikke com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301  USA
 */
define(['jquery', '/lib/ejs/ejs.js'], function (jQuery) {
	
    // Make sure EJS is loaded
    if(!EJS) {
        throw('EJS is required');
    }

    // Make sure we won't overwrite anything
    if($.render || $.fn.render) {
        throw('jQuery.render or jquery.fn.render already loaded');
    }

    /**
     * Render an EJS template to a jQuery object
     *
     * The given URL will be appended to the default templatePrefix and passed
     * to the EJS constructor. The data object will be used to fill in template
     * values. An options map can be passed to override defaults.
     */
    $.render = function(url, data, options) {
        var opts, templateUrl, ejsOptions, rendered;

        // Calculate final option map
        opts = $.extend({}, $.render.defaults, options);

        // Calculate template location
        templateUrl = (opts.templatePrefix || '') + url;

        // Set up EJS constructor arguments
        ejsOptions = {
            url: templateUrl,
            ext: opts.ext,
            cache: opts.cache,
			text: opts.text
        };

        // Render template
        rendered = new EJS(ejsOptions).render(data);

        // Return rendered template as a jQuery object
        return $(rendered);
    };

    /**
     * Plugin default settings
     *
     * templatePrefix is a prefix which will be prepended to all template URLs.
     * ext is the extension to be appended to templates, if the given URL
     * doesn't end with it already.
     * cache enables or disables template caching.
     *
     * The ext and cache options are passed to the EJS constructor as-is.
     */
    $.render.defaults = {
        templatePrefix: undefined,
        ext: '.html',
        cache: true
    };

    /**
     * Render an EJS template and replace the content of all selected DOM nodes
     * with a copy of the rendered template.
     *
     * The arguments will be passed to jQuery.ejs as-is.
     */
    $.fn.render = function(url, data, options) {
        var rendered;

        // Render the template
        rendered = $.render(url, data, options);

        // Replace content of all matched DOM nodes
        return this.each(function() {
            $(this).empty().append(rendered.clone());
        });
    };

});
