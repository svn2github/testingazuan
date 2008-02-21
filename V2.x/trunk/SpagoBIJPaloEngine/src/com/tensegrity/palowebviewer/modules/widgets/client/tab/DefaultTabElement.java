package com.tensegrity.palowebviewer.modules.widgets.client.tab;

import com.google.gwt.user.client.ui.Widget;


public class DefaultTabElement implements ITabElement {
	
	private Object attachment;
	private Widget widget;
	private String title;
	private String imgURL;
	private boolean closeable;
	private ITabPanelModel model;
	private ITabActionListener listener;

	public DefaultTabElement(String imgURL, String title, boolean closeable, Widget widget, ITabPanelModel model, 
			ITabActionListener listener) {
		this.imgURL = imgURL;
		this.title = title;
		this.closeable = closeable;
		this.widget = widget;
		this.model = model;
		this.listener = listener;
	}
	
	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabElement#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabElement#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
		model.changeTitle(this);
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabElement#getWidget()
	 */
	public Widget getWidget() {
		return widget;
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabElement#setWidget(com.google.gwt.user.client.ui.Widget)
	 */
	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public Object getAttachedObject() {
		return attachment;
	}

	public void attachObject(Object object) {
		this.attachment = object;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	public void close(ITabCloseCallback callback) {
		if(listener != null){
			listener.tryClose(callback);
		}
	}
	
}