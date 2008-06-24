/**
 *
 
 */


qx.Class.define("spagobi.commons.CoreUtils", {
  type : "static",
  statics : {
  	
  	/**
     *  Copies all the properties of config to obj.
     *
     * @param {Object} obj The receiver of the properties
 	 * @param {Object} config The source of the properties
 	 * @param {Object} defaults A different object that will also be applied for default values
 	 * @return {Object} returns obj
     */
    apply : function(o, c, defaults){
	    if(defaults){
	        // no "this" reference for friendly out of scope calls
	        apply(o, defaults);
	    }
	    if(o && c && typeof c == 'object'){
	        for(var p in c){
	            o[p] = c[p];
	        }
	    }
	    return o;
  	}
  }
});