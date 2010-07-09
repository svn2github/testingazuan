/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
 
/**
  * Object name 
  * 
  * [description]
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * -  Giulio Gavardi (giulio.gavardi@eng.it)
  */

Ext.ns("Sbi.commons");



Sbi.commons.Utilities = {

		/**
		 *  method urlToString as ext's urlEncode transforms url toString, but without encoding, used in export to preserve white spaces among paramerameters value
		 */
		
		urlToString: function(o) {

    if(!o){
        return "";
    }
    var buf = [];
    for(var key in o){
        var ov = o[key], k = key;
    	//var ov = o[key], k = encodeURIComponent(key);
        var type = typeof ov;
        if(type == 'undefined'){
            buf.push(k, "=&");
        }else if(type != "function" && type != "object"){
        	buf.push(k, "=", ov, "&");
        	//buf.push(k, "=", encodeURIComponent(ov), "&");
        }else if(ov instanceof Array){
            if (ov.length) {
             for(var i = 0, len = ov.length; i < len; i++) {
            	 ins = (ov[i] === undefined) ? '' : ov[i];
            	 buf.push(k, "=", ins, "&");
            	 //buf.push(k, "=", encodeURIComponent(ov[i] === undefined ? '' : ov[i]), "&");
             }
         } else {
             buf.push(k, "=&");
         }
        }
    }
    buf.pop();
    return buf.join("");

}
};
