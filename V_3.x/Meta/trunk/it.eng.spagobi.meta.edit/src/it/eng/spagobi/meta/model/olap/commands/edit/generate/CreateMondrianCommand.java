/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.olap.commands.edit.generate;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.generator.GeneratorDescriptor;
import it.eng.spagobi.meta.generator.GeneratorFactory;
import it.eng.spagobi.meta.generator.mondrianschema.MondrianSchemaGenerator;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.generate.AbstractSpagoBIModelGenerateCommand;
import it.eng.spagobi.meta.model.olap.OlapModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @authors cortella, gioia
 *
 */
public class CreateMondrianCommand extends AbstractSpagoBIModelGenerateCommand {

	
	private static Logger logger = LoggerFactory.getLogger(CreateMondrianCommand.class);
	
	public CreateMondrianCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.olap.commands.createmondrian.label"
			 , "model.olap.commands.createmondrian.description"
			 , "model.olap.commands.createmondrian"
			 , domain, parameter);
	}
	
	public CreateMondrianCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {		
		logger.debug("Executing CreateMondrianCommand");
		String mondrianFilePath;
		OlapModel olapModel;
		Writer out;
		
		mondrianFilePath = (String)parameter.getValue();	
		logger.debug("Mondrian path is [{}]", mondrianFilePath);
		//Get corresponding IFile
		//IWorkspace workspace= ResourcesPlugin.getWorkspace();    
		//IPath location= Path.fromOSString(mondrianFilePath); 
		//IFile ifile= workspace.getRoot().getFileForLocation(location);
		
		olapModel = (OlapModel)parameter.getOwner();

		//Call Mondrian Schema generator
		executed = true;
		GeneratorDescriptor descriptor = GeneratorFactory.getGeneratorDescriptorById("it.eng.spagobi.meta.generator.mondrianschema");
		MondrianSchemaGenerator generator = null;
		
		try {
			generator =(MondrianSchemaGenerator)descriptor.getGenerator();
			//generator.setLibDir(new File("plugins"));
			generator.generate(olapModel, mondrianFilePath);
		} catch (Exception e) {
			logger.error("An error occurred while executing Mondrian Template Generator [{}]:", e);
			showInformation("Error in Mondrian Template Generator","Cannot create Mondrian template");
			executed = false;
		}
		finally {
			// finally block to hide technical folders created during generation
			if(generator != null){
				logger.debug("hide techical folders");
				generator.hideTechnicalResources();
			}
		}


		if(executed) {
			logger.debug("Mondrian Schema Generation executed succesfully");
		} else {
			showInformation("Failed Mondrian Template Generation","Error: Mondrian Template not created");
			logger.debug("Mondrian Schema Generation not executed succesfully");
		}
		
	}
	/*
	protected void initMondrianFileContents() {
		String mondrianPath;
		OlapModel olapModel;
		Writer out;
		
		mondrianPath = (String)parameter.getValue();	
		logger.debug("Mondrian path is [{}]", mondrianPath);
		//Get corresponding IFile
		IWorkspace workspace= ResourcesPlugin.getWorkspace();    
		IPath location= Path.fromOSString(mondrianPath); 
		IFile ifile= workspace.getRoot().getFileForLocation(location);

		
		olapModel = (BusinessModel)parameter.getOwner();
		
		mondrianFile = new File(queryPath);
		ModelProperty property = businessModel.getProperties().get("structural.file");
		String modelPath = property != null? property.getValue(): "???";
		logger.debug("Model path is [{}]",modelPath);
		
		if(mondrianFile.exists()){
			out = null;
			JSONObject o;
			String queryContent ;
			try {
				o = new JSONObject(); 
				JSONObject queryMeta = new JSONObject(); 
				o.put("queryMeta", queryMeta);
				queryMeta.put("modelPath",modelPath);
				queryContent = o.toString(3);
			} catch (JSONException e) {
				throw new SpagoBIPluginException("Impossibile to create JSON Metadata ",e);
			}
			try {
				//queryFile.createNewFile();
				FileWriter fstream = new FileWriter(queryFile);
				out = new BufferedWriter(fstream);
				//businessModel = (BusinessModel)parameter.getOwner();
				out.write(queryContent);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(out != null) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}		
			
		}
	}
	*/

	
	//This command can't be undone
	@Override
	public boolean canUndo() {
		return false;
	}
	
	/**
	 * Show an information dialog box.
	 */
	public void showInformation(final String title, final String message) {
	  Display.getDefault().asyncExec(new Runnable() {
	    @Override
	    public void run() {
	      MessageDialog.openInformation(null, title, message);
	    }
	  });
	}	
}
