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

Sbi.browser.DocumentsBrowser = function(config) {    
    // sub-components   
	
	this.treePanel = new Sbi.browser.DocumentsTree({
        border:true
    });
	
	this.filterPanel = new Sbi.browser.FilterPanel({
        title:'Sort, group and filter',
        border:true
    });
	
	this.searchPanel = new Sbi.browser.SearchPanel({
        title:'Search',
        border:true
    });
	
	
	
	this.westRegionContainer = new Ext.Panel({
	       id:'westRegionContainer',
	       split:true,
	       border:true,
	       frame:true,
	       collapsible: true,
	       //margins:'0 0 0 15',
	       layout:'accordion',
	       layoutConfig:{
	          animate:true
	       },
	       items: [
	               this.searchPanel
	               , this.filterPanel
	               , this.treePanel
	       ]
	});
	
	this.detailPanel = new Sbi.browser.FolderDetailPanel({ 
        region: 'center',
        margins: '0 3 3 0',
        collapsed: false,
        split: true,
        autoScroll: false,
        height: 100,
        minHeight: 100,
        width: 100,
        minWidth: 0,
        layout: 'fit'                
    });
	
	var c = Ext.apply({}, config, {
		layout: 'border',
	    border: false,
	    items: [ 
	            // CENTER REGION ---------------------------------------------------------
	            this.detailPanel, 
	            // WEST REGION -----------------------------------------------------------
	            new Ext.Panel({               
	                region: 'west',
	                border: false,
	                frame: false,
	                //margins: '0 0 3 3',
	                collapsible: true,
	                collapsed: false,
	                hideCollapseTool: true,
	                titleCollapse: true,
	                collapseMode: 'mini',
	                split: true,
	                autoScroll: false,
	                width: 280,
	                minWidth: 280,
	                layout: 'fit',
	                items: [this.westRegionContainer]
	              })
	            // NORTH HREGION -----------------------------------------------------------
	            /*
	          	,new Sbi.browser.Toolbar({
	            	region: 'north',
	            	margins: '3 3 3 3',
	            	autoScroll: false,
	            	height: 30,
	            	layout: 'fit'
	          	})
	          	*/
	        ]
	});   
    
    Sbi.browser.DocumentsBrowser.superclass.constructor.call(this, c);
    
    this.treePanel.addListener('click', this.onTreeNodeClick, this);
    this.detailPanel.addListener('ondocumentclick', this.onDocumentClick, this);
    this.detailPanel.addListener('onfolderclick', this.onFolderClick, this);
    this.detailPanel.addListener('onbreadcrumbclick', this.onBreadCrumbClick, this);
    
    
    
    
}




Ext.extend(Sbi.browser.DocumentsBrowser, Ext.Panel, {
    
    treePanel: null
    , filterPanel: null
    , searchPanel: null
    , westRegionContainer: null
    , detailPanel: null
    
    , onTreeNodeClick: function(node, e) {
		this.detailPanel.loadFolder(node.id);
	}

	, onDocumentClick: function(panel, r) {
		var execDocumentService = Sbi.config.serviceRegistry.getServiceUrl('ExecuteBIObjectPage', false, true);
		execDocumentService += '&MESSAGEDET=EXEC_PHASE_CREATE_PAGE';
		execDocumentService += '&OBJECT_ID=' + r.id;
		window.location=execDocumentService;
	}
	
	, onFolderClick: function(panel, r) {
		this.detailPanel.loadFolder(r.id);
	}
	
	, onBreadCrumbClick: function(panel, b) {
		this.detailPanel.loadFolder(b.id);
	}

	// http://localhost:8080/SpagoBI/servlet/AdapterHTTP?OBJECT_ID=28&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&PAGE=ExecuteBIObjectPage
   
});