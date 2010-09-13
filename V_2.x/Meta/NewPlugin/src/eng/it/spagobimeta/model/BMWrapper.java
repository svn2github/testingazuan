/*
 * Wrapper class to show root node (BusinessModel) in TreeViewer and prevent
 * bug https://bugs.eclipse.org/9262
 * 
 * Singleton class
 */
package eng.it.spagobimeta.model;

import java.util.ArrayList;
import java.util.Collection;

import eng.it.spagobimeta.listeners.DeltaEvent;
import eng.it.spagobimeta.listeners.IDeltaListener;
import eng.it.spagobimeta.listeners.NullDeltaListener;

public class BMWrapper {
	
	//only one instance of this class exist globally
	static private BMWrapper _instance;
	
	protected IDeltaListener listener = NullDeltaListener.getSoleInstance();

	private Collection<BusinessModel> bm;
	
	private BMWrapper(){
		bm = new ArrayList<BusinessModel>();
	}
	
	public static BMWrapper getInstance() {
		if (_instance == null) 
			synchronized(BMWrapper.class){
				_instance = new BMWrapper();
			}
		return _instance;
	}
	
	public void init(BusinessModel biz){
		bm = new ArrayList<BusinessModel>();
		bm.add(biz);
	}


	public void setBm(Collection<BusinessModel> bm) {
		this.bm = bm;
		fireAdd(this);
	}

	synchronized public Collection<BusinessModel> getBm() {
		return bm;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	

	//Listeners methods
	protected void fireAdd(Object added) {
		listener.add(new DeltaEvent(added));
	}

	protected void fireRemove(Object removed) {
		listener.remove(new DeltaEvent(removed));
	}
	
	public void addListener(IDeltaListener listener) {
		this.listener = listener;
	}
	public void removeListener(IDeltaListener listener) {
		if(this.listener.equals(listener)) {
			this.listener = NullDeltaListener.getSoleInstance();
		}
	}	

}
