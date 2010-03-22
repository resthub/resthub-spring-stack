// jQuery Object Oriented Class
//
// Version 1.0.2
//
// Create By Hassan Jodat Shandi
// http://doob.ir/
// 27 Feb 2010
//

jQuery.extend({
    // define class core object
    Class: function() {
    },
    // define interface core object
    Interface: function() {
    }
});

// define interface elements
jQuery.extend(jQuery.Interface.prototype, {
    attributes: [],
    properties: [],
    methods: []
});

// define class elements
jQuery.extend(jQuery.Class, {
    // this method create getter and setter property
    createGetSet: function(object, name, element) {
        object.prototype['get' + name] = function() {
            return this[element];
        };
        object.prototype['set' + name] = function() {
            this[element] = arguments[0];
        };
        if(!object.prototype[element])
        {
            object.prototype[element] = '';
        }
    },
    
    // this method create getter and setter property in runtime
    createGetSetRuntime: function(object, name, element) {
        object.constructor.prototype['get' + name] = function() {
            return this[element];
        };
        object.constructor.prototype['set' + name] = function() {
            this[element] = arguments[0];
        };
        if(!object.constructor.prototype[element])
        {
            object.constructor.prototype[element] = '';
        }
    },

    // this method create module in window object
    createNamespace: function(name) {
        var names = name.split('.'),
            namespaceStr = 'window';
        while (names.length) {
            var name = names.shift();
            namespaceStr += "['" + name + "']";
        }
        return namespaceStr;
    },

    // use this method for understanding interface elements is implemented in class
    isIn: function(object, name) {
        for (var item in object.prototype) {
            if (item == name) {
                if (jQuery.isArray(object.prototype[item]))
                    return 0;
                else if (jQuery.isFunction(object.prototype[item]))
                    return 1;
                else if (jQuery.isEmptyObject(object.prototype[item]) || jQuery.isPlainObject(object.prototype[item]))
                    return 2;
                else if (jQuery.isXMLDoc(object.prototype[item]))
                    return 3;
                else
                    return 0;
            }
        }
        return -1;
    },
    
    // this method for create class object
    create: function() {
        var parent = null,
            elements = null,
            options = {
                abstract: false,
                getset: [],
                implements: [],
                module: '',
                superClass: [] // for holding superclass initialize function
            };
            
        // check for extending
        if (jQuery.isFunction(arguments[0])) {
            parent = arguments[0];
            elements = arguments[1];
            if (arguments[2]) {
                jQuery.extend(options, arguments[2] || {});
            }
        }
        else {
            elements = arguments[0];
            if (arguments[1]) {
                jQuery.extend(options, arguments[1] || {});
            }
        }
        
        // create new class core
        function handle() {
            // check if class is abstracted
            if (this.options.abstract) {
                throw new Error("abstract classes cannot be instantiated");
            }
            
            // execute constructor method
            try {
                this.initialize.apply(this, arguments);
            } catch (ex) { }
        }
        
        // extend class base methods in new class core
        jQuery.extend(handle.prototype, jQuery.Class);
        
        // extend parent class methods in new class core
        if (parent) {
            // get parent initialize function
            var superClass = parent.prototype.options.superClass || [];
            var superRender = parent.prototype.options.superRender || [];
            
            // extend parent class methods in new class core
            jQuery.extend(handle.prototype, parent.prototype);
            
            // save parent initialize function
            superClass.push(parent.prototype.initialize);
            options.superClass = superClass;
            
            try {
                superRender.push(parent.prototype.render);
                options.superRender = superRender;
            } catch (ex) { }
        }
        
        // extend user defined methods in new class core
        jQuery.extend(handle.prototype, elements || {});
        handle.prototype.options = options;

        // define getter and setter functions
        if (options.getset.length > 0) {
            for (var i = 0; i < options.getset.length; i++) {
                var name = options.getset[i][0],
                    element = options.getset[i][1];
                this.createGetSet(handle, name, element);
            }
        }
        
        // check for impelemented elements from interface
        if (options.implements.length > 0) {
            var attributesMustImplemented = [],
                propertiesMustImplemented = [],
                methodsMustImplemented = [];
            
            // extract elements from interface
            for (var i = 0; i < options.implements.length; i++) {
                jQuery.merge(attributesMustImplemented, options.implements[i].attributes);
                jQuery.merge(propertiesMustImplemented, options.implements[i].properties);
                jQuery.merge(methodsMustImplemented, options.implements[i].methods);
            }
            
            var didNotImplemented = false,
                msg = 'must be implemented';
            
            // check for attributes    
            for (var i = 0; i < attributesMustImplemented.length; i++) {
                var result = this.isIn(handle, attributesMustImplemented[i]);
                if (result != 0 && result != 2) {
                    didNotImplemented = true;
                    msg = 'attribute: ' + attributesMustImplemented[i] + ', ' + msg;
                }
            }
            
            // check for properties
            for (var i = 0; i < propertiesMustImplemented.length; i++) {
                var resultGet = this.isIn(handle, 'get' + propertiesMustImplemented[i]),
                    resultSet = this.isIn(handle, 'set' + propertiesMustImplemented[i]);
                if (resultGet != 1) {
                    didNotImplemented = true;
                    msg = 'property: get' + propertiesMustImplemented[i] + ', ' + msg;
                }
                else if (resultSet != 1) {
                    didNotImplemented = true;
                    msg = 'property: set' + propertiesMustImplemented[i] + ', ' + msg;
                }
            }
            
            // check for methods
            for (var i = 0; i < methodsMustImplemented.length; i++) {
                var result = this.isIn(handle, methodsMustImplemented[i]);
                if (result != 1) {
                    didNotImplemented = true;
                    msg = 'method: ' + methodsMustImplemented[i] + ', ' + msg;
                }
            }
            
            if (didNotImplemented) {
                throw new Error(msg);
            }
        }
        
        // check if class is module type, create module
        if (options.module != '') {
            var names = options.module.split('.'),
                name = names[0];
            window[name] = new function() { };
            for (var i = 1; i < names.length; i++) {
                name += '.' + names[i];
                eval(this.createNamespace(name) + ' = new function() {};');
            }
            eval('jQuery.extend(' + this.createNamespace(name) + ', handle.prototype);');
        }
        
        return handle;
    },
    
    // for add method to class in runtime
    addMethods: function() {
        if (arguments[0]) {
            jQuery.extend(this.constructor.prototype, arguments[0]);
        }
    },
    
    // for add attribute to class in runtime
    addAttributes: function() {
        if (arguments[0]) {
            jQuery.extend(this.constructor.prototype, arguments[0]);
        }
    },
    
    // for add property to class in runtime
    addProperty: function() {
		try {
			var name = arguments[0],
				element = arguments[1];
			this.createGetSetRuntime(this, name, element);
		} catch (ex) { }
    },
    
    // this method is use to get value And set value of property
    property: function() {
        // get value section
        if (arguments.length == 1 && this.constructor.prototype.hasOwnProperty('get' + arguments[0])) {
            if (arguments[0]) {
                if (jQuery.isFunction(this['get' + arguments[0]])) {
                    return this['get' + arguments[0]]();
                }
                else {
                    return this['get' + arguments[0]];
                }
            }
        }
        
        //set value section
        else if (this.constructor.prototype.hasOwnProperty('set' + arguments[0])) {
            if (jQuery.isFunction(this['set' + arguments[0]])) {
                for (var name in this) {
                    if (this[name] == this['get' + arguments[0]]()) {
                        if (!jQuery.isFunction(this[name])) {
                            this[name] = arguments[1];
                        }
                    }
                }
                return this['get' + arguments[0]]();
            }
            else {
                this[arguments[0]] = arguments[1];
                return this['get' + arguments[0]];
            }
        }
    },
    
    // check if two class is equal
    equal: function() {
        if (arguments[1]) {
            return arguments[0].constructor.prototype == arguments[1].constructor.prototype;
        }
        else {
            return this.constructor.prototype == arguments[0].constructor.prototype;
        }
    },
    
    // create fresh clone object from class object
    clone: function() {
        function handle() {
            try {
                this.initialize.apply(this, arguments);
            } catch (ex) { }
        }
        if (arguments[0] == true) {
            jQuery.extend(handle.prototype, this.constructor.prototype);
            return handle;
        }
        else {
            jQuery.extend(handle.constructor.prototype, this.constructor.prototype);
            return handle;
        }
    },
    
    toString: function() {
        return 'Design By Hassan Jodat Shandi';
    },
    
    // this method use for call parent initialize method
    parent: function() {
        var method = arguments[0],
            args = arguments[1],
            superClass = this.options.superClass,
            index = -1;
        for (var i = 0; i < superClass.length; i++) {
            if (superClass[i] == method)
                index = i;
        }
        if (index == -1 && superClass[superClass.length - 1] != method) {
            superClass[superClass.length - 1].apply(this, args);
        }
        else {
            superClass[index - 1].apply(this, args);
        }
    }
});
