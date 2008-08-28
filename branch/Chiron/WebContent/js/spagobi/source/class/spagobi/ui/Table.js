
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
    
    var columnIds = [];
    var columnNames = {};
    for(var i = 0; i < data.meta.length; i++) {
    	columnIds[i] =  data.meta[i].dataIndex;
    	columnNames[data.meta[i].dataIndex] = data.meta[i].name;
    }

    // Create table model
    this._tableModel = new qx.ui.table.model.Simple();
    
    this._tableModel.setColumnIds( columnIds );
	this._tableModel.setColumnNamesById( columnNames );

	
    // Customize the table column model. We want one that
    // automatically resizes columns.
    this.base(arguments, this._tableModel,
    {
      tableColumnModel : function(obj) {
        return new qx.ui.table.columnmodel.Resize(obj);
      }
    });

    // Basic setup
    this.setDimension("100%", "100%");
    this.setBorder("line-bottom");
    this.setStatusBarVisible(false);
    this.getDataRowRenderer().setHighlightFocusRow(false);
    this.getPaneScroller(0).setShowCellFocusIndicator(false);

	
    // Configure columns
    var columnModel = this.getTableColumnModel();
    var resizeBehavior = columnModel.getBehavior();
	
	
    //resizeBehavior.setWidth(0, "3*");
    //resizeBehavior.setWidth(1, "1*");

  
    // Add selection listener
    this.getSelectionModel().addEventListener("changeSelection", this._onChangeSelection, this);
    
    this._tableModel.setDataAsMapArray(data.rows, true);
    
  },

  /**
   * members of the class
   */	
  members :
  {
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
    		columnIds[i] =  data.meta[i].dataIndex;
    		columnNames[data.meta[i].dataIndex] = data.meta[i].name;
    	}
    	
    	this._tableModel.setColumnIds( columnIds );
	    this._tableModel.setColumnNamesById( columnNames );
	    this.getTableModel().setDataAsMapArray(data.rows, true);
        this.getSelectionModel().setSelectionInterval(0, 0);
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
            
    }
  }
});
