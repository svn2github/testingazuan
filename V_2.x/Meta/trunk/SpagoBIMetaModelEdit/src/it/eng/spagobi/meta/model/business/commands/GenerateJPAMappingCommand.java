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
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

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
	
	@Override
	public void execute() {
		BusinessModel businessModel;
		businessModel = (BusinessModel)parameter.getOwner();
		String directory = (String)parameter.getValue();

		//Call JPA Mapping generator
		JpaMappingGenerator generator = new JpaMappingGenerator();
		generator.generateJpaMapping(businessModel, directory);
		
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
		
		boolean result=datamartGenerator.compile();
		if (result){
			// compile OK
			datamartGenerator.jar();
			System.err.println("COMMAND [GenerateJPAMappingCommand] SUCCESFULLY EXECUTED: ");
			this.executed = true;
		}else{
			System.err.println("Compile problem!!!!!!!!!!");
			// TODO  .... gestire la visualizzazione della popup all'utente
			this.executed = false; // ????
		}

		
		
	}
	
	
	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
	
	}
	
	@Override
	public Object getImage() {
		return SpagoBIMetalModelEditPlugin.INSTANCE.getImage("full/obj16/Jpaclass");
	}
}
