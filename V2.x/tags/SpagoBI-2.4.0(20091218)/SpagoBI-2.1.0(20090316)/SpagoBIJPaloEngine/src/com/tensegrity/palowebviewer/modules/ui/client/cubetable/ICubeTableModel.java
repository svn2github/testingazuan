package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.paloclient.client.IElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.dispose.IDisposable;

public interface ICubeTableModel extends IDisposable{

	public boolean canCellBeEdited(HeaderTreeNode xNode, HeaderTreeNode yNode);

	public IDimensionList getSliceDimensions();

	public XView getView();
	
	public IPaloServerModel getPaloServerModel();

	public XQueryConstructor getQueryConstructor();

	public IDimensionList getXDimensions();

	public IDimensionList getYDimensions();

	public CubeHeaderModel getXHeaderModel();
	
	public void setShowLevels(int value);

	public CubeHeaderModel getYHeaderModel();

	public IElementType getCellType(HeaderTreeNode xNode, HeaderTreeNode  yNode);  
	
	public void updateCell(HeaderTreeNode xNode, HeaderTreeNode yNode, IResultElement value);
	
	public XCube getCube();

	public XView createView();

	public XView rebuildView();

	public void reloadData();

	public void reloadData(HeaderTreeNode node, HeaderTreeNode node2);

	public boolean isSelectedElementsPlain();

	public void addListener(ICubeTableModelListener listener);

	public void removeListener(ICubeTableModelListener listener);

	public void addListener(IDataListener listener);

	public void removeListener(IDataListener listener);

	public boolean isLoadingData();

	public boolean isModelValid();
	
	public boolean isLoaded();
	
	public boolean isExpanded();

	public String getInvalidReason();
	
	public void updateFinished();
	
	public void setCellValue(int x, int y, IResultElement value);
	
	public boolean isDisposed();
	
	public void setAllowDataLoad(boolean value);
	
	public boolean isAllowDataLoad();

	public boolean isCellConsolidated(HeaderTreeNode node, HeaderTreeNode node2);

}