package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;


public interface IDimensionList
{

    public List getDimensionsXPaths();

    public IDimensionModel getDimModel(int i);

    public ITreeModel getDimensionTree(int i);
    
    public XDimension[] getDimensions();
    
    public int getDimCount();

    public Iterator iterator();
    
    public XAxis createAxis();

    public void insertDimension(int i, IDimensionModel dim);

    public void addListener(IDimensionListListener listener);
	
	public void removeListener(IDimensionListListener listener);
	
	public void setShowLevels(int value);
	
	public int getShowLevels();

}
