/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.editor.multi.wizards;



import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * @author cortella
 *
 */
public class NewModelWizardFileCreationPage extends WizardNewFileCreationPage {
	private Text modelNameFieldInput;
	
	public NewModelWizardFileCreationPage(IStructuredSelection selection) {
        super("NewSpagoBIModelWizardPage", selection);
        setTitle("SpagoBI Model");
        setDescription("Creates a new SpagoBI Model");
        setFileExtension("sbimodel");
    }
	
	 public void createControl(Composite parent) {
		 // inherit default container and name specification widgets
		 super.createControl(parent);
		 Composite composite = (Composite)getControl();
		 
		 // ***** Adding Customization ******
		 Composite container = new Composite(composite, SWT.NULL);
		 container.setLayout(new GridLayout(2, false));
		 container.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL |
				 GridData.HORIZONTAL_ALIGN_FILL));
		 
		 // Add a group
		 Group group = new Group(container,SWT.NONE);
		 group.setLayout(new GridLayout(2,false));
		 group.setText("SpagoBI Model");
		 group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL |
				 GridData.HORIZONTAL_ALIGN_FILL));
		 
		 // Add label and text input
		 new Label(group, SWT.NONE).setText("Model name:");
		 modelNameFieldInput = new Text(group, SWT.BORDER);
		 modelNameFieldInput.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL |
				 GridData.HORIZONTAL_ALIGN_FILL));
		 modelNameFieldInput.setText("MyModel");
	 }
	 
	 public String getModelName() {
		 return modelNameFieldInput.getText();
	 }
	 

//	private Text modelNameFieldInput;
//	private StringButtonFieldEditor modelFileNameFieldInput;
//	private org.eclipse.swt.widgets.List connectionList;
//	private boolean nameSetted = true;
	
	/**
	 * @param pageName
	 */
//	protected NewModelWizardFileCreationPage(String pageName) {
//		super(pageName);
//		setTitle("Create new SpagoBI Metamodel");
//		setDescription("This wizard drives you to create a new SpagoBI Meta Business Model," +
//		" please insert a name for your BM.");
//		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("full/wizban/newModelWizard"));
//	    if (image!=null) {
//	    	setImageDescriptor(image);
//	    }
//	    dseBridge = new DSEBridge();
//	}
//
//	@Override
//	public void createControl(Composite parent) {
//		Composite composite = new Composite(parent, SWT.NULL);
//		composite.setLayout(new GridLayout(3, false));
//		
//		new Label(composite, SWT.NONE).setText("Model name:");
//		GridData gridData = new GridData();
//		gridData.horizontalAlignment = GridData.FILL;
//		gridData.horizontalSpan = 2;
//		modelNameFieldInput = new Text(composite, SWT.BORDER);
//		modelNameFieldInput.setLayoutData(gridData);
//		modelNameFieldInput.setText("Test Model");
//
//		modelFileNameFieldInput = new FileFieldEditor("modelfile", "Model file:", composite);
//		modelFileNameFieldInput.setStringValue("D:/Documenti/Progetti/metadati/libri/TestModel.sbimodel");
//		modelFileNameFieldInput.setEmptyStringAllowed(false);
//		
//		modelNameFieldInput.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				if (modelNameFieldInput.getText().length()> 0)
//					nameSetted = true;
//				else
//					nameSetted = false;
//				checkPageComplete();
//			}
//		});
//		//Important: Setting page control
// 		setControl(composite);
// 		
// 		checkPageComplete();
//	}
//
//	public void checkPageComplete() {
//		String fileName = modelFileNameFieldInput.getStringValue();
//		
//		if ( (fileName != null ) && (fileName.length()>0 ) && (nameSetted) ){
//			setPageComplete(true);
//		} 
//		else
//			setPageComplete(false);
//	}
//	
//	public String getModelFileName() {
//		String fileName = modelFileNameFieldInput.getStringValue();
//		if(!fileName.endsWith(".sbimodel")) {
//			fileName += ".sbimodel";
//		}
//		return fileName;
//	}
//	
//	public File getModelFile() {
//		return new File( getModelFileName() );
//	}
//
//	public String getModelName() {
//		return modelNameFieldInput.getText();
//	}

	
}
