qx.Class.define("qooxdoo.dao.DAOFactory",
{
	type : "static",
	
	statics : { 
		_daoMap: {
			engine: new qooxdoo.dao.SpagoBIEngineDAO()
		}
	
  		, getEngineDAO: function() {
			return this._daoMap.engine
		}
	}
});