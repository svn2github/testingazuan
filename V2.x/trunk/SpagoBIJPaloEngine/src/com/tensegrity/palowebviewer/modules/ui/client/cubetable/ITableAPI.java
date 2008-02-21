package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;

public interface ITableAPI {
	public static final int DIRECTION_HORIZONTAL = 0;
	public static final int DIRECTION_VERTICAL   = 1;
	public static final String MIN_WIDTH = "minWidth";
	public static final String MAX_WIDTH = "maxWidth";
	public static final String HINT_TIME = "hintTime";

	public void initWidget();

	public void clean();

	public void changeZstate(boolean value);
	
	/**
	 * Inserts tree model into Table at default position 0.
	 * @param direction - DIRECTION_HORIZONTAL/DIRECTION_VERTICAL
	 * @param tree - model of the tree to insert.
	 */
	public void insertTree(int direction, ITreeModel tree);

	/**
	 * Inserts tree model into Table at the position <code>pos</code>.
	 * @param direction - DIRECTION_HORIZONTAL/DIRECTION_VERTICAL
	 * @param pos - position of the tree, zero based
	 * @param tree - model of the tree to insert.
	 */
	public void insertTree(int direction, int pos, ITreeModel tree);

	/**
	 * Command the table to expand node.
	 * Path is encoded like this "node01/node02//node11/node12...",
	 * where node01,node02 belongs to the first tree, node11,node12 belongs to the second.
	 * @param direction - DIRECTION_HORIZONTAL/DIRECTION_VERTICAL
	 * @param treePath - path of the node
	 */
	public void expandTree(int direction, String treePath);
	
	/**
	 * Adds children to the tree model in the table
	 * @param direction - DIRECTION_HORIZONTAL/DIRECTION_VERTICAL
	 * @param parent - parent to add children nodes
	 * @param tree - model of the tree.
	 */
	public void insertChildren(int direction, Object parent, ITreeModel tree);

	/**
	 * Sets cell's value. 
	 * @param row - row , zero based
	 * @param column - column, zero based
	 * @param value - value to set.
	 */
	public void setCellValue(int row, int column, String value);

	/**
	 * Tells table to redraw new values
	 */
	public void updateData();

	public void addListener(ITableAPIListener listener);

	public void removeListener(ITableAPIListener listener);
	
	public Widget getWidget();
	
	public void setParameter(String key, String value);

}