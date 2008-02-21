package com.tensegrity.palowebviewer.modules.widgets.client.treecombobox;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;
import com.tensegrity.palowebviewer.modules.widgets.client.DefaultLableFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.IActionFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.ILabelFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.IWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.TreeView;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.AbstractAction;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IComboboxListener;


/**
 * Combobox widget that have tree instead of plain object list.
 *
 */
public class TreeCombobox extends Composite implements SourcesMouseEvents
{

    private ITreeComboboxModel model;
    private Panel panel;
    private Label selectedLabel;
    private Label openTreeButton;
    private PopupPanel popup;
    private ScrollPanel scroll; 
    private TreeView treeView;
    private final MouseListenerCollection mouseListeners = new MouseListenerCollection();
    private boolean treeOpened = false;
    private IWidgetFactory widgetFactory;
    private int popupXShift = -13;
    private ComboboxViewListeners listeners = new ComboboxViewListeners();
	private final ILabelFactory labelFactory;
	private int maxLength = 1000;
	private PopupListener popupListener = new PopupListener() {

		public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
			setTreeOpened(false);
		}
		
	};
	private int selectedFieldWidth = 100;
	
	
    public void addListener(ICoboboxViewListener listener) {
    	listeners.addListener(listener);
    }
    public void removeListener(ICoboboxViewListener listener) {
    	listeners.removeListener(listener);
    }
    
    public void setPoupuXShift(int value) {
    	this.popupXShift = value;
    }
    
    public void setSelectedFieldWidth(int width) {
    	selectedFieldWidth = width;
    	redrawSelectedItem();
    }

    private final IComboboxListener comboboxListener = new IComboboxListener() {

        public void onSelectionChanged(Object oldItem) {
            redrawSelectedItem();
        }

		public void onInvalidItemSet(Object item) {
		}

    };

    private final IAction clickNodeAction = new AbstractAction() {

        public void onActionPerformed(Object arg) {
            getModel().setSelectedItem(arg);
            hideTree();
        }

    };

    protected final IActionFactory treeActionFactory = new IActionFactory() {

        public IAction getActionFor(Object object) {
            return clickNodeAction;
        }

    };

    private final ClickListener openListener = new ClickListener() {

        public void onClick(Widget source) {
            toggleTree();
        }

    };
    
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		if(this.maxLength != maxLength) {
			this.maxLength = maxLength;
			redrawSelectedItem();
		}
	}

    public TreeView getTreeView() {
    	return treeView;
    }
    
    public boolean isTreeOpened() {
    	return treeOpened;
    }
    
    protected void setTreeOpened(boolean value){
    	treeOpened = value;
    }
    
    public void toggleTree() {
    	if(isTreeOpened())
    		hideTree();
    	else
    		openTree();
    }
    
    public void openTree() {
    	int left= this.getAbsoluteLeft()+ popupXShift;
    	int top = this.getAbsoluteTop();
    	top += this.getOffsetHeight();
        getPopup().setPopupPosition(left, top);
        popup.setWidth("216px");
        //popup.setHeight();
        scroll.setHeight(this.getOffsetHeight()*15+"px");
        getPopup().show();
        setTreeOpened(true);
        listeners.fireOnDropdownOpen();
    }
    
    public void hideTree() {
    	getPopup().hide();
    }

    public void setModel(ITreeComboboxModel model) {
        if(model == null)
            throw new IllegalArgumentException("Combobox model can not be null");
        if(this.model != null)
            this.model.removeComboboxListener(comboboxListener);
        this.model = model;
        this.model.addComboboxListener(comboboxListener);
        initCombobox();
    }

    public void setWidgetFactory(IWidgetFactory factory) {
    	if(factory == null)
    		throw new IllegalArgumentException("Facory can not be null.");
    	this.widgetFactory = factory;
    	if(treeView != null)
    		treeView.setWidgetFactory(getWidgetFactory());
    }
    
    public IWidgetFactory getWidgetFactory () {
    	return widgetFactory;
    }

    public ITreeComboboxModel getModel() {
        return this.model;
    }
    
    public TreeCombobox (ITreeComboboxModel model, IWidgetFactory factory) {
    	this(model, factory, new DefaultLableFactory());
    	
    }

    public TreeCombobox (ITreeComboboxModel model, IWidgetFactory factory, ILabelFactory labelFactory) {
    	this.labelFactory = labelFactory;
        panel = new BasePanel();
        initWidget(panel);
        selectedLabel = new Label();
        selectedLabel.setStyleName("label");
        openTreeButton = new Label("");
        openTreeButton.setStyleName("open-tree-button");
        setWidgetFactory(factory);
        openTreeButton.addClickListener(openListener);
        panel.add(selectedLabel);
        panel.add(openTreeButton);
        panel.setWidth("100%");
        setModel(model);
        JavaScript.disableSelection(selectedLabel);
        JavaScript.disableSelection(panel);
        JavaScript.disableSelection(openTreeButton);
        setStyleName("tensegrity-gwt-treecombobox");
        sinkEvents(Event.MOUSEEVENTS);
    }
    
    protected PopupPanel getPopup() {
    	if(popup == null){
            createPopupPanel();
    	}
    	return popup;
    }

	private void createPopupPanel() {
		treeView = new TreeView();
		treeView.setWidgetFactory(getWidgetFactory());
		treeView.setActionFactory(treeActionFactory);
		treeView.setTreeModel(model.getTreeModel());
		scroll = new ScrollPanel(treeView);
		scroll.setHeight("100%");
		scroll.setStyleName("scroll");
		Grid grid = new Grid(1,1);
		grid.setStyleName("grid_style");
		grid.setBorderWidth(0);
		grid.setWidget(0, 0, scroll);
		grid.setWidth("100%");
		popup = new PopupPanel(true);
		popup.add(grid);
		popup.addPopupListener(popupListener);
		popup.setStyleName("popup");
	}
	
	protected void redrawSelectedItem() {
		Object selected = getModel().getSelectedItem();
        String text = "";
        if(selected != null)
            text = labelFactory.getLabel(selected);
        text = cutText(text);
        selectedLabel.setText(text);
	}
    
    protected String cutText(String text) {
    	String result = text;
    	result = JavaScript.cutToFit(text, selectedFieldWidth);
		return result;
	}
	public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
        case Event.ONMOUSEMOVE:
        case Event.ONMOUSEUP:
        	DOM.eventCancelBubble(event, true);
          mouseListeners.fireMouseEvent(this, event);
          break;
        }
    }

    public void addMouseListener(MouseListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null");
        mouseListeners.add(listener);
    }

    public void removeMouseListener(MouseListener listener)  {
        mouseListeners.remove(listener);
    }

    protected void initCombobox() {
        comboboxListener.onSelectionChanged(null);
    }

    private final class BasePanel extends HorizontalPanel {
    	public void onBrowserEvent(Event event) {
    		if(openTreeButton.getElement().equals(DOM.eventGetTarget(event))){
    			DOM.eventCancelBubble(event, true);
    		} 
    		else
				filterMouseEvents(event);
    	}

		private void filterMouseEvents(Event event) {
			switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEDOWN:
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEUP:
				DOM.eventCancelBubble(event, true);
				mouseListeners.fireMouseEvent(TreeCombobox.this, event);
				break;
			}
		}
    }

}
