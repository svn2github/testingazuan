/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2009 Engineering Ingegneria Informatica S.p.A.
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
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */

Ext.ns("Sbi.browser");

Sbi.browser.FilterPanel = function(config) { 
	
	var filterPanel = new Ext.Panel({
    	frame:true,
    	margins:'15 0 0 0',
    	title: 'Filter',
    	collapsible:true,
    	titleCollapse: true,
    	style : 'margin: 0px 0px 10px 0px'
    });
    
    var sortPanel = new Ext.Panel({
    	frame:true,
    	margins:'15 0 0 0',
    	title: 'Sort',
    	collapsible:true,
    	titleCollapse: true,
    	style : 'margin: 0px 0px 10px 0px'
    });
    
    var groupPanel = new Ext.Panel({
    	frame:true,
    	margins:'15 0 0 0',
    	title: 'Group',
    	collapsible:true,
    	titleCollapse: true,
    	style : 'margin: 0px 0px 10px 0px'
    });
    
	var c = Ext.apply({}, config, {
		bodyStyle:'padding:6px 6px 6px 6px; background-color:#FFFFFF',
    	items: [sortPanel, groupPanel, filterPanel]
	});   
	
	Sbi.browser.FilterPanel.superclass.constructor.call(this, c);   
};

Ext.extend(Sbi.browser.FilterPanel, Ext.Panel, {
    
    modality: null
    
});