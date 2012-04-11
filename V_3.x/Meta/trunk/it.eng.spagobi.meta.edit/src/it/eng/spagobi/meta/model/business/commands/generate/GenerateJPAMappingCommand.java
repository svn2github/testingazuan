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


import it.eng.spagobi.meta.generator.GeneratorDescriptor;
import it.eng.spagobi.meta.generator.GeneratorFactory;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.edit.table.ModifyBusinessTableColumnsCommand;

import java.io.File;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class GenerateJPAMappingCommand extends AbstractSpagoBIModelGenerateCommand {

	private static Logger logger = LoggerFactory.getLogger(GenerateJPAMappingCommand.class);
	
	
	public GenerateJPAMappingCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.generatemapping.label"
			 , "model.business.commands.generatemapping.description"
			 , "model.business.commands.generatemapping"
			 , domain, parameter);
	}
	
	public GenerateJPAMappingCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	//This command can't be undone
	@Override
	public boolean canUndo() {
		return false;
	}
	
	@Override
	public void execute() {
		
		BusinessModel businessModel;
		businessModel = (BusinessModel)parameter.getOwner();
		String directory = (String)parameter.getValue();

		//Call JPA Mapping generator
		executed = true;
		GeneratorDescriptor descriptor = GeneratorFactory.getGeneratorDescriptorById("it.eng.spagobi.meta.generator.jpamapping");
		JpaMappingJarGenerator generator = null;
		try {
			generator =(JpaMappingJarGenerator)descriptor.getGenerator();
			generator.setLibDir(new File("plugins"));
			generator.generate(businessModel, directory);
		} catch (Exception e) {
			logger.error("An error occurred while executing command [{}]:", ModifyBusinessTableColumnsCommand.class.getName(), e);
			showInformation("Error in JPAMappingGenerator","Cannot create JPA Mapping classes");
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
			//showInformation("Successfull Compilation", "JPA Source Code correctly compiled");
			logger.debug("Command [{}] executed succesfully", ModifyBusinessTableColumnsCommand.class.getName());
		} else {
			showInformation("Failed Compilation","Error: JPA Source Code NOT correctly compiled");
			logger.debug("Command [{}] not executed succesfully", ModifyBusinessTableColumnsCommand.class.getName());
		}

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
