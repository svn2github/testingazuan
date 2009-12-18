package com.tensegrity.palowebviewer.modules.widgets.client.combobox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.IWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.LabelWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.LabeledImage;
import com.tensegrity.palowebviewer.modules.widgets.client.dispose.IDisposable;
import com.tensegrity.palowebviewer.modules.widgets.client.list.IListModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.list.ListModelEvent;


/**
 * Widget that represents dropdown list when someone tries to choose item in a
 * combobox.  
 *
 */
public class SelectionListWidget extends Composite implements IDisposable
{

    private final IListComboboxModel model;
    private final List itemList = new ArrayList();
    private VerticalPanel panel;
    private IWidgetFactory widgetFactory = new LabelWidgetFactory();
    private final IListModelListener listListener = new IListModelListener() {

		public void contentsChanged(ListModelEvent event) {
			reinitList();
		}

		public void intervalAdded(ListModelEvent event) {
			reinitList();
		}

		public void intervalRemoved(ListModelEvent event) {
			reinitList();
		}
    	
    };
    private final IComboboxListener comboboxListener = new IComboboxListener () {

    	public void onSelectionChanged(Object oldItem) {
    		for (Iterator it = itemList.iterator(); it.hasNext();) {
				ListItem item = (ListItem) it.next();
				item.checkSelection();
				
			}
        }

		public void onInvalidItemSet(Object item) {
		}
    };

    public IWidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    public void setWidgetFactory(IWidgetFactory factory) {
        if(factory == null)
            throw new IllegalArgumentException("Widget factory can not be null");
        if(widgetFactory != factory) {
            widgetFactory = factory;
            reinitList();
        }
    }

    public IListComboboxModel getModel() {
        return model;
    }

    public SelectionListWidget(IListComboboxModel model) {
        if(model == null)
            throw new IllegalArgumentException("Model can not be null.");
        this.model = model;
        this.model.addListModelListener(listListener);
        this.model.addComboboxListener(comboboxListener);
        buildWidget();
    }

    protected void buildWidget() {
        panel = new VerticalPanel();
        initWidget(panel);
        reinitList();
    }

    protected void reinitList() {
        clear();
        IListComboboxModel listModel = getModel();
        int size = listModel.getSize();
        for( int i = 0 ; i < size; i++ ) { 
            Object item = model.getElementAt(i);
            ListItem listItem = new ListItem(item);
            itemList.add(listItem);
            panel.add(listItem);
        } 
        comboboxListener.onSelectionChanged(null);
    }

	private void clear() {
		panel.clear();
	}

    protected class ListItem extends HorizontalPanel implements ClickListener {

        public static final String STYLE_NAME = "list-item";
        public static final String SELECTION_LABEL_STYLE = "selection-label";
        public static final String SELECTED_STYLE = "selected";
        private final Object item;
        private final Widget itemWidget;
        private final LabeledImage selectionLabel;

        public void onClick(Widget sender) {
            getModel().setSelectedItem(getItem());
        }

        public Object getItem() {
            return item;
        }

        public ListItem(Object item) {
            this.item = item;
            this.itemWidget = getWidgetFactory().createWidgetFor(item);
            itemWidget.addStyleName("tensegrity-gwt-clickable");
            selectionLabel = new LabeledImage(SELECTION_LABEL_STYLE, "");
            selectionLabel.addClickListener(this);
            if(itemWidget instanceof SourcesClickEvents) {
                SourcesClickEvents sce = (SourcesClickEvents)itemWidget;
                sce.addClickListener(this);
            }
            add(selectionLabel);
            add(itemWidget);
            setStyleName(STYLE_NAME);
        }
        
		public void checkSelection() {
			String text = "";
            if(getItem() == getModel().getSelectedItem() ){
                text = "*";
                selectionLabel.addStyleName(SELECTED_STYLE);
            }
            else {
                selectionLabel.removeStyleName(SELECTED_STYLE);
            }
            selectionLabel.setText(text);
		}

    }

	public void dispose() {
		this.model.removeListModelListener(listListener);
		this.model.removeComboboxListener(comboboxListener);
		
	};

}
