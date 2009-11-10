package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MeasuresDesigner {
	private GEOEditor editor=null;
	private Composite mainComposite;
	private GEODocument geoDocument;

	public MeasuresDesigner(Composite _composite, GEOEditor _editor) {
		super();
		mainComposite= _composite;
		editor = _editor;
	}

	public MeasuresDesigner(Composite _composite, GEOEditor _editor, GEODocument _geoDocument) {
		super();
		mainComposite= _composite;
		editor = _editor;
		geoDocument = _geoDocument;
	}
	
	public void createMeasuresShell(final Composite sectionClient, String columnName){
		System.out.println("column measure::"+columnName);
		final Shell dialog = new Shell (mainComposite.getDisplay(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("New Measure for "+columnName);
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label label = new Label (dialog, SWT.RIGHT);
		label.setText ("Description:");
		FormData data = new FormData ();
		data.width = 100;
		label.setLayoutData (data);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setText ("Cancel");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (100, 0);
		data.bottom = new FormAttachment (100, 0);
		cancel.setLayoutData (data);
		cancel.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("User cancelled dialog");
				dialog.close ();
			}
		});
		
		final Text text = createTextWithLayout(dialog, label, data);

/*		final Text text = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (label, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (label, 0, SWT.CENTER);
		text.setLayoutData (data);*/
		
		//aggregate function
		data = new FormData ();
		data.width = 100;
		data.top = new FormAttachment(text, 5);

		Label labelAgg = new Label (dialog, SWT.RIGHT);
		labelAgg.setText ("Aggregate function:");		
		labelAgg.setLayoutData (data);
		
		final Combo textAgg = createComboWithLayout(dialog, labelAgg, data);
		textAgg.add("sum");
		textAgg.add("media");
		
/*		final Combo textAgg = new Combo (dialog, SWT.BORDER);
		textAgg.add("sum");
		textAgg.add("media");
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelAgg, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelAgg, 0, SWT.CENTER);
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		textAgg.setLayoutData (data);*/
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String aggFunc = textAgg.getText();
				String descr = text.getText();
				//createNewHierarchy(hierarchiesTree, name, type);
				dialog.close ();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack ();
		dialog.open ();
		
	}
	
	
	private Text createTextWithLayout(Shell dialog, Label itsLabel, FormData data){
		Text text = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (itsLabel, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (itsLabel, 0, SWT.CENTER);
		text.setLayoutData (data);
		return text;
	}
	private Combo createComboWithLayout(Shell dialog, Label itsLabel, FormData data){
		Combo combo = new Combo (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (itsLabel, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (itsLabel, 0, SWT.CENTER);
		combo.setLayoutData (data);
		return combo;
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

	public GEODocument getGeoDocument() {
		return geoDocument;
	}

	public void setGeoDocument(GEODocument geoDocument) {
		this.geoDocument = geoDocument;
	}
	
}
