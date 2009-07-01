package com.tensegrity.palowebviewer.modules.ui.client.cubetable;


public interface ITableAPIListener {

	public void onLoaded();
	
	public void onExpand(String path);
	
	public void onCollapse(String path);
	
	public void onStateChanged(int direction, String path);
	
	public void onCellUpdate(String xTree, String yTree, String newValue);
	
	public boolean canCellBeEdited(String xTree, String yTree);
	
	public boolean validate(String xTree, String yTree, String value);
	
	public boolean isSelectedElementsPlain();
	
}
