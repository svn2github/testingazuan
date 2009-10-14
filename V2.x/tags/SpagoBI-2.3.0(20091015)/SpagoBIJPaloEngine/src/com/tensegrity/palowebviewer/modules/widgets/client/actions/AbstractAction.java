package com.tensegrity.palowebviewer.modules.widgets.client.actions;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Base implementation of methods that evry action must have.
 *
 */
public abstract class AbstractAction implements IAction {
	
	private boolean enabled;

	private ArrayList propertyListeners;
	
	public AbstractAction() {
		propertyListeners = new ArrayList();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void addPropertyListener(IPropertyListener propertyListener) {
		propertyListeners.add(propertyListener);
	}
	
	public void removePropertyListener(IPropertyListener propertyListener) {
		propertyListeners.remove(propertyListener);
	}

	public void setEnabled(boolean enabled) {
		if ( this.enabled == enabled )
			return;
		
		this.enabled = enabled;
		notifyPropertyChangeListeners();
	}
	
	protected void notifyPropertyChangeListeners() {
		for (Iterator it = propertyListeners.iterator(); it.hasNext(); ) {
			IPropertyListener propListener = (IPropertyListener)it.next();
			if ( isEnabled() )
				propListener.onEnabled();
			else
				propListener.onDisabled();
		}
	}

}
