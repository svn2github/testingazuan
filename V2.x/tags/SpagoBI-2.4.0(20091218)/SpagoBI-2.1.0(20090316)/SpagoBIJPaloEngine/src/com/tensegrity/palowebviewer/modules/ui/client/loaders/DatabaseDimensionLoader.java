package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class DatabaseDimensionLoader extends AbstractLoader {

	private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener () {

		public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			if(database == path[path.length -1] && type == IXConsts.TYPE_DIMENSION) {
				unsubscribe();
				executeCallback();
			}
		}
		
	};
	private XDatabase database;
	private IPaloServerModel serverModel;
	
	public DatabaseDimensionLoader(XDatabase database) {
		this.database = database;
	}
	
	public XDatabase getDatabase() {
		return database;
	}
	
	
	public void load() {
		 XDatabase database = getDatabase();
		if(database.getDimensions() == null) {
			subscribe();
			getServerModel().loadChildren(database, IXConsts.TYPE_DIMENSION);
		}
		else {
			executeCallback();
		}
	}

	protected void unsubscribe() {
		getServerModel().removeListener(serverModelListener);
		
	}

	private void subscribe() {
		getServerModel().addListener(serverModelListener);
	}

	public IPaloServerModel getServerModel() {
		return serverModel;
	}

	public void setServerModel(IPaloServerModel serverModel) {
		this.serverModel = serverModel;
	}

	public String getDescription() {
		return "DatabaseDimensionLoader";
	}


}
