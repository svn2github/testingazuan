qx.Class.define("qooxdoo.ui.form.PropertiesList",
{
  	extend : qooxdoo.ui.table.Table,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/

	construct : function() {
	    
	    this.deleteIcon = 'qx/icon/Oxygen/16/actions/dialog-close.png' ;
		var config = {
			parameters: [
	        	 {
	        	  name:''
	        	  ,value:''
	        	 }
	        	]   
		};
		
		for(var i=0; i<config.parameters.length ; i++){
			config.parameters[i].delete = this.deleteIcon  ;
		}
		
		var records = {};
		
		records.meta = [
    		{
	    		dataIndex: 'name',
	    		name: 'Name'
    		}, {
	    		dataIndex: 'value',
	    		name: 'Type'
    		}, {
	    		dataIndex: 'delete',
	    		name: ''
    		}
    	];
    	records.rows =  config.parameters;

	
		var c = {
			dataset: records
		}
    	this.base(arguments,this,c,true);
    	
    	
    	var propertyCellRendererFactory = new qx.ui.table.cellrenderer.Dynamic(this.cellRendererFactoryFunctionX);
 		for(i=0; i<records.length; i++){
			columnModel.setDataCellRenderer(records[i], propertyCellRendererFactory);
		}
		//this.addListener("cellClick",this._onCellClick,this, false);
    	
    	
    	
    	this.setWidth(800);
    	
  	},

  	members :
  	{
    	cellRendererFactoryFunctionX : function (cellInfo) {
    	alert('pippo');
    		qooxdoo.commons.CoreUtils.dump(cellInfo);
      		//return new qx.ui.table.cellrenderer.Default;
        },
        setValue : function (v) {
    		alert('Ciao');
        }
  	}
});