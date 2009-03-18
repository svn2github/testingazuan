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

Sbi.browser.DocumentsTree = function(config) {    
    // sub-components   
    
	var loadFTreeFoldersService = Sbi.config.serviceRegistry.getServiceUrl('GET_FTREE_FOLDERS_ACTION');
	
	this.loader = new Ext.tree.TreeLoader({
        dataUrl   : loadFTreeFoldersService
    });
		
    var c = Ext.apply({}, config, {
    	title            : LN('sbi.browser.documentstree.title'),
        collapsible      : true,
        enableDD		 : true,
        animCollapse     : true,
        collapseFirst	 : false,
        border           : false,
        autoScroll       : true,
        containerScroll  : true,
        animate          : false,
        trackMouseOver 	 : true,
        useArrows 		 : true,
        loader           : this.loader
    })
    
    Sbi.browser.DocumentsTree.superclass.constructor.call(this, c);
    
    this.rootNode = new Ext.tree.AsyncTreeNode({
    	id			: 'rootNode',
        text		: LN('sbi.browser.documentstree.root'),
        iconCls		: 'icon-ftree-root',
        expanded	: true,
        expandable  : false,
        draggable	: false
    });    
    this.setRootNode(this.rootNode);    
    
    this.addListener('click', this.onClickTestFn, this);
}




Ext.extend(Sbi.browser.DocumentsTree, Ext.tree.TreePanel, {
    
	loader: null
	, rootNode: null
	
	, onClickTestFn: function(node, e) {
		alert('id' + node.id + '\ntype:' + node.attributes.type);
	}

	, refresh: function() {
		this.loader.load(this.rootNode, function(){});
	}
   
});
