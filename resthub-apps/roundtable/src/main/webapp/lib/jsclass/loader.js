this.JS = this.JS || {};

JS.Package = function(loader) {
  var Set = JS.Package.OrderedSet;
  JS.Package._index(this);
  
  this._loader    = loader;
  this._names     = new Set();
  this._deps      = new Set();
  this._uses      = new Set();
  this._observers = {};
  this._events    = {};
};

(function(klass) {
  //================================================================
  // Ordered list of unique elements, for storing dependencies
  
  var Set = klass.OrderedSet = function(list, transform) {
    this._members = this.list = [];
    this._index = {};
    if (!list) return;
    
    for (var i = 0, n = list.length; i < n; i++)
      this.push(transform ? transform(list[i]) : list[i]);
  };

  Set.prototype.push = function(item) {
    var key   = (item.id !== undefined) ? item.id : item,
        index = this._index;
    
    if (index.hasOwnProperty(key)) return;
    index[key] = this._members.length;
    this._members.push(item);
  };
  
  //================================================================
  // Environment settings
  
  klass._env = this;
  
  if ((this.document || {}).getElementsByTagName) {
    var script = document.getElementsByTagName('script')[0];
    klass._isIE = (script.readyState !== undefined);
  }
  
  
  //================================================================
  // Configuration methods, called by the DSL
  
  var instance = klass.prototype;
  
  instance.addDependency = function(pkg) {
    this._deps.push(pkg);
  };
  
  instance.addSoftDependency = function(pkg) {
    this._uses.push(pkg);
  };
  
  instance.addName = function(name) {
    this._names.push(name);
    klass.getFromCache(name).pkg = this;
  };
  
  instance.onload = function(block) {
    this._onload = block;
  };
  
  //================================================================
  // Event dispatchers, for communication between packages
  
  instance.on = function(eventType, block, scope) {
    if (this._events[eventType]) return block.call(scope);
    var list = this._observers[eventType] = this._observers[eventType] || [];
    list.push([block, scope]);
  };
  
  instance.fire = function(eventType) {
    if (this._events[eventType]) return false;
    this._events[eventType] = true;
    
    var list = this._observers[eventType];
    if (!list) return true;
    delete this._observers[eventType];
    
    for (var i = 0, n = list.length; i < n; i++)
      list[i][0].call(list[i][1]);
    
    return true;
  };
  
  //================================================================
  // Loading frontend and other miscellany
  
  instance.isLoaded = function(withExceptions) {
    if (!withExceptions && this._isLoaded !== undefined) return this._isLoaded;
    
    var names = this._names.list,
        i     = names.length,
        name, object;
    
    while (i--) { name = names[i];
      object = klass.getObject(name);
      if (object !== undefined) continue;
      if (withExceptions)
        throw new Error('Expected package at ' + this._loader + ' to define ' + name);
      else
        return this._isLoaded = false;
    }
    return this._isLoaded = true;
  };
  
  instance.load = function() {
    if (!this.fire('request')) return;
    
    var allDeps    = this._deps.list.concat(this._uses.list),
        startEvent = 'load',  // could potentially use 'download' event in
        listener   = {};      // browsers that guarantee execution order
    
    listener[startEvent] = this._deps.list;
    
    klass.when(listener, function() {
      klass.when({complete: allDeps, load: [this]}, function() {
        this.fire('complete');
      }, this);
      
      var self = this, fireOnLoad = function() {
        if (self._onload) self._onload();
        self.isLoaded(true);
        self.fire('load');
      };
      
      if (this.isLoaded()) {
        this.fire('download');
        return this.fire('load');
      }
      
      if (this._loader === undefined)
        throw new Error('No load path found for ' + this._names.list[0]);
      
      typeof this._loader === 'function'
            ? this._loader(fireOnLoad)
            : klass.Loader.loadFile(this._loader, fireOnLoad);
      
      this.fire('download');
    }, this);
  };
  
  instance.toString = function() {
    return 'Package:' + this._names.list.join(',');
  };
  
  //================================================================
  // Class-level event API, handles group listeners
  
  klass.when = function(eventTable, block, scope) {
    var eventList = [], event, packages, i;
    for (event in eventTable) {
      if (!eventTable.hasOwnProperty(event)) continue;
      packages = new klass.OrderedSet(eventTable[event], function(name) { return klass.getByName(name) });
      i = packages.list.length;
      while (i--) eventList.push([event, packages.list[i]]);
    }
    
    var waiting = i = eventList.length;
    if (waiting === 0) return block && block.call(scope);
    
    while (i--) {
      eventList[i][1].on(eventList[i][0], function() {
        waiting -= 1;
        if (waiting === 0 && block) block.call(scope);
      });
      eventList[i][1].load();
    }
  };
  
  //================================================================
  // Indexes for fast lookup by path and name, and assigning IDs
  
  klass._autoIncrement = 1;
  klass._indexByPath = {};
  klass._indexByName = {};
  
  klass._index = function(pkg) {
    pkg.id = this._autoIncrement;
    this._autoIncrement += 1;
  };
  
  klass.getByPath = function(loader) {
    var path = loader.toString();
    return this._indexByPath[path] = this._indexByPath[path] || new this(loader);
  };
  
  klass.getByName = function(name) {
    if (typeof name !== 'string') return name;
    var cached = this.getFromCache(name);
    if (cached.pkg) return cached.pkg;
    
    var placeholder = new this();
    placeholder.addName(name);
    return placeholder;
  };
  
  //================================================================
  // Cache for named packages and runtime objects
  
  klass.getFromCache = function(name) {
    return this._indexByName[name] = this._indexByName[name] || {};
  };
  
  klass.getObject = function(name) {
    var cached = this.getFromCache(name);
    if (cached.obj !== undefined) return cached.obj;
    
    var object = this._env,
        parts  = name.split('.'), part;
    
    while (part = parts.shift()) object = object && object[part];
    
    return this.getFromCache(name).obj = object;
  };
  
})(JS.Package);


