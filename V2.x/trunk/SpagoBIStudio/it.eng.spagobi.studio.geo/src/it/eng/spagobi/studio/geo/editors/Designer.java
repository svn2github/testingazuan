package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Designer {


	private GEOEditor editor=null;
	private Composite mainComposite;

	
	public Designer(Composite _composite, GEOEditor _editor) {
		super();
		mainComposite= _composite;
		editor = _editor;
	}
	
	protected void createHierarchiesTree(Composite sectionClient){
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =4;
				
		Group hierarchiesGroup = new Group(sectionClient, SWT.FILL | SWT.RESIZE);
		hierarchiesGroup.setLayout(sectionClient.getLayout());
		hierarchiesGroup.setLayoutData(gd);
		
		final Tree hierarchiesTree = new Tree(hierarchiesGroup, SWT.SINGLE | SWT.BORDER );
		hierarchiesTree.setLayoutData(gd);
	    for (int i = 0; i < 4; i++) {
	        TreeItem iItem = new TreeItem(hierarchiesTree, 0);
	        iItem.setText("TreeItem (0) -" + i);
	        
	        for (int j = 0; j < 4; j++) {
	          TreeItem jItem = new TreeItem(iItem, 0);
	          jItem.setText("TreeItem (1) -" + j);
	        }
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
