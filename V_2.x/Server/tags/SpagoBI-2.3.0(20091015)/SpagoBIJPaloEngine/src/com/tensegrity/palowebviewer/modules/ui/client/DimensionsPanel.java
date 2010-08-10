package com.tensegrity.palowebviewer.modules.ui.client;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.tensegrity.palowebviewer.modules.engine.client.IClientProperties;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.IDimensionList;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;
import com.tensegrity.palowebviewer.modules.widgets.client.IWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.dnd.DnDManager;
import com.tensegrity.palowebviewer.modules.widgets.client.dnd.IDropTarget;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.TreeCombobox;


/**
 * Horizontal panel of {@link DimensionWidget} widgets.
 *
 */
public class DimensionsPanel extends HorizontalPanel implements IDropTarget
{

    private boolean dimensionSelectorVisible = true;
    private final HTML rest = new HTML("&nbsp;");
    private final IDimensionList dimList;
	private Widget dragged;
	private int position;
    

    public void setDimensionSelectorVisible(boolean value) {
        this.dimensionSelectorVisible = value;
        WidgetCollection widgets = this.getChildren();
        for (Iterator it = widgets.iterator(); it.hasNext();) {
        	Object obj = it.next();
        	if(obj instanceof DimensionWidget){
        		DimensionWidget widget = (DimensionWidget)obj;
        		widget.setDimensionSelectorVisible(dimensionSelectorVisible);
        	}
			
		}
    }

    public boolean canAcceptDrop(Widget widget, int x, int y) {
        return (widget instanceof DimensionWidget);
    }

    public void acceptDrop(Widget widget, int x, int y) {
        DimensionWidget dimWidget = (DimensionWidget)widget;
        dimWidget.setDimensionSelectorVisible(dimensionSelectorVisible);
        dimWidget.setVertical(false);
        x = getAbsoluteLeft()+x;
        
		int position = findDropPosition(x);
		insertWidget(widget, position);
        getDimensionList().insertDimension(position, dimWidget.getModel());
    }

	private void insertWidget(Widget widget, int position) {
		add(widget);
        moveLastWidget(position);
	}
    

	public void cancelDrop(Widget widget) {
		if(dragged == widget) {
			insertWidget(dragged, position);
		}
	}

	private void moveLastWidget(int position) {
		int size = getWidgetCount()-position-1;
        for (int i = 0; i < size; i++) {
			Widget w = (Widget)getWidget(position);
			remove(w);
			add(w);
		}
        remove(this.rest);
        add(this.rest);
        setCellHeight(rest, "100%");
    	setCellWidth(rest, "100%");
	}

	private int findDropPosition(int x) {
		int size = getWidgetCount();
        int position = size-1;
        for (int i = position; i >= 0; i--) {
        	Widget w = getWidget(i);
        	int left = w.getAbsoluteLeft();
        	if(left > x)
        		position = i;
        	else {
        		break;
        	}
		}
		return position;
	}
    
    public DimensionsPanel (UIManager uiManager, IDimensionList dimList, DnDManager dndManager, IWidgetFactory widgetFactory) {
        this.dimList = dimList;
        IPaloServerModel serverModel = uiManager.getEngine().getPaloServerModel();
    	dndManager.addTarget(this);
    	int size = dimList.getDimCount();
    	for (int i = 0; i < size; i++) {
    		IDimensionModel dimModel = dimList.getDimModel(i);
    		DimensionWidget dimWidget = new DimensionWidget(dimModel, widgetFactory);
    		TreeCombobox dimensionSelector = dimWidget.getDimensionSelector();
    		IClientProperties clientProperties = uiManager.getClientProperties();
    		if(clientProperties.hasPOVLoadSelectedPath())
    			new PaloTreeExpander(serverModel, dimensionSelector);
			new PaloTreeLevelExpander(dimensionSelector, clientProperties.getPOVShowLevels());
      	  	dndManager.addDragObject(dimWidget);
      	  	add(dimWidget);
		}

    	add(rest);
    	setCellHeight(rest, "100%");
    	setCellWidth(rest, "100%");
    	JavaScript.disableSelection(this);
    	JavaScript.disableSelection(rest);

    }
    
    public void removeDragObject(Widget widget) {
    	int index = getWidgetIndex(widget);
    	if(index >= 0){
    		remove(widget);
    		this.dragged = widget;
    		position = index; 
    	}
    }
    
    protected IDimensionList getDimensionList() {
        return dimList;
    }
}
