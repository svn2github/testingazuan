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
	 
	 @Override
	protected void initialPopulateContainerNameField() {
		// TODO Auto-generated method stub
		super.initialPopulateContainerNameField();
	}

}
