package it.eng.spagobi.studio.documentcomposition.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PropertiesView extends org.eclipse.ui.part.ViewPart {
	private Label label;
	
	public void setFocus() {
		label.setFocus();
	}
	
	public void createPartControl(Composite parent) {
		label = new Label(parent, 0);
		label.setText("Hello World");
	}

	
	
}
