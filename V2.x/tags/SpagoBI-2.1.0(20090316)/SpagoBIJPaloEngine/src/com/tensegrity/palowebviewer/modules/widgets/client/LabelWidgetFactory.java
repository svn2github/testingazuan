package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * Kind of {@link IWidgetFactory} that generates simple Label for given object.
 * The text for the Label is taken from Object.toString() method or "null" if the <code>object</code>
 * is <code>null</code>.
 *
 */
public class LabelWidgetFactory implements IWidgetFactory
{

    public Widget createWidgetFor(Object object) {
    	if(object == null)
    		object = "none";
        return new Label( object+"" );
    }

	public LabelWidgetFactory () {

	}

}
