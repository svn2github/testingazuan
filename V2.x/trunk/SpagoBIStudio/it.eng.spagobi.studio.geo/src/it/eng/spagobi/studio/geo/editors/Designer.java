package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

public class Designer {


	private GEOEditor editor=null;
	private Composite mainComposite;
	
	public Designer(Composite composite, GEOEditor _editor) {
		super();
		FormLayout layout=new FormLayout();
		//composite.setLayout(layout);
		mainComposite=composite;

		this.editor=_editor;
	}
	
	public void initializeDesigner(GEODocument geoDocument){
		System.out.println("eccoci nel designer");
	}
	
	public GEOEditor getEditor() {
		return editor;
	}

	public void setEditor(GEOEditor editor) {
		this.editor = editor;
	}

	public Composite getMainComposite() {
		return mainComposite;
	}

	public void setMainComposite(Composite mainComposite) {
		this.mainComposite = mainComposite;
	}
}
