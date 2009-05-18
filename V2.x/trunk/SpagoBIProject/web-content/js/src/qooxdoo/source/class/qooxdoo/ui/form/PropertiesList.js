qx.Class.define("qooxdoo.ui.form.PropertiesList",
{
  	extend : qooxdoo.ui.table.Table,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function() {
	    
	    this.saveIcon = 'qx/icon/Oxygen/16/actions/list-add.png' ;
	    this.deleteIcon = 'qx/icon/Oxygen/16/actions/dialog-close.png' ;
	    
		this.emptyRow = {
	        	  name:''
	        	  ,value:''
	        	  ,deleteIcon: this.saveIcon
	        	 };
		
		
		var config = {
			parameters: [this.emptyRow]   
		};
		
		/*
		for(var i=0; i<config.parameters.length ; i++){
			config.parameters[i].deleteIcon = this.deleteIcon  ;
		}*/
		
		var records = {};
		
		records.meta = [
    		{
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'value',
	    		name: 'Type'
    		}, {
	    		dataIndex: 'deleteIcon',
	    		name: ''
    		}
    	];
    	records.rows =  config.parameters;

	
		var c = {
			dataset: records
		}
    	this.base(arguments,{
    		selectDataObject: function(item) {
    			// alert('selectDataObject =  ' + item.toSource());
    		}
    	},c);
    	
    	
    	var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this._cellRendererFactoryFunction);
    	var propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this._propertyCellEditorFactoryFunc);
    	
 		this._tableModel.setColumnEditable(0,true);
		this._tableModel.setColumnEditable(1,true);
		
		this.getTableColumnModel().setCellEditorFactory(0, propertyCellEditorFactory);
		this.getTableColumnModel().setCellEditorFactory(1, propertyCellEditorFactory);
    	this.getTableColumnModel().setDataCellRenderer(2, propertyCellRendererFactory);
  
		this.addListener("cellClick",this._onCellClick,this, false);    	
    	
    	this.setWidth(500);  
    	 	
  	},

  	members :
  	{
    	// public methods
  		
        setData : function (data) {
        	
       	    this._addDeleteButtonToRows(data);
    		this._tableModel.setDataAsMapArray(data , true);
        }
        
        , getData : function () {	        	
           var data = new Array();
           for( i = 0; i<this._tableModel.getRowCount(); i++){
           	  var rowData = this._tableModel.getRowDataAsMap(i);           	  
           	  data.push({name: rowData.name, value: rowData.value});
           }
           
           return data ;
        }
        
        , deleteRow : function (rowIndex) {
        	
        	alert('deleteRow '+ rowIndex + ' of ' + this._tableModel.getRowCount());
            if(this._tableModel.getRowCount()=== 1){
            	qooxdoo.commons.CoreUtils.dump('last row->reset to empty row: ' + this.emptyRow);
            	this._tableModel.setDataAsMapArray([this.emptyRow] , true);
            } else {
            	alert('not last row');
        		this._tableModel.removeRows(rowIndex,1);
        		this.updateContent();
        		
        	}
        }
        , addRow : function (rowIndex) {
        	
        	 alert('addRow '+ rowIndex + ' of ' + this._tableModel.getRowCount());
        	 var rowDataToAdd = this._tableModel.getRowDataAsMap(rowIndex);
        	
        	 var data = new Array();
             for( i = 0; i< rowIndex; i++){
           	  	var rowData = this._tableModel.getRowDataAsMap(i);           	  
           	  	data.push(rowData);
             }
             data.push({name: rowDataToAdd.name, value: rowDataToAdd.value, deleteIcon: this.deleteIcon});
             data.push(this.emptyRow);
             this._tableModel.setDataAsMapArray(data , true);
             // this.setData(data);
   
        }
        
        // private methods
        
        , _cellRendererFactoryFunction : function (cellInfo) {    		
      		return new qx.ui.table.cellrenderer.Image;
        }
        
        , _propertyCellEditorFactoryFunc: function (cellInfo) {
    		return new qx.ui.table.celleditor.TextField ;
        }
        
        , _onCellClick : function (e) {
        
        	
        	if(e.getColumn()===2){
       
        		if (e.getRow()===this._tableModel.getRowCount()-1){
        			alert('Add Row');
        			this.addRow(e.getRow());
        		}else{
        			alert('Delete Row');
        			this.deleteRow(e.getRow());
        		}
    		   //qooxdoo.commons.CoreUtils.dump(e);
    		}
        }        
        
        , _addDeleteButtonToRows : function (rows) {
        	for(var i=0; i<rows.length ; i++){
				rows[i].deleteIcon = this.deleteIcon  ;	
		    }
		    rows.push(this.emptyRow);
		    return rows ;
        }
  	}
});