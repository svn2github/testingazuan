/**
 *
 
 */


qx.Class.define("spagobi.commons.CoreUtils", {
  type : "static",
  statics : {
  	
  	
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
  	},
  	
  	toStr : function(o) {
  		var s = '';
    	for(p in o) {
    		s += p + ': ' + o[p] + ";\n";
    	}
    	return s;
  	}
  }
});