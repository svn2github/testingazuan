package it.eng.spagobi.studio.core.properties;

import it.eng.spagobi.studio.core.log.SpagoBILogger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;

public class PropertyPage extends org.eclipse.ui.dialogs.PropertyPage implements
		IWorkbenchPropertyPage {

	public static QualifiedName DOCUMENT_ID = new QualifiedName("it.eng.spagobi.sdk.document.id", "Identifier");
	public static QualifiedName DOCUMENT_LABEL = new QualifiedName("it.eng.spagobi.sdk.document.label", "Label");
	public static QualifiedName DOCUMENT_NAME = new QualifiedName("it.eng.spagobi.sdk.document.name", "Name");
	public static QualifiedName DOCUMENT_DESCRIPTION = new QualifiedName("it.eng.spagobi.sdk.document.description", "Description");
	public static QualifiedName DOCUMENT_TYPE = new QualifiedName("it.eng.spagobi.sdk.document.type", "Type");
	public static QualifiedName DOCUMENT_STATE = new QualifiedName("it.eng.spagobi.sdk.document.description", "State");
	
	public static QualifiedName DATASET_ID = new QualifiedName("it.eng.spagobi.sdk.dataset.id", "Identifier");
	public static QualifiedName DATASET_LABEL = new QualifiedName("it.eng.spagobi.sdk.dataset.label", "Label");
	public static QualifiedName DATASET_NAME = new QualifiedName("it.eng.spagobi.sdk.dataset.name", "Name");
	public static QualifiedName DATASET_DESCRIPTION = new QualifiedName("it.eng.spagobi.sdk.dataset.description", "Description");
	
	public static QualifiedName DATA_SOURCE_ID = new QualifiedName("it.eng.spagobi.sdk.datasource.id", "Identifier");
	
	public static QualifiedName ENGINE_ID = new QualifiedName("it.eng.spagobi.sdk.engine.id", "Identifier");
	public static QualifiedName ENGINE_LABEL = new QualifiedName("it.eng.spagobi.sdk.engine.label", "Label");
	public static QualifiedName ENGINE_NAME = new QualifiedName("it.eng.spagobi.sdk.engine.name", "Name");
	public static QualifiedName ENGINE_DESCRIPTION = new QualifiedName("it.eng.spagobi.sdk.engine.description", "Description");
	
	public PropertyPage() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {
		// hide default buttons
		this.noDefaultAndApplyButton();
		
		Composite container = new Composite(parent, SWT.NULL);
		RowLayout rowLayout = new RowLayout();
 		rowLayout.wrap = false;
 		rowLayout.pack = true;
 		rowLayout.justify = false;
 		rowLayout.type = SWT.VERTICAL;
 		rowLayout.marginLeft = 5;
 		rowLayout.marginTop = 5;
 		rowLayout.marginRight = 5;
 		rowLayout.marginBottom = 5;
 		rowLayout.spacing = 0;
 		rowLayout.fill = true;
 		container.setLayout(rowLayout);
	    
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 10;
		
		Group docGroup = new Group(container, SWT.NULL);
	    docGroup.setText("Document's information:");
	    docGroup.setLayout(new FillLayout());
		Composite docContainer = new Composite(docGroup, SWT.NULL);
		docContainer.setLayout(layout);
	    
		Group dataSetGroup = new Group(container, SWT.NULL);
	    dataSetGroup.setText("Dataset's information:");
	    dataSetGroup.setLayout(new FillLayout());
		Composite datasetContainer = new Composite(dataSetGroup, SWT.NULL);
		datasetContainer.setLayout(layout);
	    
		Group engineGroup = new Group(container, SWT.NULL);
	    engineGroup.setText("Engine's information:");
	    engineGroup.setLayout(new FillLayout());
		Composite engineContainer = new Composite(engineGroup, SWT.NULL);
		engineContainer.setLayout(layout);

		addPropertyContent(docContainer, DOCUMENT_ID);
		addPropertyContent(docContainer, DOCUMENT_LABEL);
		addPropertyContent(docContainer, DOCUMENT_NAME);
		addPropertyContent(docContainer, DOCUMENT_DESCRIPTION);
		addPropertyContent(docContainer, DOCUMENT_TYPE);
		addPropertyContent(docContainer, DOCUMENT_STATE);
		addPropertyContent(datasetContainer, DATASET_ID);
		addPropertyContent(datasetContainer, DATASET_LABEL);
		addPropertyContent(datasetContainer, DATASET_NAME);
		addPropertyContent(datasetContainer, DATASET_DESCRIPTION);
		addPropertyContent(engineContainer, ENGINE_ID);
		addPropertyContent(engineContainer, ENGINE_LABEL);
		addPropertyContent(engineContainer, ENGINE_NAME);
		addPropertyContent(engineContainer, ENGINE_DESCRIPTION);
		
		return container;
	}
	
	private void addPropertyContent(Composite composite, QualifiedName qn) {
		Label label = new Label(composite, SWT.NULL);
		label.setText(qn.getLocalName());
		Label value = new Label(composite, SWT.NULL);
		value.setText(getProperty(qn));
	}

	protected String getProperty(QualifiedName qn) {
		IFile file = (IFile) this.getElement();
		try {
			String value = file.getPersistentProperty(qn);
			if (value == null)
				return "EMPTY";
			return value;
		} catch (CoreException e) {
			SpagoBILogger.errorLog("Error while recovering property " + qn.getLocalName(), e);
			return "Error while recovering property " + qn.getLocalName();
		}
	}
	
	
	
//	protected void setDocumentId(String documentId) {
//		IFile file = (IFile) this.getElement();
//		String value = documentId;
//		if (value.equals("")) value = null;
//		try {
//			file.setPersistentProperty(DOCUMENT_ID_PROP_KEY, value);
//		} catch (CoreException e) {}
//	}
	
	public boolean performOk() {
//		setDocumentId(textField.getText());
		return super.performOk();
	}

}
