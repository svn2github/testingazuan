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
package it.eng.spagobi.meta.model.business.commands.generate;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.initializer.properties.BusinessModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessModel;

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
public class CreateQueryCommand extends AbstractSpagoBIModelGenerateCommand {

	File queryFile;
	
	private static Logger logger = LoggerFactory.getLogger(CreateQueryCommand.class);
	
	public CreateQueryCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.createquery.label"
			 , "model.business.commands.createquery.description"
			 , "model.business.commands.createquery"
			 , domain, parameter);
	}
	
	public CreateQueryCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {		
		logger.debug("Executing CreateQueryCommand");
		initQueryFileContents();
		openQueryEditor();		
	}
	
	protected void initQueryFileContents() {
		String queryPath;
		BusinessModel businessModel;
		Writer out;
		
		queryPath = (String)parameter.getValue();	
		logger.debug("Query path is [{}]", queryPath);
		//queryFile = new File(directory,"query.metaquery");
		//Get corresponding IFile
		IWorkspace workspace= ResourcesPlugin.getWorkspace();    
		IPath location= Path.fromOSString(queryPath); 
		IFile ifile= workspace.getRoot().getFileForLocation(location);

		
		businessModel = (BusinessModel)parameter.getOwner();
		
		queryFile = new File(queryPath);
		ModelProperty property = businessModel.getProperties().get("structural.file");
		String modelPath = property != null? property.getValue(): "???";
		logger.debug("Model path is [{}]",modelPath);
		
		if(queryFile.exists()){
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
	
	protected void openQueryEditor() {
		if (queryFile.exists() && queryFile.isFile()) {
		    IFileStore fileStore = EFS.getLocalFileSystem().getStore(queryFile.toURI());
		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		    
		    //Force Workspace refresh
			IWorkspace workspace= ResourcesPlugin.getWorkspace();    
			IPath location= Path.fromOSString(queryFile.getAbsolutePath()); 
			IFile ifile= workspace.getRoot().getFileForLocation(location);
	        
			try {
	        	ifile.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				logger.error("Refresh Local workspace error [{}]",e);
				e.printStackTrace();
			}
			
			//*******
			
		    try {
		        IDE.openEditorOnFileStore( page, fileStore );
		    } catch ( PartInitException e ) {
		       logger.error("Error Opening Query Editor [{}]",e);
		    	e.printStackTrace();
		    }
		}
	}
	
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
