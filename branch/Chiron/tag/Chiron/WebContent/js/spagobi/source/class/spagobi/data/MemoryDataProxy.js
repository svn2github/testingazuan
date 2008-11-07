qx.Class.define("spagobi.data.MemoryDataProxy",
{
  	extend : qx.core.Object,

	properties : {
  		data : { init : [], check: "Array" }
	},

	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/
	construct : function(data)
	{
		this.base(arguments);
		
	    this.initData(data);
	},

  	members :
  	{
    	load: function() {
    		return this.getData();
    	}
  	}
});