JS.Package.DomLoader = {
  usable: function() {
    return !!JS.Package.getObject('window.document.getElementsByTagName');
  },
  
  __FILE__: function() {
    var scripts = document.getElementsByTagName('script');
    return scripts[scripts.length - 1].src;
  },
  
  loadFile: function(path, fireCallbacks) {
    var self = this,
        tag = document.createElement('script');
    
    tag.type = 'text/javascript';
    tag.src = path;
    
    tag.onload = tag.onreadystatechange = function() {
      var state = tag.readyState, status = tag.status;
      if ( !state || state === 'loaded' || state === 'complete' || (state === 4 && status === 200) ) {
        fireCallbacks();
        tag.onload = tag.onreadystatechange = self._K;
        tag = null;
      }
    };
    
    if (window.console && console.info)
      console.info('Loading ' + path);
    
    document.getElementsByTagName('head')[0].appendChild(tag);
  },
  
  _K: function() {}
};

JS.Package.ServerLoader = {
  usable: function() {
    return typeof JS.Package.getObject('load') === 'function' &&
           typeof JS.Package.getObject('version') === 'function';
  },
  
  setup: function() {
    var self = this;
    load = (function(origLoad) {
      return function() {
        self._currentPath = arguments[0];
        return origLoad.apply(JS.Package._env, arguments);
      };
    })(load);
  },
  
  __FILE__: function() {
    return this._currentPath;
  },
  
  loadFile: function(path, fireCallbacks) {
    load(path);
    fireCallbacks();
  }
};

(function() {
  var candidates = [  JS.Package.DomLoader,
                      JS.Package.ServerLoader ],
      
      n = candidates.length,
      i, candidate;
  
  for (i = 0; i < n; i++) {
    candidate = candidates[i];
    if (candidate.usable()) {
      JS.Package.Loader = candidate;
      if (candidate.setup) candidate.setup();
      break;
    }
  }
})();


