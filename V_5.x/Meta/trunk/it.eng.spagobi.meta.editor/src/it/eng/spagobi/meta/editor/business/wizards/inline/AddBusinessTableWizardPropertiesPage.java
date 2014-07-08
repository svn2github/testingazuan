/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

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

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	/**
	 * @param pageName
	 */
	protected AddBusinessTableWizardPropertiesPage(String pageName, BusinessModel owner, AddBusinessTableWizardPagePhysicalTableSelection physicalTableSelectionPage, PhysicalTable physicalTable) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.addbusinessclass.title"));
		setDescription(RL.getString("business.editor.wizard.addbusinessclass.properties.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
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
 		groupBusinessTableProperties.setText(RL.getString("business.editor.wizard.addbusinessclass.properties"));
 		groupBusinessTableProperties.setLayout(new GridLayout(2, false));
 		
 		Label lblName = new Label(groupBusinessTableProperties, SWT.NONE);
 		lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
 		lblName.setText(RL.getString("business.editor.wizard.addbusinessclass.properties.label.name"));
 		
 		textName = new Text(groupBusinessTableProperties, SWT.BORDER);
 		textName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent me){
				String string = textName.getText();
 				if (checkNameAlreadyUsed(string)){
 					setErrorMessage(RL.getString("business.editor.wizard.addbusinessclass.properties.error.name"));
 					setPageComplete(false);
 				} else if (string.length() > 0) {
 					setErrorMessage(null);
 					setMessage(RL.getString("business.editor.wizard.addbusinessclass.properties.message"));
 					checkPageComplete();
 				}	
			}
			});

 		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		
 		Label lblDescription = new Label(groupBusinessTableProperties, SWT.NONE);
 		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
 		lblDescription.setText(RL.getString("business.editor.wizard.addbusinessclass.properties.label.description"));
 		
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
			setMessage(RL.getString("business.editor.wizard.addbusinessclass.properties.message"));
			setPageComplete(true);
		} else {			
			setErrorMessage(RL.getString("business.editor.wizard.addbusinessclass.properties.error.name"));
			setPageComplete(false);
		}
	}
	
}
