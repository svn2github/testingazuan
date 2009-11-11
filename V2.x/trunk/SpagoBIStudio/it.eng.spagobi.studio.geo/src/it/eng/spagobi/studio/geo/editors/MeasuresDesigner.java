package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.KPI;
import it.eng.spagobi.studio.geo.editors.model.geo.Param;
import it.eng.spagobi.studio.geo.editors.model.geo.Tresholds;
import it.eng.spagobi.studio.geo.util.DesignerUtils;

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
	
	private KPI fillMeasure(Shell dialog, String columnName){
		KPI kpi = new KPI();
		Tresholds tresholds = new Tresholds();
		kpi.setTresholds(tresholds);
		
		Text desc = (Text)dialog.getData("Description");
		kpi.setDescription(desc.getText());
		Text treshType = (Text)dialog.getData("TresholdsType");
		tresholds.setType(treshType.getText());
		Text treshLb = (Text)dialog.getData("TresholdsLb");
		tresholds.setLbValue(treshLb.getText());
		Text treshUb = (Text)dialog.getData("TresholdsUb");
		tresholds.setUbValue(treshUb.getText());
		
		Param param = new Param();
		tresholds.setParam(param);
		
		Text treshParamName = (Text)dialog.getData("TresholdsParamName");
		param.setName(treshParamName.getText());
		Text treshParamVal = (Text)dialog.getData("TresholdsParamValue");
		param.setValue(treshParamVal.getText());
		
		return kpi;
	} 
	
	public void createMeasuresShell(final Composite sectionClient, final String columnName){

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
		data.width = 140;
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
		
		final Text text = createTextWithLayout(dialog, label, data, "Description");
		
		//aggregate function
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(text, 5);

		Label labelAgg = new Label (dialog, SWT.RIGHT);
		labelAgg.setText ("Aggregate function:");		
		labelAgg.setLayoutData (data);
		
		final Combo textAgg = createComboWithLayout(dialog, labelAgg, data);
		textAgg.add("sum");
		textAgg.add("media");
		
		//color
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textAgg, 5);
		Label labelCol = new Label (dialog, SWT.RIGHT);
		labelCol.setText ("Colour:");		
		labelCol.setLayoutData (data);
		final Composite textColor = createColorPickWithLayout(dialog, labelCol, data);
		
		//tresholds
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textColor, 5);
		Label labelTreshType = new Label (dialog, SWT.RIGHT);
		labelTreshType.setText ("Tresholds type:");		
		labelTreshType.setLayoutData (data);
		final Text textTreshType = createTextWithLayout(dialog.getShell(), labelTreshType, data, "TresholdsType");
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textTreshType, 5);
		Label labelTreshLb = new Label (dialog, SWT.RIGHT);
		labelTreshLb.setText ("Tresholds lb value:");		
		labelTreshLb.setLayoutData (data);
		final Text textTreshLb = createTextWithLayout(dialog.getShell(), labelTreshLb, data, "TresholdsLb");
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textTreshLb, 5);
		Label labelTreshUb = new Label (dialog, SWT.RIGHT);
		labelTreshUb.setText ("Tresholds ub value:");		
		labelTreshUb.setLayoutData (data);
		final Text textTreshUb = createTextWithLayout(dialog.getShell(), labelTreshUb, data, "TresholdsUb");
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textTreshUb, 5);
		Label labelTreshParamName = new Label (dialog, SWT.RIGHT);
		labelTreshParamName.setText ("Tresholds param name:");		
		labelTreshParamName.setLayoutData (data);
		final Text textTreshParamName = createTextWithLayout(dialog.getShell(), labelTreshParamName, data, "TresholdsParamName");
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textTreshParamName, 5);
		Label labelTreshParamVal = new Label (dialog, SWT.RIGHT);
		labelTreshParamVal.setText ("Tresholds param value:");		
		labelTreshParamVal.setLayoutData (data);
		final Text textTreshParamVal = createTextWithLayout(dialog.getShell(), labelTreshParamVal, data, "TresholdsParamValue");
		
		//colours
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textTreshParamVal, 5);
		Label labelColoursType = new Label (dialog, SWT.RIGHT);
		labelColoursType.setText ("Colours type:");		
		labelColoursType.setLayoutData (data);
		final Composite textColoursType = createColorPickWithLayout(dialog, labelColoursType, data);
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textColoursType, 5);
		Label labelColoursOutbound = new Label (dialog, SWT.RIGHT);
		labelColoursOutbound.setText ("Colours outbound colour:");		
		labelColoursOutbound.setLayoutData (data);
		final Composite textColoursOutbound = createColorPickWithLayout(dialog, labelColoursOutbound, data);
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textColoursOutbound, 5);
		Label labelColoursNullVal = new Label (dialog, SWT.RIGHT);
		labelColoursNullVal.setText ("Colours outbound colour:");		
		labelColoursNullVal.setLayoutData (data);
		final Composite textColoursNullVal = createColorPickWithLayout(dialog, labelColoursNullVal, data);
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textColoursNullVal, 5);
		Label labelColParamName = new Label (dialog, SWT.RIGHT);
		labelColParamName.setText ("Colours param name:");		
		labelColParamName.setLayoutData (data);
		final Composite textColParamName = createColorPickWithLayout(dialog, labelColParamName, data);
		
		data = new FormData ();
		data.width = 140;
		data.top = new FormAttachment(textColParamName, 5);
		Label labelColParamVal = new Label (dialog, SWT.RIGHT);
		labelColParamVal.setText ("Colours param value:");		
		labelColParamVal.setLayoutData (data);
		final Composite textColParamVal = createColorPickWithLayout(dialog, labelColParamVal, data);
		
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Add");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				String aggFunc = textAgg.getText();
				String descr = text.getText();
				fillMeasure(dialog, columnName);
				dialog.close ();
			}
		});
		
		Button delete = new Button (dialog, SWT.PUSH);
		delete.setText ("Delete");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (ok, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		delete.setLayoutData (data);
		delete.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				//delete measure
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
	
	
	private Text createTextWithLayout(Shell dialog, Label itsLabel, FormData data, String dataKey){
		Text text = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (itsLabel, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (itsLabel, 0, SWT.CENTER);
		text.setLayoutData (data);
		dialog.setData(dataKey, text);
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
	
	private Composite createColorPickWithLayout(Shell dialog, Label itsLabel, FormData data){
		final Label colorLabel = new Label(dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 50;
		data.left = new FormAttachment (itsLabel, 0, SWT.DEFAULT);
		data.right = new FormAttachment (50, 0);
		data.top = new FormAttachment (itsLabel, 0, SWT.CENTER);
		colorLabel.setLayoutData(data);
		
		Composite colorSection = DesignerUtils.createColorPicker(dialog, "#FF0000", colorLabel);
		data = new FormData ();
		data.width = 50;
		data.left = new FormAttachment (colorLabel, 0, SWT.DEFAULT);
		data.right = new FormAttachment (70, 50);
		data.top = new FormAttachment (colorLabel, 0, SWT.CENTER);
		colorSection.setLayoutData (data);
		return colorSection;
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
