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

Ext.ns("Sbi.execution");

Sbi.execution.SubObjectsPanel = function(config) {
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getSubObjectsService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_SUBOBJECTS_ACTION'
		, baseParams: params
	});
	this.services['deleteSubObjectsService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DELETE_SUBOBJECTS_ACTION'
		, baseParams: params
	});
	
    this.subObjectsStore = new Ext.data.JsonStore({
        root: 'results'
        , idProperty: 'id'
        , fields: ['name', 'description', 'owner', 'creationDate', 'lastModificationDate', 'visibility']

		, proxy: new Ext.data.HttpProxy({
			url: this.services['getSubObjectsService']
		})
   
    }); 

    // create the Grid
    this.subObjectsPanel = new Ext.grid.GridPanel({
        store: this.subObjectsStore,
        columns: [
            {id: "id", header: "Id", sortable: true, dataIndex: 'id',  hidden: true},
            {header: LN('sbi.execution.subobjects.name'), sortable: true, dataIndex: 'name'},
            {header: LN('sbi.execution.subobjects.description'), sortable: true, dataIndex: 'description'},
            //{header: LN('sbi.execution.subobjects.owner'), sortable: true, dataIndex: 'owner'},
            //{header: LN('sbi.execution.subobjects.creationDate'), sortable: true, dataIndex: 'creationDate', renderer: Ext.util.Format.dateRenderer('d/m/Y')},
            //{header: LN('sbi.execution.subobjects.lastModificationDate'), sortable: true, dataIndex: 'lastModificationDate', renderer: Ext.util.Format.dateRenderer('d/m/Y')},
            //{header: LN('sbi.execution.subobjects.visibility'), sortable: true, dataIndex: 'visibility'}
        ],
		viewConfig: {
        	forceFit: true
		},
		forceFit: false,
        stripeRows: true,
        collapsible: true,
        title: LN('sbi.execution.subobjects.title')
    });
    
	var c = Ext.apply({}, config, {
		items: [this.subObjectsPanel]
	});   
	
	// constructor
    Sbi.execution.SubObjectsPanel.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.execution.SubObjectsPanel, Ext.Panel, {
	
	services: null
	, subObjectsStore: null
	, subObjectsPanel: null
	   
    // public methods
	
	, loadSubObjects: function( executionInstance ) {
		this.subObjectsStore.load({params: executionInstance});
	}

	, deleteSubObjects: function() {
		
	}
});