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
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi");

Sbi.Sync = function(){
    
	// private variables
	var FORM_ID = 'download-form';
	var METHOD = 'post';
	
    // public space
	return {
		
		form: null
		
		, request: function(o) {
			var f = this.getForm();
			//var d = f.getDom();
			if(o.method) f.method = o.method;
			
			f.action = o.url;
			if(o.params) {
				f.action = Ext.urlAppend(f.action, Ext.urlEncode(o.params) );
			}
			
			f.submit();
		}
	
		, getForm: function() {
			//by unique request
			if(this.form === null) {
				this.form = document.getElementById(FORM_ID);
				if(!this.form) {
					var dh = Ext.DomHelper;
					this.form = dh.append(Ext.getBody(), {
					    id: FORM_ID
					    , tag: 'form'
					    , method: METHOD
					    , cls: 'download-form'
					});
				}
			}
			return this.form;
		}
	
	}
}();	