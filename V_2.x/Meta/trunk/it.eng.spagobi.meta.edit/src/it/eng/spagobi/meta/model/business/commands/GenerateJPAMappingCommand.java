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
package it.eng.spagobi.meta.model.business.commands;


import java.util.Set;

import it.eng.spagobi.meta.compiler.DataMartGenerator;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author cortella
 *
 */
public class GenerateJPAMappingCommand extends AbstractSpagoBIModelCommand {

	public GenerateJPAMappingCommand(EditingDomain domain, CommandParameter parameter) {
		super("JPA Mapping", "Generate JPA Mapping", domain, parameter);
	}
	
	public GenerateJPAMappingCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	//This command can't be undo
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
		JpaMappingGenerator generator = new JpaMappingGenerator();
		try {
			generator.generateJpaMapping(businessModel, directory);
		} catch (Exception e) {
			System.err.println(">>> Error in JPAMappingGenerator: "+e);
			e.printStackTrace();
			showInformation("Error in JPAMappingGenerator","Cannot create JPA Mapping classes");
		}
		
		//Get Package Name
		String packageName = businessModel.getProperties().get(BusinessModelDefaultPropertiesInitializer.MODEL_PACKAGE).getValue();
			
		//Call Java Compiler
		DataMartGenerator datamartGenerator = new DataMartGenerator(
				directory,
				directory+"/build/",
				null,
				directory+"/dist/",
				packageName.replace(".", "/")
				);		
		
		boolean result = datamartGenerator.compile();
		if (result){
			// compile OK
			showInformation("Successfull Compilation ","JPA Source Code correctly compiled");
			
			//Generate Jar from compiled code
			datamartGenerator.jar();
			System.err.println("COMMAND [GenerateJPAMappingCommand] SUCCESFULLY EXECUTED: ");
			this.executed = true;
		}else{
			// compile error
			showInformation("Failed Compilation","Error: JPA Source Code NOT correctly compiled");
			System.err.println(">> JPA Compile problem! <<");
			this.executed = false; 
		}

	}
	
	@Override
	public Object getImage() {
		return SpagoBIMetaModelEditPlugin.INSTANCE.getImage("full/obj16/Jpaclass");
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
