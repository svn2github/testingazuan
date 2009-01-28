package com.tensegrity.palowebviewer.modules.util.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Helper class for JavaScript.
 *
 */
public class JavaScript {
	
	
	private static Label testWidthDiv;
	
	public static String getUrlParam(String name){
		return getUrlParam(name, getMainWindow());
	};
	
	private native static String getUrlParam(String name, JavaScriptObject wnd)/*-{
		//alert(wnd.location.href);
		var mask = "[\\?&]"+ name + "=([^&#]*)";
		var regex = new RegExp(mask);
		var result = regex.exec(wnd.location.href);
		if(result != null) {
			result = result[1];
		}
		return result;
	}-*/;
	
	
	public native static JavaScriptObject getMainWindow()/*-{
		var result = window;
		while(result.name != 'wpalo-main') {
			//alert(result.name + "\n" + result.location.href);
			result = result.parent;
		}
		return parent;
	}-*/;
	
	public static int testWidth(String text) {
		return testWidth(getMainWindow(), text);
	}
	
	public native static int testWidth(JavaScriptObject wnd, String text)/*-{
		var objTestWidth = wnd.document.getElementById("testWidth")
		objTestWidth.innerHTML = text;
		return objTestWidth.clientWidth;
	}-*/;
	
	public static int testHeight(String text){
		Label label = getTestWidthDiv();
		label.setText(text);
		int result = label.getOffsetHeight();
		return result;
	}
	
	public static String cutToFit(String text, int width) {
		if(text == null)
			throw new IllegalArgumentException("text can not be null");
		if(width <= 0)
			throw new IllegalArgumentException("width must be positive");
		if(testWidth(text) > width) {
			int last = text.length()-2;
			while(testWidth(text+"...") > width && last >= 0) {
				text = text.substring(0,last);
				last--;
			}
			text +="...";
		}
		return text;
	}

	
	
	
	
	public static native void disableSelection(Element element)/*-{
    	element.unselectable = "on";
    	element.style.MozUserSelect = "none";
	}-*/;
	public static void disableSelection(Widget widget) {
		disableSelection(widget.getElement());
	}

	private static Label getTestWidthDiv() {
		if (testWidthDiv == null) {
			testWidthDiv = new Label();
			Panel panel = new SimplePanel();
			panel.add(testWidthDiv);
			Element element = panel.getElement();
			DOM.setElementProperty(element, "position", "absolute");
			DOM.setElementProperty(element, "visibility", "hidden");
			DOM.setElementProperty(element, "white-space", "nowrap");
			RootPanel.get().add(panel);
		}
		return testWidthDiv;
	}
	
	public static void setTooltip(Widget w, String tooltip) {
		Element element = w.getElement();
		DOM.setElementProperty(element, "title", tooltip);
	}
	
	public static Element findElementWithStyle(Element parent, String styleName) {
		Element result = null;
		final int size = DOM.getChildCount(parent);
		for ( int i = 0; i < size && result == null; i ++) {
			Element element = DOM.getChild(parent, i);
			String className = DOM.getElementProperty(element, "className");
			if(styleName.equals(className)) {
				result = element;
			}
			else {
				result = findElementWithStyle(element, styleName);
			}
			
		}
		return result;
		
	}

}
