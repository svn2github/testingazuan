package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.Composite;


/**
 * Label that is used to replace widgets for objects that aren't yet loaded.
 * CSS: tensegrity-gwt-loading-label
 */
public class LoadingLabel extends Composite
{
	private final LabeledImage label;

	public LoadingLabel () {
     this("   Loading...");
	}
	
	public LoadingLabel (String text) {
        label = new LabeledImage("tensegrity-gwt-loading-label", text);
        initWidget(label);
	}

}
