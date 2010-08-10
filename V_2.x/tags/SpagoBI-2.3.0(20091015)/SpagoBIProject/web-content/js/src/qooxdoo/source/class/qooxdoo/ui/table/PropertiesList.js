qx.Class.define("qooxdoo.ui.table.PropertiesList",
{
  	extend : qooxdoo.ui.table.Table,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function(config) {
	    
	    this.saveIcon = 'qx/icon/Oxygen/16/actions/list-add.png' ;
	    this.deleteIcon = 'qx/icon/Oxygen/16/actions/dialog-close.png' ;
	    
		this.emptyRow = {
	        	  name:''
	        	  ,value:''
	        	  ,deleteIcon: this.saveIcon
	        	 };
		
		
		config.parameters = [this.emptyRow];
		
		
		/*
		for(var i=0; i<config.parameters.length ; i++){
			config.parameters[i].deleteIcon = this.deleteIcon  ;
		}*/
		
		var records = {};
		
		records.meta = [
    		{
	    		dataIndex: 'name',
	    		name: config.nameColumnLabel
    		}, {
	    		dataIndex: 'value',
	    		name: config.valueColumnLabel
    		}, {
	    		dataIndex: 'deleteIcon',
	    		name: ''
    		}
    	];
    	records.rows =  config.parameters;

		this.columnNameCellEditor = config.cellNameRendered ;
		this.columnValueCellEditor = config.cellValueRendered ;
		this.cellNameOptions = config.cellNameOptions ;
		this.cellValueOptions = config.cellValueOptions ;
		
		var c = {
			dataset: records
		}
    	this.base(arguments,{
    		selectDataObject: function(item) {
    			// alert('selectDataObject =  ' + item.toSource());
    		}
    	},c);

		
		
	    var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this._cellRendererFactoryFunction);
    	var propertyCellEditorFactory0 = this._createCeddEditorFactory(0);
    	var propertyCellEditorFactory1 = this._createCeddEditorFactory(1);	

    	
 		this._tableModel.setColumnEditable(0,true);
		this._tableModel.setColumnEditable(1,true);
		
		this.getTableColumnModel().setCellEditorFactory(0, propertyCellEditorFactory0);
		this.getTableColumnModel().setCellEditorFactory(1, propertyCellEditorFactory1);
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
           for( i = 0; i<this._tableModel.getRowCount()-1; i++){
           	  var rowData = this._tableModel.getRowDataAsMap(i);           	  
           	  data.push({name: rowData.name, value: rowData.value});
           }
           
           return data ;
        }
        
        , deleteRow : function (rowIndex) {
        	
            if(this._tableModel.getRowCount()=== 1){
            	this._tableModel.setDataAsMapArray([this.emptyRow] , true);
            } else {
            	
        		this._tableModel.removeRows(rowIndex,1);
        		this.updateContent();
        		
        	}
        }
        , addRow : function (rowIndex) {
        	
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
        
        , _createCellTextEditor : function (cellInfo) {   	 	
      		return new qx.ui.table.celleditor.TextField;
        }
        
         , _createCellComboEditor : function (cellInfo) {   	 	
      		var cellEditor = new qx.ui.table.celleditor.ComboBox() ;
      		
      		if(cellInfo.col === 0){
				var comboOptions = cellInfo.table.cellNameOptions ;
			    if(comboOptions){
			      	cellEditor.setListData(comboOptions);
			    }
			} else if (cellInfo.col === 1){
				var comboOptions = cellInfo.table.cellValueOptions ;
			    if(comboOptions){
			      	cellEditor.setListData(comboOptions);
			    }
			} 
			return cellEditor;
        }
        
        , _createCeddEditorFactory : function (column) {   	
        
        	var propertyCellEditorFactory;	
        	 	
      		if(column === 0){
           		
           			if(this.columnNameCellEditor === 'combo'){
         				propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this._createCellComboEditor);		
           			}else{
           				propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this._createCellTextEditor);	
           			}
	      		
		      	} else if (column === 1){
		      	
		      		if(this.columnValueCellEditor === 'combo'){
			      		propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this._createCellComboEditor);	
			      		
           			}else{
           				propertyCellEditorFactory = new qx.ui.table.celleditor.Dynamic(this._createCellTextEditor);	
           			}
		      	}
		      	return propertyCellEditorFactory;
        }
        , _onCellClick : function (e) {
        
        	
        	if(e.getColumn()===2){
       
        		if (e.getRow()===this._tableModel.getRowCount()-1){
        		
        			this.addRow(e.getRow());
        		}else{
        			
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