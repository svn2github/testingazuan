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
    
    //this.subObjectsStore.addListener('load', function() { this.doLayout(); alert('cioc');}, this);

    function visibilityRenderer(val) {
        if (val) {
            return LN('sbi.execution.subobjects.visibility.public');
        } else {
            return LN('sbi.execution.subobjects.visibility.private');
        }
    }
    
    function fireOnSelectedEvent(grid, rowIndex, event) {
    	var selectedRecord =  grid.getStore().getAt(rowIndex);
    	var subObjectId = selectedRecord.get('id');
    	this.fireEvent('onselected', subObjectId);
    }
    
    this.executeColumn = new Ext.grid.ButtonColumn({
	       header:  '',
	       dataIndex: 'delete',
	       iconCls: 'icon-execute',
	       clickHandler: function(e, t) {
	          var index = this.grid.getView().findRowIndex(t);
	          var selectedRecord = this.grid.subObjectsStore.getAt(index);
	          var subObjectId = selectedRecord.get('id');
	          this.grid.fireEvent('onselected', subObjectId);
	       },
	       width: 25,
	       renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' '+this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
	});
    
    this.sm = new Ext.grid.CheckboxSelectionModel();
    
	var c = Ext.apply({}, config, {
        store: this.subObjectsStore
        , columns: [
            {id: "id", header: "Id", sortable: true, dataIndex: 'id',  hidden: true}
            , {header: LN('sbi.execution.subobjects.name'), sortable: true, dataIndex: 'name'}
            , {header: LN('sbi.execution.subobjects.description'), sortable: true, dataIndex: 'description'}
            , {header: LN('sbi.execution.subobjects.owner'), sortable: true, dataIndex: 'owner'}
            , {header: LN('sbi.execution.subobjects.creationDate'), sortable: true, dataIndex: 'creationDate'} //, renderer: Ext.util.Format.dateRenderer('d/m/Y')},
            , {header: LN('sbi.execution.subobjects.lastModificationDate'), sortable: true, dataIndex: 'lastModificationDate'} //, renderer: Ext.util.Format.dateRenderer('d/m/Y')},
            , {header: LN('sbi.execution.subobjects.visibility'), sortable: true, dataIndex: 'visibility', renderer: visibilityRenderer}
            , this.executeColumn
            , this.sm
        ]
        , plugins: this.executeColumn
		, viewConfig: {
        	forceFit: true
		}
        , tbar:[
           '->'
           , {
        	   text: LN('sbi.execution.subobjects.deleteSelected')
        	   , tooltip: LN('sbi.execution.subobjects.deleteSelectedTooltip')
        	   , iconCls:'icon-remove'
        	   , scope: this
        	   , handler : this.deleteSelectedSubObjects
           	}
        ]
        , listeners: {
			rowdblclick: fireOnSelectedEvent
        }
        , collapsible: true
        , title: LN('sbi.execution.subobjects.title')
        , autoScroll: true
        , sm : this.sm
        //, layout: 'fit'
        , height: 200
	});   
	
	// constructor
    Sbi.execution.SubObjectsPanel.superclass.constructor.call(this, c);
    
    this.addEvents('onselected');
    
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
		var recordsSelected = this.getSelectionModel().getSelections();
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
	
	//, getSelectedSubObjectId: function() {
	//	return this.selectedSubObjectId;
	//}
	
});