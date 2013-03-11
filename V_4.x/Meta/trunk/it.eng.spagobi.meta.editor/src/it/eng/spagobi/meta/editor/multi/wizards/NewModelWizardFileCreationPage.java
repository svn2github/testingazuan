/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.multi.wizards;



import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class NewModelWizardFileCreationPage extends WizardNewFileCreationPage {
	private Text modelNameFieldInput;
	private static Logger logger = LoggerFactory.getLogger(NewModelWizardFileCreationPage.class);
	private Map<String,String> modelsPresent = null;
	Label error = null;

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
		//composite.getShell().setSize(600, 800);
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
		modelNameFieldInput.setTextLimit(20);
		modelNameFieldInput.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL |
				GridData.HORIZONTAL_ALIGN_FILL));
		modelNameFieldInput.setText("MyModel");
		

		error = new Label(group, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan=2;
		error.setForeground(new Color(container.getDisplay(), new RGB(255,0,0)));
		error.setText("                                                                    ");
		error.setVisible(false);
		error.setLayoutData(gridData);

		modelNameFieldInput.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event event) {
				setErrorMessageIfPresent(error);
			}
		});

		// build an array with models present if already selected and check if default exists
		IPath path = getContainerFullPath();
		if(path != null){
			modelsPresent = getModelsPresent(path);
			setErrorMessageIfPresent(error);
		}	
	}




	public String getModelName() {
		return modelNameFieldInput.getText();
	}

	@Override
	protected void initialPopulateContainerNameField() {
		// TODO Auto-generated method stub
		super.initialPopulateContainerNameField();
	}


	@Override
	public void handleEvent(Event event) { 
		// if selection changes check models present
		if(event.type==SWT.Selection){
			logger.debug("selection changed: calculate models inside folder");
			IPath path = getContainerFullPath();
			if(path != null){
				modelsPresent = getModelsPresent(path);
				setErrorMessageIfPresent(error);
			}				
			logger.debug("Found models in number. "+modelsPresent.size());
		}

		super.handleEvent(event);
	}


	public void setErrorMessageIfPresent(Label error){
		if(modelNameFieldInput!= null){
			String modelname = modelNameFieldInput.getText();
			if(modelsPresent!=null && modelname != null && !modelname.equals("")){
				if(modelsPresent.keySet().contains(modelname.toUpperCase())){
					logger.warn("model name already defined in file "+modelsPresent.get(modelname.toUpperCase())+": a change is suggested to avoid name collision");
					error.setText("model name already defined in file "+modelsPresent.get(modelname.toUpperCase())+": a change is suggested to avoid name collision");
					error.setVisible(true);
					error.redraw();
				}
				else{
					error.setVisible(false);
					error.redraw();
				}
			}	
		}
	}



	/** get all models present in selected folder
	 * 
	 * @return
	 */

	public Map<String,String> getModelsPresent(IPath path){
		logger.debug("IN");
		FileInputStream fio = null;
		Map<String,String> models = new HashMap<String,String>();
		try{
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot wsroot = workspace.getRoot();
			IPath location = wsroot.getLocation();
			IPath fullPath = location.append(path);
			java.io.File dir = fullPath.toFile(); 

			java.io.File[] listOfFiles = dir.listFiles();
			EmfXmiSerializer emfXmiSerializer = new EmfXmiSerializer();

			for (int i = 0; i < listOfFiles.length; i++){
				if(listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("sbimodel")){
					java.io.File f = listOfFiles[i];		
					fio = new FileInputStream(f);

					Model root = emfXmiSerializer.deserialize(fio);						
					logger.debug("Model root is [{}] ",root );
					BusinessModel businessModel = root.getBusinessModels().get(0);
					if(businessModel != null){
						logger.debug("found model " + businessModel.getName());
						models.put(businessModel.getName().toUpperCase(), f.getName());
					}

				}
			}

		}
		catch (Exception e) {
			logger.error("error in retrieving other models, go ahead as default case");
		}
		finally{
			if(fio!= null)
				try {
					fio.close();
				} catch (IOException e) {
					logger.error("error in closing the stream");				
				}
		}
		logger.debug("OUT");
		return models;		 
	}

}
