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
  * - Chiara Chiarelli (chiara.chiarelli@eng.it)
  */

Ext.ns("Sbi.home");

Sbi.home.Footer = function(config) {
	
		var c = Ext.apply({}, config,  {
			region: 'south',
	        xtype: 'panel',
	      
	       
	        items: [
	        	new  Ext.ux.ManagedIframePanel({
						frameConfig:{autoCreate:{id: 'iframeFooter', name:'iframeFooter', style: 'height:30; margins:0px 0px 0px 0px;',scrolling : 'no' ,border: false,layout: 'fit'}}
		                ,defaultSrc : Sbi.config.contextName+'/themes/'+Sbi.config.currTheme+'/html/footer.html'
		                ,border		: false 
		                ,height: 30
		                ,loadMask   : true
		                ,layout: 'fit'
						,collapseMode: 'mini'
						,scrolling  : 'no'	
						 , margins:'0 0 0 0'
						, bodyStyle:'padding:0px 0px 0px 0px',
				})],
	        border: false,
			collapseMode: 'mini',
	        autoHeight: true
		});   
		        
		Sbi.home.Footer.superclass.constructor.call(this, c);
    	
	
};

Ext.extend(Sbi.home.Footer, Ext.Panel, {

	// ---------------------------------------------------------------------------
    // public methods
	// ---------------------------------------------------------------------------

});