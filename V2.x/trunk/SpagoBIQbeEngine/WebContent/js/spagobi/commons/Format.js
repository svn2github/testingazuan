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
  * Public Functions
  * 
  *  [list]
  * 
  * 
  * Authors
  * 
  * - Andrea Gioia (adrea.gioia@eng.it)
  */

Ext.ns("Sbi.qbe.commons");

Sbi.qbe.commons.Format = function(){
 
	alert('Sbi.qbe.commons.Format');
	
	return {
		/**
         * Cut and paste from Ext.util.Format
         */
        date : function(v, format){
			alert(v + ' : ' + format.toSource());
			
			format = format || "m/d/Y";
			
			if(typeof format === 'string') {
				format = {
					dateFormat: format,
			    	nullValue: ''
				};
			}
	
			alert(v + ' : ' + format.toSource());
			
            if(!v){
                return format.nullValue;
            }
            if(!(v instanceof Date)){
                v = new Date(Date.parse(v));
            }
            return v.dateFormat(format.dateFormat);
        }

        /**
         * Cut and paste from Ext.util.Format
         */
        , dateRenderer : function(format){
            return function(v){
                return Ext.util.Format.date(v, format);
            };
        }
        
        
        /**
         * thanks to Condor: http://www.extjs.com/forum/showthread.php?t=48600
         */
        , number : function(v, format)  {
    		
        	format = Ext.apply({}, format || {}, {
	    		decimalSeparator: '.',
	    		decimalPrecision: 2,
	    		groupingSeparator: ',',
	    		groupingSize: 3,
	    		currencySymbol: '$',
	    		nullValue: ''
	    		
    		});

        	if(!v) {
        		 return format.nullValue;
        	}
        	
        	if (typeof value !== 'number') {
    			value = String(value);
    			if (format.currencySymbol) {
    				value = value.replace(format.currencySymbol, '');
    			}
    			if (format.groupingSeparator) {
    				value = value.replace(new RegExp(format.groupingSeparator, 'g'), '');
    			}
    			if (format.decimalSeparator !== '.') {
    				value = value.replace(format.decimalSeparator, '.');
    			}
    			value = parseFloat(value);
    		}
    		var neg = value < 0;
    		value = Math.abs(value).toFixed(format.decimalPrecision);
    		var i = value.indexOf('.');
    		if (i >= 0) {
    			if (format.decimalSeparator !== '.') {
    				value = value.slice(0, i) + format.decimalSeparator + value.slice(i + 1);
    			}
    		} else {
    			i = value.length;
    		}
    		if (format.groupingSeparator) {
    			while (i > format.groupingSize) {
    				i -= format.groupingSize;
    				value = value.slice(0, i) + format.groupingSeparator + value.slice(i);
    			}
    		}
    		if (format.currencySymbol) {
    			value = format.currencySymbol + value;
    		}
    		if (neg) {
    			value = '-' + value;
    		}
    		return value;
        }   
        
        , numberRenderer : function(format){
            return function(v){
                return Sbi.qbe.commons.Format.number(v, format);
            };
        }
        
        , string : function(v, format) {
        	format = Ext.apply({}, format || {}, {
	    		trim: true,
	    		maxLength: null,
	    		ellipsis: true,
	    		changeCase: null, // null | 'capitalize' | 'uppercase' | 'lowercase'
	    		prefix: '',
	    		suffix: '',
	    		nullValue: ''
    		});
        	
        	if(!v){
                return format.nullValue;
            }
        	
        	if(format.trim) v = Ext.util.Format.trim(v);
        	if(format.maxLength) {
        		if(format.ellipsis === true) {
        			v = Ext.util.Format.ellipsis(v, format.maxLength);
        		} else {
        			v = Ext.util.Format.substr(v, 0, format.maxLength);
        		}
        	}
        	if(format.changeCase){
        		if(format.changeCase === 'capitalize') {
        			v = Ext.util.Format.capitalize(v);
        		} else if(format.changeCase === 'uppercase') {
        			v = Ext.util.Format.uppercase(v);
        		} else if(format.changeCase === 'lowercase') {
        			v = Ext.util.Format.lowercase(v);
        		}        		
        	}
        	if(format.prefix) v = format.prefix+ v;
        	if(format.suffix) v =  v + format.suffix;
        	
        	return v;
        }
        
        , stringRenderer : function(format){
            return function(v){
                return Sbi.qbe.commons.Format.string(v, format);
            };
        }
        
        , boolean : function(v, boolean) {
        	format = Ext.apply({}, format || {}, {
	    		trueSymbol: 'true',
	    		falseSymbol: 'false',
	    		nullValue: ''
    		});
        	
        	if(v === true){
        		 v = format.trueSymbol;
            } else if(v === true){
            	 v = format.falseSymbol;
            } else {
            	 v = format.nullValue;
            }
        	
        	return v;
        }
        
        , booleanRenderer : function(format){
            return function(v){
                return Sbi.qbe.commons.Format.boolean(v, format);
            };
        }
        
	};
	
}();

alert('>' + Sbi.qbe.commons.Format);






	