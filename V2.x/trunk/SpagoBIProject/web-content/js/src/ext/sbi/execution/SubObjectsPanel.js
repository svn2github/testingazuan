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
	
	this.executionInstance = null;
	this.selectedSubObjectId = null;
	
    this.subObjectsStore = new Ext.data.JsonStore({
        root: 'results'
        , idProperty: 'id'
        , fields: ['id', 'name', 'description', 'owner', 'creationDate', 'lastModificationDate', 'visibility']
		, url: this.services['getSubObjectsService']
    }); 
    
    this.subObjectsStore.addListener('load', function() { this.doLayout(); alert('cioc');}, this);

    function visibilityRenderer(val) {
        if (val) {
            return LN('sbi.execution.subobjects.visibility.public');
        } else {
            return LN('sbi.execution.subobjects.visibility.private');
        }
    }
    
    this.sm = new Ext.grid.CheckboxSelectionModel();
    
	var c = Ext.apply({}, config, {
        store: this.subObjectsStore,
        columns: [
            {id: "id", header: "Id", sortable: true, dataIndex: 'id',  hidden: true},
            {header: LN('sbi.execution.subobjects.name'), sortable: true, dataIndex: 'name'},
            {header: LN('sbi.execution.subobjects.description'), sortable: true, dataIndex: 'description'},
            {header: LN('sbi.execution.subobjects.owner'), sortable: true, dataIndex: 'owner'},
            {header: LN('sbi.execution.subobjects.creationDate'), sortable: true, dataIndex: 'creationDate'}, //, renderer: Ext.util.Format.dateRenderer('d/m/Y')},
            {header: LN('sbi.execution.subobjects.lastModificationDate'), sortable: true, dataIndex: 'lastModificationDate'}, //, renderer: Ext.util.Format.dateRenderer('d/m/Y')},
            {header: LN('sbi.execution.subobjects.visibility'), sortable: true, dataIndex: 'visibility', renderer: visibilityRenderer},
            this.sm
        ],
		viewConfig: {
        	forceFit: true
		},
        tbar:[
           '->'
           , {
            text: LN('sbi.execution.subobjects.deleteSelected'),
            tooltip: LN('sbi.execution.subobjects.deleteSelectedTooltip'),
            iconCls:'icon-remove',
            scope: this,
            handler : this.deleteSelectedSubObjects
        }],
        collapsible: true,
        title: LN('sbi.execution.subobjects.title'),
        sm : this.sm,
        //layout: 'fit'
        height: 300
	});   
	
	// constructor
    Sbi.execution.SubObjectsPanel.superclass.constructor.call(this, c);
    
};

Ext.extend(Sbi.execution.SubObjectsPanel, Ext.grid.GridPanel, {
	
	services: null
	, subObjectsStore: null
	, sm: null
	, executionInstance: null
	, selectedSubObjectId: null
	   
    // public methods
	
	, loadSubObjects: function( executionInstance ) {
		this.subObjectsStore.load({params: executionInstance});
		this.executionInstance = executionInstance;
	}

	, deleteSelectedSubObjects: function() {
		alert('entrato in deleteSelectedSubObjects');
		var recordsSelected = this.getSelectionModel().getSelections();
		alert('recordsSelected' + recordsSelected);
		if (recordsSelected && recordsSelected.length > 0) {
			var ids = new Array();
			for (var count = 0; count < recordsSelected.length; count++) {
				ids[count] = recordsSelected[count].get('id');
			}
			var idsJoined = ids.join(',');
	
			Ext.Ajax.request({
		        url: this.services['deleteSubObjectsService'],
		        params: {'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID, 'id': idsJoined},
		        callback : function(options , success, response) {
		  	  		if(success) {
		  	  			// removes the subobjects from the store
		  	  			for (var count = 0; count < recordsSelected.length; count++) {
		  	  				this.subObjectsStore.remove(recordsSelected[count]);
		  	  			}
		  	  		} else { 
		  	  			Sbi.exception.ExceptionHandler.showErrorMessage('Cannot detele customized views', 'Service Error');
		  	  		}
		        },
		        scope: this,
				failure: Sbi.exception.ExceptionHandler.handleFailure      
			});
		} else {
			Sbi.exception.ExceptionHandler.showWarningMessage(LN('sbi.execution.subobjects.noSubObjectsSelected'), 'Warning');
		}
	}
	
	, getSelectedSubObjectId: function() {
		return this.selectedSubObjectId;
	}
	
});