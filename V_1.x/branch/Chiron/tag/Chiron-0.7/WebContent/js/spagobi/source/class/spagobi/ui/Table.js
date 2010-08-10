
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


qx.Class.define("spagobi.ui.Table",
{
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
    
    // Reset the class member variables so that each button of icon bar shows its resp. table columns
    this.columnIds = [];
    this.columnNames = {};
    
    for(var i = 0; i < data.meta.length; i++) {
    	this.columnIds[i] =  data.meta[i].dataIndex;
    	this.columnNames[data.meta[i].dataIndex] = data.meta[i].name;
    }

    // Create table model
    this._tableModel = new qx.ui.table.model.Simple();
    
    this._tableModel.setColumnIds( this.columnIds );
	this._tableModel.setColumnNamesById( this.columnNames );

	
    // Customize the table column model. We want one that
    // automatically resizes columns.
    this.base(arguments, this._tableModel,
    {
      tableColumnModel : function(obj) {
        return new qx.ui.table.columnmodel.Resize(obj);
      }
    });

   

	
    // Configure columns
    var columnModel = this.getTableColumnModel();
    var resizeBehavior = columnModel.getBehavior();
	
	
   if (data.ID != undefined){
    	if (data.ID == "ROLES"){
		
			var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.propertyCellRendererFactoryFunc);
	 	
   		 	var propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this.propertyCellEditorFactoryFunc);

			for(i=0; i<data.columns.length; i++){
				columnModel.setDataCellRenderer(data.columns[i], propertyCellRendererFactory);
				columnModel.setCellEditorFactory(data.columns[i], propertyCellEditorFactory);
			}
			
			this.addEventListener("cellClick",this._onCellClick, this );
			
			/*
			columnModel.setDataCellRenderer(2, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(2, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(3, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(3, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(4, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(4, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(5, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(5, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(6, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(6, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(7, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(7, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(8, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(8, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(9, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(9, propertyCellEditorFactory);
			columnModel.setDataCellRenderer(10, propertyCellRendererFactory);
			columnModel.setCellEditorFactory(10, propertyCellEditorFactory);
		
			
			
			
		//	columnModel.setDataCellRenderer(2, new qx.ui.table.cellrenderer.Boolean());
	 	//  columnModel.setDataCellRenderer(3, new qx.ui.table.cellrenderer.Boolean());    
	 //   columnModel.setDataCellRenderer(4, new qx.ui.table.cellrenderer.Dynamic(this.propertyCellRendererFactoryFunc));
	 //   columnModel.setDataCellRenderer(5, new qx.ui.table.cellrenderer.Boolean());
	 //   columnModel.setDataCellRenderer(6, new qx.ui.table.cellrenderer.Boolean());
	 //   columnModel.setDataCellRenderer(7, new qx.ui.table.cellrenderer.Boolean());
	//    columnModel.setDataCellRenderer(9, new qx.ui.table.cellrenderer.Boolean());
	//   columnModel.setDataCellRenderer(10, new qx.ui.table.cellrenderer.Boolean());
		
	*/
    }
    }
	
	// Basic setup
 //this.setDimension('100%', '100%');
    //alert (this.getWidthValue());
    //var a = (92 * screen.width)/100;
   	//this.setWidth(a);
    
    this.setWidth('100%');
    this.setHeight('100%');
    this.setBorder("inset-thin");					//line-bottom
    this.setOverflow("auto");
    this.setStatusBarVisible(false);
    this.getDataRowRenderer().setHighlightFocusRow(true);
    this.getPaneScroller(0).setShowCellFocusIndicator(false);
    
    this._tableModel.setDataAsMapArray(data.rows, true);
    
    // Add selection listener
    this.getSelectionModel().addEventListener("changeSelection", this._onChangeSelection, this);
    
  },

  /**
   * members of the class
   */	
  members :
  {
  	
  	 columnIds : [],
     columnNames : {},
    
    
  	 propertyCellEditorFactoryFunc : function (cellInfo) 

  		{
  			/*var table 			= cellInfo.table;
  			var tableModel 	= table.getTableModel();
  			var rowData			= tableModel.getRowData(cellInfo.row);
  			var metaData		= rowData[3];*/
  		/*	var cellEditor ;  // 	= new qx.ui.table.celleditor.TextField;
  			var validationFunc 	= null;

  			for ( var cmd in this.columnIds )
  			{
  				switch ( cmd )
  				{
  			case "type":
               switch ( this.columnIds['type'] )
               {
                 case "checkbox":
  						     cellEditor = new qx.ui.table.celleditor.CheckBox; break;
               }

  				}
  			}
  			return cellEditor;*/
  			
  			return new qx.ui.table.celleditor.CheckBox;
  		},

  	 
  	 propertyCellRendererFactoryFunc : function (cellInfo)
      {
        	/*var table 		 = cellInfo.table;
  			var tableModel 	 = table.getTableModel();
  			var rowData		 = tableModel.getRowData(cellInfo.row);
  			var metaData	 = rowData[3];
*/
  		/*	for ( var cmd in this.columnIds )
  			{
  				switch ( cmd )
  				{
            case "type":
              switch ( this.columnIds['type'])
              {
                case "checkbox": return new qx.ui.table.cellrenderer.Boolean;
              }
              break;

  				}
        
        return new qx.ui.table.cellrenderer.Default;}*/
        return new qx.ui.table.cellrenderer.Boolean;
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
	
			if ( ! e instanceof qx.event.type.DataEvent){
				return;
			}
			
  			//var event_data = new qx.ui.table.pane.CellEvent(null,null,event);
  			var colnum = e.getColumn();
  			var romnum = e.getRow();
  			
  			if(typeof(this.getTableModel().getValue(colnum,romnum)) != 'boolean'){
  				return;
  			}
  			//	alert(event +" ," +colnum +" ,"+ romnum);
  			//var changedData = event.getData();
  			//alert (typeof(this.getTableModel().getValue(colnum,romnum)));
			
			if ( this.getTableModel().getValue(colnum,romnum) === true )
			{
				//event.setData(false);// == false;
				this.getTableModel().setValue(colnum,romnum,false);
				//	alert (this.getTableModel().getValue(colnum,romnum));
			}
			else 
			{
				//event.setData(true);// == true;
				this.getTableModel().setValue(colnum,romnum,true);
				//	alert (this.getTableModel().getValue(colnum,romnum));
				//event_data.setData(true);
			}
  		  
      }
  }
});
