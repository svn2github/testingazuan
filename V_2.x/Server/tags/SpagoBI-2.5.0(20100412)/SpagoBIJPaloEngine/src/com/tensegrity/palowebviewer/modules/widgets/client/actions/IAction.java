package com.tensegrity.palowebviewer.modules.widgets.client.actions;

/**
 * Some action that have states enabled/disabled and can be observed. 
 *
 */
public interface IAction {
	
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);
	
	public void onActionPerformed(Object arg);
	
	
	public void addPropertyListener(IPropertyListener propertyListener);
	public void removePropertyListener(IPropertyListener propertyListener);
	
}