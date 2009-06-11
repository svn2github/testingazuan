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
  * - Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.execution");

Sbi.execution.SnapshotsPanel = function(config) {
	
	var c = Ext.apply({
		// defaults
	}, config || {});
	
	// always declare exploited services first!
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getSnapshotsService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_SNAPSHOTS_ACTION'
		, baseParams: params
	});
	
	this.selectedSnapshotId = null;
	
    this.snapshotsStore = new Ext.data.JsonStore({
        root: 'results'
        , idProperty: 'id'
        , fields: ['id', 'name', 'description', {name:'creationDate', type:'date', dateFormat: Sbi.config.clientServerDateFormat}]
		, url: this.services['getSnapshotsService']
    });
    
    this.executeColumn = new Ext.grid.ButtonColumn({
	       header:  '',
	       dataIndex: 'delete',
	       iconCls: 'icon-execute',
	       clickHandler: function(e, t) {
	          var index = this.grid.getView().findRowIndex(t);
	          var selectedRecord = this.grid.snapshotsStore.getAt(index);
	          var snapshotId = selectedRecord.get('id');
	          this.grid.fireEvent('executionrequest', snapshotId);
	       },
	       width: 25,
	       renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
	});
    
	c = Ext.apply({}, c, {
        store: this.snapshotsStore
        , columns: [
            {id: "id", header: "Id", sortable: true, dataIndex: 'id',  hidden: true}
            , {header: LN('sbi.execution.snapshots.name'), sortable: true, dataIndex: 'name'}
            , {header: LN('sbi.execution.snapshots.description'), sortable: true, dataIndex: 'description'}
            , {header: LN('sbi.execution.snapshots.creationDate'), sortable: true, dataIndex: 'creationDate', renderer: Ext.util.Format.dateRenderer(Sbi.config.localizedDateFormat)} 
            , this.executeColumn
        ]
        , plugins: this.executeColumn
		, viewConfig: {
        	forceFit: true
		}
        , collapsible: false
        , title: LN('sbi.execution.snapshots.title')
        , autoScroll: true
        , height: 200
	});
	
	// constructor
    Sbi.execution.SnapshotsPanel.superclass.constructor.call(this, c);
    
    this.on('rowdblclick', this.onRowDblClick, this);
    
    this.addEvents('executionrequest');
    
};

Ext.extend(Sbi.execution.SnapshotsPanel, Ext.grid.GridPanel, {
	
	services: null
	, snapshotsStore: null
	, executionInstance: null
	, selectedSnapshotId: null
	   
    // public methods
	
	, synchronize: function( executionInstance ) {
		this.snapshotsStore.load({params: executionInstance});
		this.executionInstance = executionInstance;
	}

	//private methods

	, onRowDblClick: function(grid, rowIndex, event) {
		var selectedRecord =  grid.getStore().getAt(rowIndex);
		var snapshotId = selectedRecord.get('id');
		this.fireEvent('executionrequest', snapshotId);
	}
	
});