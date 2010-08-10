
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

qx.Class.define("spagobi.ui.Table",
{
  //extend : qx.legacy.ui.table.Table,//change
  extend : qx.ui.table.Table,

  /**
   * Constructor to create a list (table)
   * 
   * *Example*
   * <p><code>
   * var obj = new spagobi.ui.custom.MasterDetailsPage();
   * var records = spagobi.app.data.DataService.loadFeatureRecords();
   * var listPage = new spagobi.ui.Table(obj, records );
   * </code>
   * 
   * @param controller Object which is used to set the data of the form
   * @param data Object containing the layout of the list and the data of the list and form
   */	
  construct : function(controller, data)
  {
    // Establish controller link
    this._controller = controller;
    this._data = data;
    // Reset the class member variables so that each button of icon bar shows its resp. table columns
    this.columnIds = [];
    this.columnNames = {};
    
    for(var i = 0; i < this._data.meta.length; i++) {
    	this.columnIds[i] =  this._data.meta[i].dataIndex;
    	this.columnNames[this._data.meta[i].dataIndex] = this._data.meta[i].name;
    }

    // Create table model
    //this._tableModel = new qx.legacy.ui.table.model.Simple();//change
    this._tableModel = new qx.ui.table.model.Simple();
    
    this._tableModel.setColumnIds( this.columnIds );
	this._tableModel.setColumnNamesById( this.columnNames );

	
    // Customize the table column model. We want one that
    // automatically resizes columns.
    this.base(arguments, this._tableModel,
    {
      tableColumnModel : function(obj) {
        //return new qx.legacy.ui.table.columnmodel.Resize(obj);//change
        return new qx.ui.table.columnmodel.Resize(obj);
      }
    });

  this.set({
     // 	flex: 1
     //     height: 150 
      });
   

	
    // Configure columns
    var columnModel = this.getTableColumnModel();
    var resizeBehavior = columnModel.getBehavior();
    
//	alert(this._data.ID);
	
   if (this._data.ID != undefined){
   		this.Identity.dummyId = this._data.ID;
 //   	alert(this.Identity);
    	if (this._data.ID == "ROLES"){
		
	//		var propertyCellRendererFactory = new qx.legacy.ui.table.cellrenderer.Dynamic(this.propertyCellRendererFactoryFunc);//change
			var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.propertyCellRendererFactoryFunc);
	 	
   		 	//var propertyCellEditorFactory = new qx.legacy.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);//change
   		 	var propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);

			for(i=0; i<this._data.columns.length; i++){
				columnModel.setDataCellRenderer(this._data.columns[i], propertyCellRendererFactory);
				columnModel.setCellEditorFactory(this._data.columns[i], propertyCellEditorFactory);
			}
			
			this.addListener("cellClick",this._onCellClick, this,false );//this.Identity,
			
    }
    
   		else if (this._data.ID == "Scheduler"){
    		
    		var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.cellRendererFactoryFunction);
   // 		var propertyCellEditorFactory = new qx.ui.table.cellrenderer.Image();//new qx.ui.table.cellrenderer.Dynamic(this.propertyCellEditorFactoryFunc1);
	 		
   		 	//var propertyCellEditorFactory = new qx.legacy.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);//change
  // 		 	var propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);
			
	//		alert(this._data.columns.length);
			for(i=0; i<this._data.columns.length; i++){
				
	/*			columnModel.setCellRenderer(function(cellInfo){
					if (cellInfo.row == this._data.columns[i])
					    return new qx.ui.table.cellrenderer.Image;
});*/
				columnModel.setDataCellRenderer(this._data.columns[i], propertyCellRendererFactory);
		//		columnModel.setCellEditorFactory(this._data.columns[i], propertyCellEditorFactory);
			}
	//		this.addListener("changeDataRowRenderer",this._onImageClick,this, false);//this._data.ID,
			this.addListener("cellClick",this._onCellClick,this, false);//this._data.ID,
	
	}
	
		else if (this._data.ID == "LINK"){
			
		
		}
			this.setStatusBarVisible(false);
		    this.getDataRowRenderer().setHighlightFocusRow(true);
			this._tableModel.setDataAsMapArray(this._data.rows, true);
    }
	//this.setWidth('100%');//change
    //this.setHeight('100%');//change
    //this.setBorder("inset-thin");//change
    //this.setDecorator(new qx.ui.decoration.Single(null,"solid",null));
    
    //this.setOverflow("auto");//change ... find
    else {
    this.setStatusBarVisible(false);
    this.getDataRowRenderer().setHighlightFocusRow(true);
   // this.getPaneScroller(0).setShowCellFocusIndicator(false);
    
    this._tableModel.setDataAsMapArray(this._data.rows, true);
    
    // Add selection listener
    this.getSelectionModel().addListener("changeSelection", this._onChangeSelection, this);
    
    }
  },

  /**
   * members of the class
   */	
  members :
  {
  	 _data : undefined,		
  	 columnIds : [],
     columnNames : {},
     Identity: {},
     config : {}, 	
     tree1 : undefined,
     nodeLabel: undefined,
     inputField: undefined,
  	 propertyCellEditorFactoryFunc : function (cellInfo) 
  	  {
  			return new qx.ui.table.celleditor.CheckBox;
  	  },
	
	 propertyCellRendererFactoryFunc1 : function (cellInfo)
      {
      		return new qx.ui.table.cellrenderer.Default;
      },
  	 
  	 propertyCellRendererFactoryFunc : function (cellInfo)
      {
        	return new qx.ui.table.cellrenderer.Boolean;
      },
      
     cellRendererFactoryFunction : function (cellInfo)
      {
      	//	alert("Image");
      		return new qx.ui.table.cellrenderer.Image;
      },
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
    	for(var i = 0; i < data.meta.length; i++) {
    		this.columnIds[i] =  data.meta[i].dataIndex;
    		columnNames[data.meta[i].dataIndex] = data.meta[i].name;
    	}
    	
    	this._tableModel.setColumnIds( columnIds );
	    this._tableModel.setColumnNamesById( columnNames );
	    this.getTableModel().setDataAsMapArray(data.rows, true);
        this.getSelectionModel().setSelectionInterval(0, 0);
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
        			//if (this._controller != spagobi.ui.custom.FunctionalityTreeSubClass )
          this._controller.selectDataObject(itemData);
        }
      }
            
    },
    
    _onCellClick : function(e){
    	
		//if ( ! e instanceof qx.event.type.DataEvent){//change
		if ( ! e instanceof qx.event.type.Data){
				return;
			}
        if(this.Identity.dummyId=="ROLES"){
  			var d = new spagobi.ui.Roles(this, e);
		  }
    	  
	    else{
		  	var d = new spagobi.ui.Scheduler(e);
		 }
     }
  }
      
      
   /*   _onImageClick : function(e){
      	var colnum = e.getColumn();
  			var romnum = e.getRow();
  			alert(typeof(this.getTableModel().getValue(colnum,romnum)));
      	
      	
      }
      
   */
      
  
});