JS.Package.DSL = {
  __FILE__: function() {
    return JS.Package.Loader.__FILE__();
  },
  
  pkg: function(name, path) {
    var pkg = path
        ? JS.Package.getByPath(path)
        : JS.Package.getByName(name);
    pkg.addName(name);
    return new JS.Package.Description(pkg);
  },
  
  file: function(path) {
    var pkg = JS.Package.getByPath(path);
    return new JS.Package.Description(pkg);
  },
  
  load: function(path, fireCallbacks) {
    JS.Package.Loader.loadFile(path, fireCallbacks);
  }
};

JS.Package.Description = function(pkg) {
  this._pkg = pkg;
};

(function(klass) {
  
  klass._batch = function(method, args) {
    var n = args.length, method = this._pkg[method], i;
    for (i = 0; i < n; i++) method.call(this._pkg, args[i]);
    return this;
  };
  
  klass.provides = function() {
    return this._batch('addName', arguments);
  };
  
  klass.requires = function() {
    return this._batch('addDependency', arguments);
  };
  
  klass.uses = function() {
    return this._batch('addSoftDependency', arguments);
  };
  
  klass.setup = function(block) {
    this._pkg.onload(block);
    return this;
  };
  
})(JS.Package.Description.prototype);

JS.Package.DSL.loader = JS.Package.DSL.file;

JS.Packages = function(declaration) {
  declaration.call(JS.Package.DSL);
};
 
JS.require = function() {
  var requirements = [], i = 0;
  
  while (typeof arguments[i] === 'string'){
    requirements.push(arguments[i]);
    i += 1;
  }
  
  JS.Package.when({complete: requirements}, arguments[i], arguments[i+1]);
};

require = JS.require;


JS.Packages(function() { with(this) {
    
    var PATH = JS.Package._env.JSCLASS_PATH ||
               __FILE__().replace(/[^\/]*$/g, '');
    
    if (!/\/$/.test(PATH)) PATH = PATH + '/';
    
    var module = function(name) { return file(PATH + name + '.js') };
    
    module('core')          .provides('JS.Module',
                                      'JS.Class',
                                      'JS.Kernel',
                                      'JS.Singleton',
                                      'JS.Interface');
    
    module('comparable')    .provides('JS.Comparable')
                            .requires('JS.Module');
    
    module('constant_scope').provides('JS.ConstantScope')
                            .requires('JS.Module');
    
    module('forwardable')   .provides('JS.Forwardable')
                            .requires('JS.Module');
    
    module('enumerable')    .provides('JS.Enumerable')
                            .requires('JS.Module',
                                      'JS.Class');
    
    module('observable')    .provides('JS.Observable')
                            .requires('JS.Module');
    
    module('hash')          .provides('JS.Hash')
                            .requires('JS.Class',
                                      'JS.Enumerable',
                                      'JS.Comparable');
    
    module('set')           .provides('JS.Set',
                                      'JS.HashSet',
                                      'JS.SortedSet')
                            .requires('JS.Class',
                                      'JS.Enumerable')
                            .uses(    'JS.Hash');
    
    module('linked_list')   .provides('JS.LinkedList',
                                      'JS.LinkedList.Doubly',
                                      'JS.LinkedList.Doubly.Circular')
                            .requires('JS.Class',
                                      'JS.Enumerable');
    
    module('command')       .provides('JS.Command',
                                      'JS.Command.Stack')
                            .requires('JS.Class',
                                      'JS.Enumerable',
                                      'JS.Observable');
    
    module('decorator')     .provides('JS.Decorator')
                            .requires('JS.Module',
                                      'JS.Class');
    
    module('method_chain')  .provides('JS.MethodChain')
                            .requires('JS.Module',
                                      'JS.Kernel');
    
    module('proxy')         .provides('JS.Proxy',
                                      'JS.Proxy.Virtual')
                            .requires('JS.Module',
                                      'JS.Class');
    
    module('ruby')          .provides('JS.Ruby')
                            .requires('JS.Class');
    
    module('stack_trace')   .provides('JS.StackTrace')
                            .requires('JS.Module',
                                      'JS.Singleton');
    
    module('state')         .provides('JS.State')
                            .requires('JS.Module',
                                      'JS.Class');
}});