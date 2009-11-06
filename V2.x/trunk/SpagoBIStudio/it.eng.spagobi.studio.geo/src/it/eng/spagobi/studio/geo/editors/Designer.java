package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Designer {


	private GEOEditor editor=null;
	private Composite mainComposite;
	
	private Vector<String> dataSets;
	private Vector<String> maps;
	
	public Designer(Composite _composite, GEOEditor _editor) {
		super();
		FormLayout layout=new FormLayout();
		//composite.setLayout(layout);
		mainComposite= _composite;

		this.editor=_editor;
	}
	
	public void initializeDesigner(GEODocument geoDocument){
		System.out.println("loading informations for designer");
		dataSets = new Vector<String>();
		for (int i=0; i< 4 ; i++){
			dataSets.add("dataset"+i);
		}
		createDatasetCombo();
		
		maps = new Vector<String>();
		for (int i=0; i< 5 ; i++){
			maps.add("map"+i);
		}
		createMapCombo();
		//mainComposite.pack();
		mainComposite.redraw();
	}
	
	private void createDatasetCombo(){
		Label datasetLabel = new Label(mainComposite,  SWT.SIMPLE);
		datasetLabel.setText("Data Set");
		final Combo datasetCombo = new Combo(mainComposite,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		for(int i=0; i< dataSets.size(); i++){
			datasetCombo.setText(dataSets.elementAt(i));
		}		
	}
	
	private void createMapCombo(){
		Label mapLabel = new Label(mainComposite,  SWT.SIMPLE);
		mapLabel.setText("Map");
		final Combo mapCombo = new Combo(mainComposite,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		for(int i=0; i< maps.size(); i++){
			mapCombo.setText(maps.elementAt(i));
		}		
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
