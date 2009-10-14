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

Sbi.execution.SubobjectsPanel = function(config, doc) {
	
	var c = Ext.apply({
		// defaults
	}, config || {});
	
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
	//this.subobjectPreference = c.subobject;
	this.executionInstance = null;
	//this.selectedSubObjectId = null;
	
    this.subObjectsStore = new Ext.data.JsonStore({
        root: 'results'
        , idProperty: 'id'
        , fields: ['id', 'name', 'description', 'owner', 
                   {name:'creationDate', type:'date', dateFormat: Sbi.config.clientServerTimestampFormat}, 
                   {name:'lastModificationDate', type:'date', dateFormat: Sbi.config.clientServerTimestampFormat}, 
                   'visibility']
		, url: this.services['getSubObjectsService']
    }); 
    
    this.executeColumn = new Ext.grid.ButtonColumn({
	       header:  '',
	       dataIndex: 'delete',
	       iconCls: 'icon-execute',
	       clickHandler: function(e, t) {
	          var index = this.grid.getView().findRowIndex(t);
	          var selectedRecord = this.grid.subObjectsStore.getAt(index);
	          var subObjectId = selectedRecord.get('id');
	          this.grid.fireEvent('executionrequest', subObjectId);
	       },
	       width: 25,
	       renderer : function(v, p, record){
	           return '<center><img class="x-mybutton-'+this.id+' grid-button ' +this.iconCls+'" width="16px" height="16px" src="'+Ext.BLANK_IMAGE_URL+'"/></center>';
	       }
	});
    
    var isUserAbleToDeleteSubObjects = 
    	Sbi.user.functionalities.contains('SaveSubobjectFunctionality') &&
    	(doc.typeCode != 'DATAMART' || Sbi.user.functionalities.contains('BuildQbeQueriesFunctionality'));
    
    var columns = [
       {id: "id", header: "Id", sortable: true, dataIndex: 'id',  hidden: true}
       , {header: LN('sbi.execution.subobjects.name'), sortable: true, dataIndex: 'name'}
       , {header: LN('sbi.execution.subobjects.description'), sortable: true, dataIndex: 'description'}
       , {header: LN('sbi.execution.subobjects.owner'), sortable: true, dataIndex: 'owner'}
       , {header: LN('sbi.execution.subobjects.creationDate'), sortable: true, dataIndex: 'creationDate', renderer: Ext.util.Format.dateRenderer(Sbi.config.localizedDateFormat)} 
       , {header: LN('sbi.execution.subobjects.lastModificationDate'), sortable: true, dataIndex: 'lastModificationDate', renderer: Ext.util.Format.dateRenderer(Sbi.config.localizedDateFormat)} 
       , {
       	header: LN('sbi.execution.subobjects.visibility')
       	, sortable: true
       	, dataIndex: 'visibility'
       	, renderer: function(val) {
       		return val? LN('sbi.execution.subobjects.visibility.public'): LN('sbi.execution.subobjects.visibility.private')
       	}
       }
       , this.executeColumn
    ];
    
    var tbar = undefined;
    
    if (isUserAbleToDeleteSubObjects) {
    	this.sm = new Ext.grid.CheckboxSelectionModel();
    	columns.push(this.sm);
    	tbar = [
		    '->'
		    , {
		 	   text: LN('sbi.execution.subobjects.deleteSelected')
		 	   , tooltip: LN('sbi.execution.subobjects.deleteSelectedTooltip')
		 	   , iconCls:'icon-remove'
		 	   , scope: this
		 	   , handler : this.deleteSelectedSubObjects
		    }
    	];
    } else {
    	this.sm = new Ext.grid.RowSelectionModel({singleSelect:false});
    }
    
    this.shortcutsHiddenPreference = config.shortcutsHidden !== undefined ? config.shortcutsHidden : false;
    
    this.isHidden = !Sbi.user.functionalities.contains('SeeSubobjectsFunctionality')
        			||
    				this.shortcutsHiddenPreference;
    
	c = Ext.apply({}, c, {
        store: this.subObjectsStore
        , columns: columns
        , plugins: this.executeColumn
		, viewConfig: {
        	forceFit: true
        	, emptyText: LN('sbi.execution.subobjects.emptyText')
		}
        , tbar: tbar
        , collapsible: false
        , title: LN('sbi.execution.subobjects.title')
        , autoScroll: true
        , sm : this.sm
        , height: 200
        , hidden: this.isHidden
	});
	
	// constructor
    Sbi.execution.SubobjectsPanel.superclass.constructor.call(this, c);
    
    this.on('rowdblclick', this.onRowDblClick, this);
    
    this.addEvents('executionrequest', 'ready');
    
};

Ext.extend(Sbi.execution.SubobjectsPanel, Ext.grid.GridPanel, {
	
	services: null
	, subObjectsStore: null
	, sm: null
	, executionInstance: null
	//, selectedSubObjectId: null
	   
    // public methods
	
	, synchronize: function( executionInstance ) {
		
		this.executionInstance = executionInstance;
		if (this.isHidden === false) {
			this.subObjectsStore.on(
				'load', 
				function() {this.fireEvent('ready');},
				this
			);
			this.subObjectsStore.load({params: executionInstance});
		} else {
			// must fire 'ready' event to inform that the panel is ready (see ParametersSelectionPage.js)
			this.fireEvent('ready');
		}
		
	}

	, deleteSelectedSubObjects: function() {
		var recordsSelected = this.getSelectionModel().getSelections();
		if (recordsSelected && recordsSelected.length > 0) {
			var ids = new Array();
			for (var count = 0; count < recordsSelected.length; count++) {
				// check if user is able to delete subobject
				var aRecord = recordsSelected[count];
				if (!Sbi.user.functionalities.contains('DocumentAdminManagement') && aRecord.get('owner') != Sbi.user.userId) {
					Sbi.exception.ExceptionHandler.showWarningMessage(LN('sbi.execution.subobjects.cannotDeleteSubObject') + ' \'' + aRecord.get('name') + '\'', 'Warning');
					return;
				}
				ids[count] = aRecord.get('id');
			}
			var idsJoined = ids.join(',');
			
			Ext.Ajax.request({
		        url: this.services['deleteSubObjectsService'],
		        params: {'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID, 'id': idsJoined},
		        success : function(response, options) {
		      		if (response && response.responseText !== undefined) {
		      			var content = Ext.util.JSON.decode( response.responseText );
		      			if (content !== undefined && content.result == 'OK') {
			  	  			// removes the subobjects from the store
			  	  			for (var count = 0; count < recordsSelected.length; count++) {
			  	  				this.subObjectsStore.remove(recordsSelected[count]);
			  	  			}
		      			} else {
			      			Sbi.exception.ExceptionHandler.showErrorMessage('Error while deleting customized views', 'Service Error');
			      		}
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Error while deleting customized views', 'Service Error');
		      		}
		        },
		        scope: this,
				failure: Sbi.exception.ExceptionHandler.handleFailure      
			});
		} else {
			Sbi.exception.ExceptionHandler.showWarningMessage(LN('sbi.execution.subobjects.noSubObjectsSelected'), 'Warning');
		}
	}
	
	// private methods
	
	, onRowDblClick: function(grid, rowIndex, event) {
    	var selectedRecord =  grid.getStore().getAt(rowIndex);
    	var subObjectId = selectedRecord.get('id');
    	this.fireEvent('executionrequest', subObjectId);
    }
	
});