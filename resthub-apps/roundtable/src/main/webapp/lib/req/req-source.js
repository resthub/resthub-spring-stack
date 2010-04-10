/* encoding: utf-8

  ****  Req-js - javascript lazy-loading and dependency managment made easy  ****

  Version: 1.0

  Copyright (c) 2009
    Már Örlygsson  (http://mar.anomy.net/) and
    Hugsmiðjan ehf. (http://www.hugsmidjan.is)

  Dual licensed under a MIT licence (http://en.wikipedia.org/wiki/MIT_License)
  and GPL 2.0 or above (http://www.gnu.org/licenses/old-licenses/gpl-2.0.html).

-----------------------------------------------------------------------------

  Demo:
    * <./demo/req-demo.html>

  Documentation:
    * <.docs/req-docs.html>
    * <http://mar.anomynet/entries/2009/10/20/23.39.22/>

  Get updates from:
    * <http://github.com/maranomynet/req/>
    * <git://github.com/maranomynet/req.git>

-----------------------------------------------------------------------------

  TODO (developer's notes):
    * Support also loading CSS files. (maybe ??)
    * Look into doing parallel downloading (while ensuring execution order)
      with DOM Node injection in normal browsers but using 'defer' in MSIE
      As per suggestions in this article:
          <http://www.stevesouders.com/blog/2009/04/27/loading-scripts-without-blocking/>
    * Look into making assets groupable - so that each group has its own baseUrl and joining rules/parameters.


*/

/*
  First: Attempt to apply document.readyState support in FF3.5 and older. 
  See more:
   * http://webreflection.blogspot.com/2009/11/195-chars-to-help-lazy-loading.html
   * http://groups.google.com/group/jquery-dev/browse_thread/thread/5aba1a1c2a7e53a7/e76736baf202f6e1
*/
(function(h,a,c,k){if(h[a]==null&&h[c]){h[a]="loading";h[c](k,c=function(){h[a]="complete";h.removeEventListener(k,c,!1)},!1)}})(document,"readyState","addEventListener","DOMContentLoaded");

