package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.ISubsetListModel;
import com.tensegrity.palowebviewer.modules.widgets.client.IWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IListComboboxModel;
import com.tensegrity.palowebviewer.modules.widgets.client.combobox.SelectionListWidget;
import com.tensegrity.palowebviewer.modules.widgets.client.dispose.IDisposable;

public class SubsetSelectionPopup extends PopupPanel implements IDisposable {

	public static String POPUP_STYLE = "popup";
	private final ISubsetListModel model;
	private SelectionListWidget subsetList;
	private IWidgetFactory widgetFactory;
	private ScrollPanel scroll;
	
	public SubsetSelectionPopup(ISubsetListModel model, IWidgetFactory widgetFactory) {
		super(true);
		this.model = model;
		this.widgetFactory = widgetFactory;
		buildWidget();
		
	}
	
	public IListComboboxModel getListModel() {
		return model;
	}
	
	protected void buildWidget() {
		subsetList = new SelectionListWidget(getListModel());
        subsetList.setWidgetFactory(widgetFactory);
        scroll = new ScrollPanel(subsetList);
        this.add(scroll);
        this.setStyleName(POPUP_STYLE);
	}
	
	public void show() {
		if(!model.isLoaded()){
			model.load();
		}
		scroll.setWidth("100%");
		scroll.setHeight("100%");
		super.show();
	}
	
	public void show(int left, int top){
		setPopupPosition(left, top);
		show();
	}

	public void dispose() {
		subsetList.dispose();
	}

}
