/* *

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

* */

/* *
 * Created by SpagoBI team
 * Andrea Gioia (andrea.gioia@eng.it), 
 * Amit Rana (amit.rana@eng.it), 
 * Gaurav Jauhri (gaurav.jauhri@eng.it)
 
 * */

/**
 * Class that provides some common utilities
 */
 
qx.Class.define("qooxdoo.commons.CoreUtils", {
  type : "static",
  statics : {
 
 /**
  * Function that copies source object "c" into destination object "o". 
  * <p>Parameters can be a simple data type like number, boolean, string 
  * or a complex object with properties. 
  * <p>If the property already exists in destination object, its value is 
  * overridden by the value in souce object. If the propert doesn't exist,
  * it is added from source to destination object.  
  * 
  * <p>*Example*
  * <p>
  * 	<code>
  * var a = qooxdoo.commons.CoreUtils.apply({top: 10, left: 20}, 
  * 										{top: 50, width:80})
  * 	</code>
  * <p> sets object "a" with properties {top: 50, left: 20, width:80}
  * 
  * @param o {qx.core.Object} Destination object
  * @param c {qx.core.Object} Source object 
  * @param defaults {qx.core.Object ? null} Default object to copy from 
  * 									  if no source is specified
  * @return {qx.core.Object} Object "o"
  */ 	
  	
    apply : function(o, c, defaults){
	    if(defaults){
	        // no "this" reference for friendly out of scope calls
	    	qooxdoo.commons.CoreUtils.apply(o, defaults);
	    }
	    if(o && c && typeof c == 'object'){
	        for(var p in c){
	            o[p] = c[p];
	        }
	    }
	    return o;
  	},
  
  /**
  * Function to convert an object into a string. 
  * <p>Returns a string with the object properties and their respective values 
  * 
  * <p>*Example 1* 
  * <p><code>string str = toStr({name:"Myname", val:"123"}) </code> 
  * <p>sets the value of string __str__ to 		 "__name: Myname;__
  * <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  *    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  *    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
  *    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; __val: 123__"
  * 	
  * @param o {qx.core.Object} The object to be converted into a string
  */ 
  	
  	toStr : function(o) {
  		var s = '';
  		
  		for(p in o) {
  	    	s += p + ': ' + o[p] + ";\n";
  	    }
  		
    	return s;
  	}
  	
  	, dump : function(o) {
  		alert(this.toStr(o));
  	}
  	
  	, arrayToStr : function(a) {
  		var s = '';
  		
  		for(i = 0; i < a.length; i++) {
			s += '{\n';
			s += qooxdoo.commons.CoreUtils.toStr(a[i]);
			s += '}, ';
		}
  		return s;
  	}
  	
  	, msg : function(s){
  		alert(s);
  	}	
  }
});