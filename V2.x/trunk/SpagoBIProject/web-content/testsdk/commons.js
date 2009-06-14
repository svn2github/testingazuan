
Sbi = this.Sbi || {};
Sbi.sdk = {version: '2.2'};


/**
 * @class Sbi.sdk
 * Sbi.sdk core utilities and functions.
 * @singleton
 */

Sbi.sdk.apply = function(o, c, defaults){
    if(defaults){
        // no "this" reference for friendly out of scope calls
        Sbi.sdk.apply(o, defaults);
    }
    if(o && c && typeof c == 'object'){
        for(var p in c){
            o[p] = c[p];
        }
    }
    return o;
};

/**
 * Creates namespaces to be used for scoping variables and classes so that they are not global.  Usage:
 * <pre><code>
 *  Ext.namespace('Company', 'Company.data');
 *  Company.Widget = function() { ... }
 *  Company.data.CustomStore = function(config) { ... }
 * </code></pre>
 *
 * @param {String} namespace1
 * @param {String} namespace2
 * @param {String} etc
 * @method namespace
 */
Sbi.sdk.namespace =  function() {
    var a=arguments, o=null, i, j, d, rt;
    for (i=0; i<a.length; ++i) {
        d=a[i].split(".");
        rt = d[0];
        eval('if (typeof ' + rt + ' == "undefined"){' + rt + ' = {};} o = ' + rt + ';');
        for (j=1; j<d.length; ++j) {
            o[d[j]]=o[d[j]] || {};
            o=o[d[j]];
        }
    }
};

/**
 * Takes an object and converts it to an encoded URL. e.g. Ext.urlEncode({foo: 1, bar: 2}); would return "foo=1&bar=2".  
 * Optionally, property values can be arrays, instead of keys and the resulting string that's returned will contain a name/value pair for each array value.
 *
 * @param {Object} o
 * @return {String}
*/
Sbi.sdk.urlEncode = function(o){
    if(!o){
        return "";
    }
    var buf = [];
    for(var key in o){
        var ov = o[key], k = encodeURIComponent(key);
        var type = typeof ov;
        if(type == 'undefined'){
            buf.push(k, "=&");
        }else if(type != "function" && type != "object"){
            buf.push(k, "=", encodeURIComponent(ov), "&");
        }else if(ov instanceof Array){
            if (ov.length) {
                for(var i = 0, len = ov.length; i < len; i++) {
                    buf.push(k, "=", encodeURIComponent(ov[i] === undefined ? '' : ov[i]), "&");
                }
            } else {
                buf.push(k, "=&");
            }
        }
    }
    buf.pop();
    return buf.join("");
},

/**
  * Takes an encoded URL and and converts it to an object. e.g. Ext.urlDecode("foo=1&bar=2"); would return {foo: 1, bar: 2} or Ext.urlDecode("foo=1&bar=2&bar=3&bar=4", true); would return {foo: 1, bar: [2, 3, 4]}.
  * @param {String} string
  * @param {Boolean} overwrite (optional) Items of the same name will overwrite previous values instead of creating an an array (Defaults to false).
  * @return {Object} A literal with members
  */
Sbi.sdk.urlDecode = function(string, overwrite){
    if(!string || !string.length){
        return {};
    }
    var obj = {};
    var pairs = string.split('&');
    var pair, name, value;
    for(var i = 0, len = pairs.length; i < len; i++){
        pair = pairs[i].split('=');
        name = decodeURIComponent(pair[0]);
        value = decodeURIComponent(pair[1]);
        if(overwrite !== true){
            if(typeof obj[name] == "undefined"){
                obj[name] = value;
            }else if(typeof obj[name] == "string"){
                obj[name] = [obj[name]];
                obj[name].push(value);
            }else{
                obj[name].push(value);
            }
        }else{
            obj[name] = value;
        }
    }
    return obj;
}
