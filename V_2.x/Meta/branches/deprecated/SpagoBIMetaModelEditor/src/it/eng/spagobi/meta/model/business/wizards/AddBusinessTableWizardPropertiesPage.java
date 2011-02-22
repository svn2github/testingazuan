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
package it.eng.spagobi.meta.model.business.wizards;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

/**
 * @author cortella
 *
 */
public class AddBusinessTableWizardPropertiesPage extends WizardPage {
	private Text textName;
	private Text textDescription;
	private PhysicalTable physicalTable;
	private BusinessModel owner;
	private AddBusinessTableWizardPagePhysicalTableSelection pageOneRef;

	/**
	 * @param pageName
	 */
	protected AddBusinessTableWizardPropertiesPage(String pageName, BusinessModel owner, AddBusinessTableWizardPagePhysicalTableSelection physicalTableSelectionPage, PhysicalTable physicalTable) {
		super(pageName);
		setTitle("Business Table Creation");
		setDescription("Please set the properties of your Business Table");
		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("wizards/createBC.png"));
		if (image!=null) setImageDescriptor(image);
		this.physicalTable = physicalTable;
		this.owner = owner;
		this.pageOneRef =  physicalTableSelectionPage;
	}


	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
 		//Important: Setting page control
 		setControl(composite);
 		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		createPropertiesGroup(composite, SWT.NONE);

 		if (physicalTable != null){
 			setSuggestedBusinessTableName(physicalTable.getName());
 		}
 		
 		checkPageComplete();
	}
	
	public void createPropertiesGroup(Composite composite, int style)	{
 		Group groupBusinessTableProperties = new Group(composite, style);
 		groupBusinessTableProperties.setText("Business Table Properties");
 		groupBusinessTableProperties.setLayout(new GridLayout(2, false));
 		
 		Label lblName = new Label(groupBusinessTableProperties, SWT.NONE);
 		lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
 		lblName.setText("Name: ");
 		
 		textName = new Text(groupBusinessTableProperties, SWT.BORDER);
 		textName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent me){
				String string = textName.getText();
 				if (checkNameAlreadyUsed(string)){
 					setErrorMessage("Name already in use or not valid");
 					setPageComplete(false);
 				} else if (string.length() > 0) {
 					setErrorMessage(null);
 					setMessage("Please set the properties of your Business Table");
 					checkPageComplete();
 				}	
			}
			});

 		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		
 		Label lblDescription = new Label(groupBusinessTableProperties, SWT.NONE);
 		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
 		lblDescription.setText("Description: ");
 		
 		textDescription = new Text(groupBusinessTableProperties, SWT.BORDER);
 		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	public void setSuggestedBusinessTableName(String passedPhysicalTableName){
		String physicalTableName = passedPhysicalTableName;
		/*
		if (physicalTable != null){
			physicalTableName = physicalTable.getName();
		}
		*/
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		String suggestedName =  initializer.beutfyName(physicalTableName);
		//check if name is already in use in the Business Model
		boolean nameUsed = checkNameAlreadyUsed(suggestedName);
		if (!nameUsed){
			textName.setText(suggestedName);
		}
		else {
			while(nameUsed){
				suggestedName = suggestedName + "_copy";
				nameUsed = checkNameAlreadyUsed(suggestedName);
			}	
			textName.setText(suggestedName);
		}
		setErrorMessage(null);
	}
	
	
	public String getName(){
		return textName.getText();
	}
	
	public String getDescription(){
		return textDescription.getText();
	}
	
	/*
	 * Check if name is already in use in the Business Model
	 */
	public boolean checkNameAlreadyUsed(String BusinessTableName){
		if (owner.getTable(BusinessTableName) != null){
			return true;
		} else {
			return false;
		}
	}
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if (textName.getText().length() > 0){
			setErrorMessage(null);
			setMessage("Please set the properties of your Business Table");
			this.
			setPageComplete(true);
		} else {			
			setErrorMessage("Name already in use or not valid");
			setPageComplete(false);
		}
	}
	
}
