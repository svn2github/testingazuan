/* ************************************************************************

   qooxdoo - the new era of web development

   http://qooxdoo.org

   Copyright:
     2004-2008 1&1 Internet AG, Germany, http://www.1und1.de

   License:
     LGPL: http://www.gnu.org/licenses/lgpl.html
     EPL: http://www.eclipse.org/org/documents/epl-v10.php
     See the LICENSE file in the project's top-level directory for details.

   Authors:
     * Fabian Jakobs (fjakobs)
     * Sebastian Werner (wpbasti)

************************************************************************ */

qx.Class.define("spagobi.test.view.Table",
{
  extend : qx.ui.table.Table,

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
    
    /*
    this._tableModel.setColumnIds([ "description", "name", "id" ]);
	this._tableModel.setColumnNamesById(
    {
      description : this.tr("Description"),
      name  : this.tr("Name"),
      id    : this.tr("ID")
    });
	*/
	
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

    //this.getTableColumnModel().setColumnVisible(2, false);

    // Add selection listener
    this.getSelectionModel().addEventListener("changeSelection", this._onChangeSelection, this);
    
    this._tableModel.setDataAsMapArray(data.rows, true);
    //this.getSelectionModel().setSelectionInterval(0, 0);
  },

  members :
  {
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