(function(_null, undefined){



// -------------------------------------------------------------------------------
// Private Methods/Properties:
// -------------------------------------------------------------------------------

  var _queue = [], // THE PROCESSING QUEUE!! Mother of all...
      _onreadystatechange = 'onreadystatechange', // string cache to cut down minified file-size
      _onload = 'onload',                         // string cache to cut down minified file-size

      // prep (normalize) the asset array (arguments) passed to the Req() function.
      // runs recursively to resolve `.req` dependency information, and fold those assets back into the returned queue-stub
      _prepQueue = function (queueStub)
      {
        var assetId,
            _allAssets = R.assets,
            _fixUrl = R.fixUrl,
            _fixedQueueStub = [];

        // loop through the queueStub
        for (var i=0,l=queueStub.length; i<l; i++)
        {
          var asset = queueStub[i];

          if (typeof asset == 'function')
          {
            // push functions onto the _fixedQueueStub and do nothing further. - Next please!
            _fixedQueueStub.push(asset);
          }
          else if (asset && asset !== true)
          {
            // if the asset is an humble String (i.e. a friendly id/handle or a URL)
            if (asset.charAt)
            {
              // ...let's call it assetId
              assetId = asset;
              // ...and then make asset into a real Asset Object.
              // first checking if it already exists in R.assets (_allAssets)
              // either under it's current name, or (in case assetId is a URL) under the name of a normalized URL (_fixUrl)
              // and if not, then create a simple {src:assetId} asset object, and immediately insert it into the database for future reference.
              asset = _allAssets[assetId] || ( _allAssets[assetId] = (_allAssets[_fixUrl(assetId)] || { src : assetId }) );
            }
            else
            {
              // assume `asset` is an Asset Object (since it's not a Function or a String)
              // derive assetId from the asset.
              assetId = asset.id || asset.src;
              // and insert it into the R.assets database (_allAssets) if it's not already there.
              _allAssets[assetId] = _allAssets[assetId] || asset;
            }

            // if this is the first we see of this asset, during this recursive run of _prepQueue.
            // (And if it's not already _loaded)
            if (!asset._encountered && !asset._loaded)
            {
              // then start normalizing it.
              // First off, raise the _encountered flag to avoiding infinite requirement loops.
              asset._encountered = 1;
              // check if it's already _processed (normalized) by _prepQueue in an earlier run.
              if (!asset._processed)
              {
                // mark it as _processed
                asset._processed = 1;
                // NOTE: Some assets may have no URL themselves - only a list of requirements.
                // If there is a .src (URL), however, then...
                if (asset.src)
                {
                  // normalize the URL
                  asset.src = _fixUrl(asset.src);
                  // and store an extra reference to the asset under the name of .src
                  //  - to ensure that lookup by script URL works as well.
                  _allAssets[asset.src] = asset;
                }
                // Enforce that the dependency list must be Array
                var req = asset.req;
                if (req && req.charAt)
                {
                  asset.req = [req];
                }
              }
              // if there are any dependencies listed (req), then (recursively) run that Array through _prepQueue
              // and append the normalized/_processed results to the _fixedQueueStub.
              req && _fixedQueueStub.push.apply( _fixedQueueStub, _prepQueue(asset.req) );
              // finally push the asset onto the _fixedQueueStub
              _fixedQueueStub.push(asset);
            }
          }
        }
        // return a neatly normalized Array of nice asset objects.
        return _fixedQueueStub;
      },


      // holds a list of assets that should be joined in one mega-URL for faster download (saving HTTP overhead).
      _joinBuffer = [],

      // flushes the _joinBuffer.
      // Guaranteed to return an asset object.
      _bufferFlush = function () {

        var asset,
            i = _joinBuffer.length;

        if (i>=R.joinLim)
        {
          // there are more than the minimum number of assets in the buffer, so we proceed to join them into one resource!
          var _concatUrls = [];
          while (i--)
          {
            if (_joinBuffer[i].src)
            {
              _concatUrls.unshift( R.getJoinStub(_joinBuffer[i]) );
            }
          }
          asset = {
              src:    _joinUrl.replace(s, _concatUrls.join(R.joint||'')),
              _loads: _joinBuffer
            };
          // empty the _joinBuffer
          _joinBuffer = [];
        }
        else
        {
          // return only the first item off the _joinBuffer.
          // if the _joinBuffer contains multiple items (R.joinLim is larger than 1) we rely on
          // _processNext to run _bufferFlush again and thus slowly empty the _joinBuffer one asset at a time.
          asset = _joinBuffer.shift();
          // politely tell _processNext to not place this asset back in the _joinBuffer, but load it directly!
          asset._alreadyBeenInJoinBuffer = 1;
        }
        return asset;
      },


      _processNext = function () {
        // don't to anything if both the _queue and _joinBuffer are empty.
        // Note: The _isRunning flag is set inline. (to save precious bytes :-)
        if (_isRunning = !!(_queue.length || _joinBuffer.length))
        {
          // if the _queue is empty, then use the (combined) contents of the _joinBuffer
          var asset = _queue.shift() || _bufferFlush();
          // check if it's a function that needs to be run.

          if (typeof asset == 'function')
          {
            // before running any functions, make sure to flush the _joinBuffer
            if (_joinBuffer.length)
            {
              // stash the function asset back into the _queue
              _queue.unshift(asset);
              // and replace the `asset` variable with the _bufferFlush output (and continue as if nothing happend!)
              asset = _bufferFlush();
            }
            else
            {
              // run the function
              asset();
              asset = _null; // set asset to null to avoid entering the condition block below (and save us a second `typeof` check)
            }
          }
          // make sure this asset isn't `null` and isn't already loaded
          if (asset  &&  !asset._loaded)
          {
            // check to see if the asset is already loaded (usually by other means, such as inline <script /> tag, etc.)
            if (asset.check && asset.check())
            {
              // We always need to flush the _joinBuffer before we run `.onload()`
              // otherwise bad things may happen.
              if (_joinBuffer.length && asset[_onload])
              {
                // reinsert asset into the _queue, and place the _bufferFlush output infront of that
                // so that it gets loaded first.  After that the current asset get's reprocessed and finally as its `.onload` run
                _queue.unshift(_bufferFlush(), asset);
              }
              else
              {
                asset._loaded = 1;
                if (asset[_onload])
                {
                  asset[_onload]();
                  asset[_onload] = _null; // why? can't remember... sorry :-(  But probably to release memory, and make double-sure that .onload doesn't run twice.
                }
              }
            }
            else
            {
              // make sure the asset is joinable, and hasn't _alreadyBeenInJoinBuffer and then...
              // stash it away in the _joinBuffer for later processing - en masse.
              // NOTE: (we do this even if the asset doesn't have a .src - because it might be a group-asset
              //       defining a bunch of `.req`s,  and even have an `.onload` event that needs to be run
              //       at the appropriate hour.
              if ((asset.join===true || !asset.src)  &&  !asset._alreadyBeenInJoinBuffer)
              {
                _joinBuffer.push(asset);
              }
              else
              {
                // ok, here we have an asset that needs to be loaded,
                // but we may not do that directly if there's stuff in the _joinBuffer
                if (_joinBuffer.length && !asset._alreadyBeenInJoinBuffer)
                {
                  // stash the asset back into the _queue
                  _queue.unshift(asset);
                  // and replace the `asset` variable with the _bufferFlush output (and continue as if nothing happend!)
                  asset = _bufferFlush();
                }
                // ...now with whatever asset the previous conditional-block left us with (might be a _bufferFlush)
                // ...check if it has .src and load it
                if (asset.src)
                {
                  var scriptElm = document.createElement('script'),
                      _charset = asset.charset || R.charset;
                  _charset  &&  (scriptElm.charset = _charset);
                  ;;;scriptElm.className = 'Req'; // add a className marker - to make debugging easier. (;;;-prefixed lines get removed by dean.edwards' "Packer" minifier)
                  scriptElm.src = asset.src;
                  scriptElm[_onload] = scriptElm[_onreadystatechange] = function()
                  {
                    if (!scriptElm.readyState || /^(loaded|complete)$/.test(scriptElm.readyState))
                    {
                      scriptElm[_onreadystatechange] = scriptElm[_onload] = _null; // this keeps browsers like Opera (<10) from running both the .onload and the .onreadystatechange handler.
                      // define a list of what was just loaded (might be a bunch of assets, via a joined script url from the _joinBuffer)
                      var _loads = asset._loads || [asset];
                      for (var i=0, _loadAsset; (_loadAsset = _loads[i]); i++)
                      {
                        // loop through the list and mark each item as _loaded and run `.onload` callbacks as needed
                        _loadAsset._loaded = 1;
                        _loadAsset[_onload] && _loadAsset[_onload]();
                        _loadAsset[_onload] = _null; // why? See comment a couple of screenfuls up...^^^^
                      }
                      // recurse!!
                      _processNext();
                    }
                  };
                  // NOTE: if we were to use a simple `.insertBefore(scriptElm, _headElm.firstChild)` like done by jQuery
                  // we'd get all the injected scriptElms in reverse order which would make debugging confusing.
                  _firstBaseElm ?
                      _headElm.insertBefore(scriptElm, _firstBaseElm): // silly hack, needed to avoid crash in MSIE 6.0 (see: http://dev.jquery.com/ticket/2709 )
                      _headElm.appendChild(scriptElm);
                  // return without recursing ... because now we're playing The Waiting Game with the <script> we've just inserted
                  return;
                }
                // else - asset has no src URL - must be a simple requirement-package....
                asset._loaded = 1;
              }
            }
          }
          // recurse!!
          _processNext();
        }
      },


      _isRunning,  // flag to indicate that _processNext is indeed running - only waiting for a <script> to load.
      _headElm,    // cached reference to the <head> element
      _firstBaseElm, // reference to the first <base> element in the document - needed to avoid crash in MSIE 6.0 (see: http://dev.jquery.com/ticket/2709 ) 
      _baseUrl,    // cached *normalized* value of Req.baseUrl
      _joinUrl,    // cached *normalized* value of Req.joinUrl
      s,           // cached value of Req.urlToken



// -------------------------------------------------------------------------------
// Defining The Req Namespace:
// -------------------------------------------------------------------------------

  R = Req = function (appendToQueue) {  // (also create a reference to Req from a minifiable local variable `R`)
    s = s || R.urlToken || '%{s}'; // default Req.urlToken to a value of '%{s}'
    // normalize the baseUrl
    _baseUrl = R.baseUrl || s;
    _baseUrl += _baseUrl.indexOf(s)>-1 ? '' : s; // enforce+append the mandatory %{s}

    // normalize the joinUrl
    _joinUrl = R.joinUrl || s;
    _joinUrl += _joinUrl.indexOf(s)>-1 ? '' : s; // enforce+append the mandatory %{s}

    // find + store/cache the <head> element
    _headElm = _headElm || document.getElementsByTagName('head')[0];
    _firstBaseElm = _firstBaseElm || _headElm.getElementsByTagName('base')[0];
    // prep (normalize) the assets in the arguments array.
    var _queueStub = _prepQueue( [].slice.call(arguments, 0) ),
        i = _queueStub.length;
    // delete temporary "_encountered" markers inserted by _prepQueue (to avoid infinite `.req`uirement loops)
    // subsequent runs of Req() might want to jump the queue with some of the same assets,
    // and in that case we don't want _prepQueue to skip them.
    while(i--) { delete _queueStub[i]._encountered; }

    // Default to stacking new _queueStub at the beginning of the _queue, for immediate processing!
    // (this is A Good Thing because it faciliates nested Req() calls.)
    // Allow `true` as a first argument, to push the _queueStub onto the *end* of the _queue, for delay processing.
    _queue[appendToQueue===true?'push':'unshift'].apply(_queue, _queueStub);

    // Delay first call to `_processNext();` slightly,
    // to allow immediate subsequent `Req` calls to push their assets to the front of the `_queue`.
    setTimeout(function(){
        // if, at that time, we're not waiting for a <script> to load, then start to _processNext item in the _queue
        if (!_isRunning) { _processNext(); }
      }, 0);
  };


// -------------------------------------------------------------------------------
// Public (Overloadable) Methods/Properties:
// -------------------------------------------------------------------------------

  //R.charset  = null;    // Example: 'utf-8';  <--  the default charset="" attribute for the <script /> elements.
  //R.urlToken = '%{s}';  // replacement pattern for inserting relative asset.src urls into _baseUrl and _joinUrl
  //R.baseUrl  = '';      // Example: 'http://www.server.com/scripts/%{s}.js';  <--  the first occurrence of Req.urlToken gets replaced by an `asset`'s `.src` value.
  //R.joinUrl  = '';      // Example: 'http://www.server.com/join/%{s}'              (...if the urlToken is missing, it gets appended to the Url)
  //R.joint    = '';      // Example: '|'     string/token to seperate the script URL stubs as they are appended to the joinUrl.
  R.joinLim = 1;        // minimum number of items in the _joinBuffer for joining to occur


  // Req.fixUrl() is used by _prepQueue() to normalize assset.src values and add a default _baseUrl to relative paths.
  R.fixUrl = function (url)
  {
    return /^(\.?\/|https?:)/.test(url) ? url : _baseUrl.replace(s, url);
  };

  // Req.getJoinStub() is used by _bufferFlush() to get the joinUrl "stub" for the given asset.
  // Defaults to returning whatever comes after _baseUrl in a normalized asset.src
  R.getJoinStub = function (asset)
  {
    return asset.src.replace(_baseUrl.split(s)[0], '');
  };


  // Req's asset database
  R.assets  = {
  /*
      'My Asset ID or URL' : {  // the URL - or friendly id/name for this asset.  (If the `src` property is empty this label is assumed to be the URL.)
        // Asset properties and their default values:
        req:     ['Asset name', 'Asset name 2'],                    // List of assets this asset depends on, each of which may depend on other assets, etc. etc.
        check:   function () { return !!window.myScriptObject; },   // Function to determine wheather this resource has alreay been loaded (via other means, such as, direct <script> tags, etc.)
        src:     'js/myscript.js',                                  // The actual URL to the javascript file. (Relative URLs get normalized with Req.fixUrl and Req.baseUrl - while URLs starting with "http(s)://", "/", and "./" are left untouched)
        charset: 'utf-8',                                           // Character encoding of the script file -- (common for mixed charset environments on old MSIE browsers which ignore server's HTTP headers).
        onload:  function () { doStuff(); },                        // Callback (onload event handler) to run when the asset has loaded for the first time. (Useful for running inits.)
        join:    false,                                             // Can this asset be joined with others into a single HTTP request (see Req.joinUrl and Req.getJoinStub, etc.)
        id:      'My Asset ID'                                      // Optional friendly id/name for the assset. (Only used when passing asset objects as paramters to the Req() function)
      }
  */
  };



})(null);