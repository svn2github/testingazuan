
qx.Class.define("spagobi.ui.PagedTable", {
  
	extend : qx.ui.layout.VerticalBoxLayout,

	construct : function(controller, data)  {
    	    	
    	this.base(arguments);    	
    	
    	this._filterBar = new spagobi.ui.FilterBar();
    	this.add(this._filterBar);
    	
    	this._table = new spagobi.ui.Table(controller, data);
    	this.add(this._table);
    	
    	this._navigationBar = new spagobi.ui.NavigationBar();
    	this.add(this._navigationBar);
  	},

	members : {
    	_filterBar: undefined,
    	_navigationBar: undefined,
    	_table: undefined    
    }
});
