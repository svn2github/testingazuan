package com.tensegrity.palowebviewer.modules.widgets.client.tab;

import com.google.gwt.user.client.ui.Widget;

public interface ITabElement {

	public abstract String getTitle();

	public abstract void setTitle(String title);

	public abstract Widget getWidget();

	public abstract void setWidget(Widget widget);
	
	public Object getAttachedObject();

	public void attachObject(Object object);
	
	public void close(ITabCloseCallback callback);
	
	public String getImgURL();

	public void setImgURL(String imgURL);
	

}