package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;


/**
 * ITreeViewItem describes an item in {@link TreeView}.
 *
 */
public interface ITreeViewItem
{

    public void setClickAction(IAction action);

    public TreeItem addItem(Widget widget);

    public void addItem(TreeItem item);
    
    public void insertItem(ITreeViewItem item, int index);
    
    public void addItem(ITreeViewItem itme);
    
    public TreeItem getChild(int i);
    
    public ITreeViewItem getChildItem(int i);
    
    public int getChildCount();
    
    public void remove();
    
    public void setWidget(Widget widget);

    public Object getNode();
    
    public boolean isChildrenInited();

    public void reinit();
    
    public void stateChanged();
    
    public void setState(boolean state);


}
