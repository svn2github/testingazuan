package com.tensegrity.palowebviewer.server.paloaccessor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.palo.api.CubeView;
import org.palo.api.persistence.PersistenceError;
import org.palo.api.persistence.PersistenceObserverAdapter;

public class CubeLoaderPersistanceObserver extends PersistenceObserverAdapter {
	
	private static final Logger log = Logger.getLogger(CubeLoaderPersistanceObserver.class);
	
	private final List views = new ArrayList();
	
	public CubeLoaderPersistanceObserver(){
	}
	
	public void loadComplete(Object obj) {
		CubeView view = (CubeView) obj;
		log.debug("loaded view: " + view.getName());
		views.add(view);
	}
	
	public List getViews(){
		return views;
	}
	
	public int getViewCount(){
		return views.size();
	}

	public void loadFailed(String id, PersistenceError[] errs) {
		log.error("loading failed for : " +id);
		for (int i = 0; i < errs.length; i++) {
			log.error("cause : " + errs[i].getCause());
		}
	}

	public void loadIncomplete(Object id, PersistenceError[] errs) {
		log.warn("loading incomplete for : " +id);
		for (int i = 0; i < errs.length; i++) {
			log.error("cause : " + errs[i].getCause());
		}
		if (id instanceof CubeView) {
			CubeView view = (CubeView) id;
			log.warn("adding incomplete view  : " + view.getName());
			views.add(view);
		}
	}

}
