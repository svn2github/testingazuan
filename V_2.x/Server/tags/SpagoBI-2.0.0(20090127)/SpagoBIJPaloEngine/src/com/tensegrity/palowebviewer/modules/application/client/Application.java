package com.tensegrity.palowebviewer.modules.application.client;

import it.eng.spagobi.engines.jpalo.modules.listeners.client.SpagoBIServerModelListener;

import com.google.gwt.core.client.EntryPoint;
import com.tensegrity.palowebviewer.modules.engine.client.Engine;
import com.tensegrity.palowebviewer.modules.engine.client.EngineLogger;
import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.ui.client.RequestParamParser;
import com.tensegrity.palowebviewer.modules.ui.client.UIManager;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableAPIImpl;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;

/**
 * Palo AJAX Browser Application main entry point.
 * 
 * <p>
 * Creates and start Engine and UIManager
 * </p>
 * 
 */
public class Application implements EntryPoint {

    /**
     * Application entry point.
     */
    public void onModuleLoad() {
        PerformanceTimer.setTreshhold(20);
        PerformanceTimer.setSlowTreshhold(1000);
        Logger.setUseFirebug(true);
        Logger.setOn(true);

    	PerformanceTimer startupTimer = new PerformanceTimer("application startup");
    	startupTimer.start();
    	
    	
    	CubeTableAPIImpl.defineBridgeMethods();
    	IEngine engine = new Engine();
    	if(Logger.isOn()){
    		engine = new EngineLogger(engine);
    	}        	
    	
    	
    	UIManager uiManager = new UIManager(engine);    	
    	IPaloServerModel paloServerModel = engine.getPaloServerModel();
    	
    	SpagoBIServerModelListener spagoBIListener = new SpagoBIServerModelListener(paloServerModel, uiManager);
    	RequestParamParser paramParser = new RequestParamParser();
    	spagoBIListener.setServerName( paramParser.getServerName() );
    	spagoBIListener.setSchemaName( paramParser.getSchemaName() );
    	spagoBIListener.setCubeName( paramParser.getCubeName() );    	
    	paloServerModel.addListener( spagoBIListener );
    	
    	Logger.debug("ServerName: " + paramParser.getServerName());
    	Logger.debug("SchemaName: " + paramParser.getSchemaName());
    	Logger.debug("CubeName: " + paramParser.getCubeName());
    	
    	
    	uiManager.start();
    	    	
    	startupTimer.report();
    	
    	
    }
    
}
