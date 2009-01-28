/**

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

**/
package it.eng.spagobi.engines.jpalo.modules.listeners.client;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.ui.client.RequestParamParser;
import com.tensegrity.palowebviewer.modules.ui.client.UIManager;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIServerModelListener extends AbstractServerModelListener {
	
	
	private IPaloServerModel paloServerModel;
	private UIManager uiManager;
	private boolean analysisLoaded;
	
	private String serverName = "Mondrian XMLA";
	private String schemaName = "FoodMart";
	private String cubeName = "HR";
	
	public SpagoBIServerModelListener(IPaloServerModel paloServerModel, UIManager uiManager) {
		setPaloServerModel(paloServerModel);
		setUiManager(uiManager);
		setAnalysisLoaded(false);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onError(Throwable cause) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onObjectLoaded(XObject[] path, XObject object) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onUpdateComplete() {
		if( isAnalysisLoaded() ) return;
			
	
		Logger.debug("Last loading task has been completed");
        
        XObject targetCube = null;
        XRoot root = paloServerModel.getRoot();
    	XServer[] servers = root.getServers();
    	Logger.debug("Start scanning on servers list looking for server named " + serverName);
    	for(int k = 0; k < servers.length; k++) {
    		if(!servers[k].getDispName().equals( serverName )) continue;
    		
    		Logger.debug("Target server " + serverName + " has been found at position " + k);
        	
    		XDatabase[] databases =  servers[k].getDatabases();
        	if(databases == null) {      
        		Logger.debug("Start loading schemas on server " + servers[k].getDispName());
        		paloServerModel.loadChildren( servers[k],IXConsts.TYPE_DATABASE );
        		continue;
        	}
        	
        	Logger.debug("Start scanning on databases list looking for schema named " + schemaName);
        	for(int i = 0; i < databases.length; i++) {
        		if(!databases[i].getName().equals( schemaName )) continue;
        		
        		Logger.debug("Target schema " + schemaName + " has been found at position " + i);
        		
        		XCube[] cubes = databases[i].getCubes();
        		if(cubes == null) {
        			Logger.debug("Start loading cubes on schema " + databases[i].getName());
	        		paloServerModel.loadChildren( databases[i], IXConsts.TYPE_CUBE );
	        		continue;	   
        		}
        		
        		Logger.debug("Start scanning on cubes list looking for cube named " + cubeName);
        		for(int j = 0; j < cubes.length; j++) {
        			if(!cubes[j].getName().equalsIgnoreCase( cubeName )) continue;
        			Logger.debug("Target cube " + cubeName + " has been found at position " + j);
        			targetCube = cubes[j];	        			
        		}
        	}
    	}
    	
    	
    	if(targetCube != null) {
    		Logger.debug("Open new analysis on cube " + targetCube.getName());
    		uiManager.openEditor(targetCube);
    		setAnalysisLoaded(true);
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public void defaultViewLoaded(XCube cube) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void objectRenamed(XObject object) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void onFavoriteViewsLoaded() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void objectInvalide(XPath path) {
	}


	protected IPaloServerModel getPaloServerModel() {
		return paloServerModel;
	}


	protected void setPaloServerModel(IPaloServerModel paloServerModel) {
		this.paloServerModel = paloServerModel;
	}


	protected UIManager getUiManager() {
		return uiManager;
	}


	protected void setUiManager(UIManager uiManager) {
		this.uiManager = uiManager;
	}


	protected boolean isAnalysisLoaded() {
		return analysisLoaded;
	}


	protected void setAnalysisLoaded(boolean analysisLoaded) {
		this.analysisLoaded = analysisLoaded;
	}


	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}


	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
}
