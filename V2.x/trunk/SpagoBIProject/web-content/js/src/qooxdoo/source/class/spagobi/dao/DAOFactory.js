qx.Class.define("spagobi.dao.DAOFactory",
{
	type : "static",
	
	statics : { 
		_daoMap: {
			engine: new spagobi.dao.SpagoBIEngineDAO()
		}
	
  		, getEngineDAO: function() {
			return this._daoMap.engine
		}
	}
});