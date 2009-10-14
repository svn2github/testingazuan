
/* *

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

* */

/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 
 */
 
/**
 * Class to create a list (table) 
 * 
 */ 

/*
#asset(img/spagobi/test/*)
#asset(qx/icon/Oxygen/16/apps/office-calendar.png)
*/

qx.Class.define("qooxdoo.ui.table.Table",
{
  extend : qx.ui.table.Table,

  /**
   * Constructor to create a list (table)
   * 
   * *Example*
   * <p><code>
   * var obj = new qooxdoo.ui.custom.MasterDetailsPage();
   * var records = qooxdoo.app.data.DataService.loadFeatureRecords();
   * var listPage = new qooxdoo.ui.table.Table(obj, records );
   * </code>
   * 
   * @param controller Object which is used to set the data of the form
   * @param config Object containing the layout of the list and the data of the list and form
   */	
  construct : function(controller, config)
  {
    // Establish controller link
    this._controller = controller;
    
    if(config.dataset) {
    	this._data = config.dataset;
    }
    
    
    // Reset the class member variables so that each button of icon bar shows its resp. table columns
    this.columnIds = [];
    this.columnNames = {};
    
    for(var i = 0; i < this._data.meta.length; i++) {
    	this.columnIds[i] =  this._data.meta[i].dataIndex;
    	this.columnNames[this._data.meta[i].dataIndex] = this._data.meta[i].name;
    }

    // Create table model
    this._tableModel = new qx.ui.table.model.Simple();    
    this._tableModel.setColumnIds( this.columnIds );
	this._tableModel.setColumnNamesById( this.columnNames );

	
    // Customize the table column model. We want one that
    // automatically resizes columns.
    this.base(arguments, this._tableModel, {
      tableColumnModel : function(obj) {       
        return new qx.ui.table.columnmodel.Resize(obj);
      }
    });

    this.set({});
   
	
    // Configure columns
    var columnModel = this.getTableColumnModel();
    var resizeBehavior = columnModel.getBehavior();
    
    if (this._data.ID != undefined){
   		this.Identity.dummyId = this._data.ID;
 
    	if (this._data.ID == "ROLES"){
    		var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.propertyCellRendererFactoryFunc);
   		 	var propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);
			for(i=0; i<this._data.columns.length; i++){
				columnModel.setDataCellRenderer(this._data.columns[i], propertyCellRendererFactory);
				columnModel.setCellEditorFactory(this._data.columns[i], propertyCellEditorFactory);
			}
			this.addListener("cellClick",this._onCellClick, this,false );
    	} else if (this._data.ID == "Scheduler"){    		
    		var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.cellRendererFactoryFunction);
 			for(i=0; i<this._data.columns.length; i++){
				
				columnModel.setDataCellRenderer(this._data.columns[i], propertyCellRendererFactory);
			}
			this.addListener("cellClick",this._onCellClick,this, false); 
   		} else if (this._data.ID == "LINK"){
		
		}
    	
    	
    	this.setStatusBarVisible(false);
        this.getDataRowRenderer().setHighlightFocusRow(true);
		this._tableModel.setDataAsMapArray(this._data.rows, true);
    
   	} else {
	   
   		this.setStatusBarVisible(false);
   	    this.getDataRowRenderer().setHighlightFocusRow(true);
	    this._tableModel.setDataAsMapArray(this._data.rows, true);
	    
	    	    
	    
	    
	    if(config.proxy) {
	    	config.proxy.load({}, true, function(dataset){
	    		this.setData(dataset);
	    		this._data = dataset;
	  		}, this);
	    }
	    
	 // Add selection listener
	  this.getSelectionModel().addListener("changeSelection", this._onChangeSelection, this);   
	    
	    //this._data = data;
	    //this.setData(this._data);
    }
  },

  /**
   * members of the class
   */	
  members :
  {
  	 _data : {
 		meta: [
		       {dataIndex: 'id', name: 'Data'}
		]
		, rows: [{id: ''}]    
  	 }
  	 
  	 
  	 , columnIds : [],
     columnNames : {},
     Identity: {},
     config : {}, 	
     tree1 : undefined,
     nodeLabel: undefined,
     inputField: undefined,
     
     
     /**
   	 * Function used to set the data in the list
   	 * 
   	 * *Example*
   	 * <p><code>
   	 * 
   	 * </code>
   	 * 
   	 * @param data Object containing the layout of the list and the data of the list and form
   	 */
     setData: function(data) {
    	 
     	var columnIds = [];
     	var columnNames = {};
     	for(var i = 0; i < data.metaData.fields.length; i++) {
     		this.columnIds[i] =  data.metaData.fields[i].dataIndex;
     		columnNames[data.metaData.fields[i].dataIndex] = data.metaData.fields[i].name;
     	}
     	
     	
     	var cm = new qx.ui.table.model.Simple();
     	cm.setColumnIds( this.columnIds );
     	cm.setColumnNamesById( columnNames );
 	    
 	   
     	var menu = this.__columnVisibilityBt.getMenu();
        if (menu)
        {
          var entries = menu.getChildren();
          for (var i=0,l=entries.length; i<l; i++) {
            if(entries[i])entries[i].destroy();
          }
        }
     	
     	this.setTableModel( cm );    
     	     		
 	    this.getTableModel().setDataAsMapArray(data.rows, true);
        this.getSelectionModel().setSelectionInterval(0, 0); 	    
     },
     
     
     
  	 
     propertyCellEditorFactoryFunc : function (cellInfo) {
  		return new qx.ui.table.celleditor.CheckBox;
  	 },
	
	 propertyCellRendererFactoryFunc1 : function (cellInfo) {
      	return new qx.ui.table.cellrenderer.Default;
     },
  	 
  	 propertyCellRendererFactoryFunc : function (cellInfo) {
        return new qx.ui.table.cellrenderer.Boolean;
     },
      
     cellRendererFactoryFunction : function (cellInfo) {
    	 return new qx.ui.table.cellrenderer.Image;
     },
  	
     
     
    
    /**
  	 * Function used to get the data in the list
  	 * 
  	 * *Example*
  	 * <p><code>
  	 * 
  	 * </code>
  	 * 
  	 * @param data Object containing the layout of the list and the data of the list and form
  	 */
  	 
     getUpdatedData: function() {
     		
     		return this._tableModel.getData();
     },
     
     /**
     * TODOC
     *
     * @type member
     * @param e {Event} TODOC
     * @return {void}
     */
     
     
    _onChangeSelection : function(e) {
      	
      var selectedEntry = this.getSelectionModel().getAnchorSelectionIndex();
     
      if (selectedEntry >= 0) {
        var itemData = this.getTableModel().getRowData(selectedEntry);
				 
        // If this is undefined, the data is not yet ready...
        if (itemData) {        
          this._controller.selectDataObject(itemData);
        }
      }
            
    },
    
    _onCellClick : function(e){
    	
    	if ( ! (e instanceof qx.ui.table.pane.CellEvent)){
			return;
		}
    	
        if(this.Identity.dummyId=="ROLES"){
  			var d = new qooxdoo.ui.table.Roles(this, e);
		  }
    	else{
		  	var d = new qooxdoo.ui.Scheduler(e);
		 }
     }
  }
      
      
  
});
