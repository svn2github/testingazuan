package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;
import com.tensegrity.palowebviewer.modules.widgets.client.IWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IComboboxListener;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.ITreeComboboxModel;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.TreeCombobox;


/**
 * Widget that displayes {@link IDimensionModel}. It have several states:vertical/horisontal, short/full.
 *
 */
public class DimensionWidget extends Composite implements SourcesMouseEvents
{
	public static String DIMENSION_WIDGET_STYLE = "dimension-widget";
	public static String VERTICAL_STYLE = "vertical";
	public static String HORIZONTAL_STYLE = "horizontal";
    public static String SUBSET_SELECTED_STYLE = "subset-selected";
    public static String SUBSET_UNSELECTED_STYLE = "subset-unselected";
    public static String SUBSET_BUTTON_STYLE = "subset-button";
    public static String DIM_ICON_STYLE = "dim-icon";
    public static String BALL_ICON_STYLE = "ball-icon";
    
    private static final String DIMENSION_FULL = "dimension-full";
    private static final String DIMENSION_SHORT = "dimension-short";
    
    private static final int DIM_NAME_MAX_WIDTH = 72;


    private boolean vertical = false;
    private FocusPanel basePanel;
    private Panel panel;
    private HTML horizontalLabel;
    private HTML verticalLabel;
    private Label vSubsetButton;
    private Label hSubsetButton;
    private Label dimIcon;
    private TreeCombobox dimensionSelector;
    private SubsetSelectionPopup popup;
    private HorizontalPanel selectorPanel; 
    private boolean subsetListOpen = false;
    private final IWidgetFactory widgetFactory;
    private final IDimensionModel model;
    private final MouseListenerCollection mouseListeners = new MouseListenerCollection();
    private final ClickListener subsetButtonClickListener = new ClickListener() {

        public void onClick(Widget sender) {
            toggleSubsetList();
        }

    };
    private final PopupListener subsetPopupListener = new PopupListener() {

		public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
			subsetListOpen = false;
		}
    	
    };

    private final IComboboxListener subsetComboboxListener = new IComboboxListener() {

        public void onSelectionChanged(Object oldItem) {
            hideSubsetList();
            Object subset = getModel().getSubsetListModel().getSelectedItem();
            if(subset != null) {
                hSubsetButton.removeStyleName(SUBSET_UNSELECTED_STYLE);
                vSubsetButton.removeStyleName(SUBSET_UNSELECTED_STYLE);
                hSubsetButton.addStyleName(SUBSET_SELECTED_STYLE);
                vSubsetButton.addStyleName(SUBSET_SELECTED_STYLE);
            }
            else {
                hSubsetButton.removeStyleName(SUBSET_SELECTED_STYLE);
                vSubsetButton.removeStyleName(SUBSET_SELECTED_STYLE);
                hSubsetButton.addStyleName(SUBSET_UNSELECTED_STYLE);
                vSubsetButton.addStyleName(SUBSET_UNSELECTED_STYLE);
            }
        }

		public void onInvalidItemSet(Object item) {
		}

    };
    private final IComboboxListener selectedElementListener = new IComboboxListener() {

		public void onSelectionChanged(Object oldItem) {
        	
			resetTooltip();
		}

		public void onInvalidItemSet(Object item) {
		}
    	
    };

    private final MouseListener mouseListener = new MouseListener() {

        private int translateX(Widget child, int x) {
            int result = child.getAbsoluteLeft();
            result -= DimensionWidget.this.getAbsoluteLeft();
            result += x;
            return result;
        }

        private int translateY(Widget child, int y) {
            int result = child.getAbsoluteTop();
            result -= DimensionWidget.this.getAbsoluteTop();
            result += y;
            return result;
        }

        public void onMouseDown(Widget sender, int x, int y) {
            x = translateX(sender, x);
            y = translateY(sender, y);
            mouseListeners.fireMouseDown(DimensionWidget.this, x, y);
        }

        public void onMouseEnter(Widget sender) {
            mouseListeners.fireMouseEnter(DimensionWidget.this);
        }

        public void onMouseLeave(Widget sender) {
            mouseListeners.fireMouseLeave(DimensionWidget.this);
        }

        public void onMouseMove(Widget sender, int x, int y) {
            x = translateX(sender, x);
            y = translateY(sender, y);
            mouseListeners.fireMouseMove(DimensionWidget.this, x, y);
        }

        public void onMouseUp(Widget sender, int x, int y)  {
            x = translateX(sender, x);
            y = translateY(sender, y);
            mouseListeners.fireMouseUp(DimensionWidget.this, x, y);
        }

    };
    
    protected void resetTooltip() {
    	String tooltip = getDimensionName();
		if(isDimensionSelectorVisible()){
			XElement element = model.getSelectedElement();
			String name ="";
			if(element != null)
				name = element.getName();
			tooltip += "(Element='"+name+"')";
		}
    	JavaScript.setTooltip(basePanel, tooltip);
    	JavaScript.setTooltip(dimensionSelector, tooltip);
    	JavaScript.setTooltip(verticalLabel, tooltip);
    	JavaScript.setTooltip(horizontalLabel, tooltip);
    }
    
    protected XDimension getDimension() {
    	return getModel().getDimension();
    }

    public void setDimensionSelectorVisible(boolean value) {
            selectorPanel.setVisible(value);
            dimIcon.setVisible(value);
            if(value){
                basePanel.setStyleName(DIMENSION_FULL);
                getDimensionName();
                
                //It was done to fix bug in IE, it cut the tail of letters g,y etc.
                if(dimensionSelector.getParent()== null)
                	panel.add(dimensionSelector);
            }
            else {
                removeStyleName(DIMENSION_FULL);
                basePanel.setStyleName(DIMENSION_SHORT);
                //It was done to fix bug in IE, it cut the tail of letters g,y etc.                
                if(dimensionSelector.getParent()!= null)
                	panel.remove(dimensionSelector);
            }
            resetTooltip();
    }

    public boolean isDimensionSelectorVisible() {
        return selectorPanel.isVisible();
    }

    public boolean isVertical() {
        return vertical;
    }
    
    public String getDimensionName() {
    	return getModel().getDimension().getName();
    }

    public void setVertical(boolean value) {
        vertical = value;
        if(vertical)
            makeVertical();
        else
            makeHorizontal();
    }

	private void makeHorizontal() {
		verticalLabel.setVisible(false);
		vSubsetButton.setVisible(false);

		hSubsetButton.setVisible(true);
		horizontalLabel.setVisible(true);

		panel.setStyleName(HORIZONTAL_STYLE);
	}

	private void makeVertical() {
		verticalLabel.setVisible(true);
		vSubsetButton.setVisible(true);

		hSubsetButton.setVisible(false);
		horizontalLabel.setVisible(false);

		panel.setStyleName(VERTICAL_STYLE);
	}

    public IDimensionModel getModel() {
        return model;
    }

    public boolean isSubsetListOpen() {
        return subsetListOpen;
    }

    public void toggleSubsetList() {
    	if(isSubsetListOpen())
    		hideSubsetList();
    	else
    		openSubsetList();
    }

    public void openSubsetList() {
    	int left= this.getAbsoluteLeft();
    	int top = this.getAbsoluteTop();
    	top += this.getOffsetHeight();
        getSubsetSelectionPopup().show(left, top);
        subsetListOpen = true;
    }
    
    public void hideSubsetList() {
    	getSubsetSelectionPopup().hide();
    }
    
    public DimensionWidget (IDimensionModel model, IWidgetFactory widgetFactory) {
        if(model == null)
            throw new IllegalArgumentException("Model can not be null.");
        this.model = model;
        this.widgetFactory = widgetFactory;
        panel = buildWidget();
        createBasePanel();
        initWidget(basePanel);
        setDimensionSelectorVisible(true);
        setVertical(false);
        disableSelection();
        createSubsetSelectionPopup();
        resetTooltip();
    }
    
    private void disableSelection() {
    	JavaScript.disableSelection(basePanel);
    	JavaScript.disableSelection(this.horizontalLabel);
    	JavaScript.disableSelection(this.verticalLabel);
    	JavaScript.disableSelection(this.dimIcon);
    	JavaScript.disableSelection(this.selectorPanel);
    	JavaScript.disableSelection(this.dimensionSelector);
    	JavaScript.disableSelection(this.hSubsetButton);
    	JavaScript.disableSelection(this.vSubsetButton);
    	JavaScript.disableSelection(this.panel);
    }

	private VerticalPanel buildWidget() {
		VerticalPanel result = new VerticalPanel();
        buildDimensionSelector(this.widgetFactory);
        createVDimLabel();
        result.add(verticalLabel);
        hSubsetButton = createSubsetButton();
        vSubsetButton = createSubsetButton();
        HorizontalPanel hPanel = createHorizontalPanel();
        result.add(hPanel);
        getModel().getSubsetListModel().addComboboxListener(subsetComboboxListener);
        getModel().getComboboxModel().addComboboxListener(selectedElementListener);
        subsetComboboxListener.onSelectionChanged(null);
        selectorPanel = createSelectorPanel();
        result.add(selectorPanel);
        result.add(vSubsetButton);
        return result;
	}

	private void createBasePanel() {
		basePanel = new BasePanel();
        JavaScript.disableSelection(basePanel);
        basePanel.addMouseListener(mouseListener);
        basePanel.add(panel);
	}

	private void createSubsetSelectionPopup() {
		popup = new SubsetSelectionPopup(getModel().getSubsetListModel(), widgetFactory);
		popup.addPopupListener(subsetPopupListener);
		popup.setWidth("150px");
		popup.setHeight("300px");
	}
	
	protected SubsetSelectionPopup getSubsetSelectionPopup() {
		if(popup == null) {
			createSubsetSelectionPopup();
		}
		return popup;
	}

	private HorizontalPanel createSelectorPanel() {
		HorizontalPanel result = new HorizontalPanel();
        Label ballIcon = new Label();
        JavaScript.disableSelection(ballIcon);
        ballIcon.setStyleName(BALL_ICON_STYLE);
        result.add(ballIcon);
        result.add(dimensionSelector);
        result.setCellWidth(dimensionSelector, "100%");
        result.setWidth("100%");
        return result;
	}

	private HorizontalPanel createHorizontalPanel() {
        createHDimLabel();
		HorizontalPanel hPanel = new HorizontalPanel();
        dimIcon = new Label();
        dimIcon.addStyleName(DIM_ICON_STYLE);
        hPanel.add(dimIcon);
        hPanel.add(horizontalLabel);
        hPanel.setCellWidth(horizontalLabel, "100%");
        hPanel.add(hSubsetButton);
        hPanel.setWidth("100%");
		return hPanel;
	}

	private Label createSubsetButton() {
		Label result = new Label("");
		result.setStyleName(SUBSET_BUTTON_STYLE);
		result.addStyleName(SUBSET_UNSELECTED_STYLE);
		result.addClickListener(subsetButtonClickListener);
		return result;
	}

	private void createVDimLabel() {
		String dimName = getDimensionName();
		verticalLabel = new HTML(getVerticalText(dimName));
        verticalLabel.setStyleName("vertical-label");
	}

	private void createHDimLabel() {
		String dimName = getDimensionName();
        horizontalLabel = new HTML();
        
		dimName = JavaScript.cutToFit(dimName, DIM_NAME_MAX_WIDTH);
//        if(dimName.length()>14)
//        	horizontalLabel.setText(dimName.substring(0,11)+"...");
//        else
        	horizontalLabel.setText(dimName);
        horizontalLabel.setWidth("100%");
        horizontalLabel.setStyleName("label");
        horizontalLabel.addMouseListener(mouseListener);
     }

    protected void buildDimensionSelector(IWidgetFactory widgetFactory) {
        XObjectLabelFactory labelFactory = new XObjectLabelFactory();
		ITreeComboboxModel comboboxModel = getModel().getComboboxModel();
		dimensionSelector = new TreeCombobox(comboboxModel, widgetFactory, labelFactory);
		dimensionSelector.setSelectedFieldWidth(DIM_NAME_MAX_WIDTH);

        dimensionSelector.addMouseListener(mouseListener);
        dimensionSelector.setWidth("100%");
    }
    
    public TreeCombobox getDimensionSelector() {
    	return dimensionSelector;
    }

    public void addMouseListener(MouseListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null");
        mouseListeners.add(listener);
    }

    public void removeMouseListener(MouseListener listener)  {
        mouseListeners.remove(listener);
    }

    protected String getVerticalText(String text) {
        String result = "";
        boolean isLong = text.length()>6;
        if(isLong)
        	text = text.substring(0,5);
        char[] chars = text.toCharArray();
        for( int i = 0 ; i < chars.length; i++ ) { 
            result += chars[i] + "<BR/>";
        } 
        if(isLong)
        	result += "...";
        return result;
    }

    
    
    private class BasePanel extends FocusPanel {
    	
    	public void onBrowserEvent(Event event) {
            Element target = DOM.eventGetTarget(event);
    		if(target != null && 
                    (target.equals(hSubsetButton.getElement())||
                    target.equals(vSubsetButton.getElement()))) {
    			DOM.eventCancelBubble(event, true);
    		}
            else
                super.onBrowserEvent(event);
        }
    	
    }

}
