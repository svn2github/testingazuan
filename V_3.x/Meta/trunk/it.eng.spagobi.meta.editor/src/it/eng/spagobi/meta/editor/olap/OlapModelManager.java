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
package it.eng.spagobi.meta.editor.olap;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.model.SortBusinessModelTablesCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.commands.edit.cube.CreateCubeCommand;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class OlapModelManager {

	public OlapModelInitializer olapModelInitializer = new OlapModelInitializer();
	public EditingDomain editingDomain;
	
	private static OlapModelManager istanza;
	
    // Suppress default constructor for noninstantiability
	private OlapModelManager(){
      
	}
	
	public static OlapModelManager getInstance()
	{
		if (istanza == null)
		{
			istanza = new OlapModelManager();
		}

		return istanza;
	}
	
	/*
	 * Check if the selected Business Object on the Business Model could be related to the Olap Model, then
	 * check propertyId and value to determine if required to create Olap Objects
	 * 
	 */
	public void checkForOlapModel(EditingDomain editingDomain,Object selectedBusinessObject, Object propertyId, Object value){
		this.editingDomain = editingDomain;
		//Gestione per l'OlapModel
	    if (selectedBusinessObject instanceof BusinessTable){
		    //propertyId ="structural.tabletype", value="cube"
		    if ( (propertyId.equals("structural.tabletype")) && (value.equals("cube")) ){
		    	System.out.println("structural.tabletype cube su "+selectedBusinessObject);
		    	checkPreviousObjects((BusinessTable)selectedBusinessObject);
		    	createOlapCube((BusinessTable)selectedBusinessObject);
		    } 
		    //propertyId ="structural.tabletype", value="dimension"
		    else if ( (propertyId.equals("structural.tabletype")) && (value.equals("dimension")) ){
		    	System.out.println("structural.tabletype dimension su "+selectedBusinessObject);

		    } 
	    }
	    else if (selectedBusinessObject instanceof SimpleBusinessColumn){
		    //propertyId="structural.columntype", value="measure"
		    if ( (propertyId.equals("structural.columntype")) && (value.equals("measure")) ){
		    	System.out.println("structural.columntype measure su "+selectedBusinessObject);

		    } 
	    }
	}
	
	//check if there are previous Olap Object to remove from the model (because tabletype changed)
	private void checkPreviousObjects(BusinessTable businessTable){
		Model rootModel = businessTable.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		
		EList<Cube>cubes = olapModel.getCubes();
		for (Cube cube:cubes){
			if (cube.getTable().equals(businessTable)){
				//TODO:remove cube from the model --> da fare con command
				cube.setModel(null);
			}
		}
		
		EList<Dimension>dimensions = olapModel.getDimensions();
		for (Dimension dimension:dimensions){
			if (dimension.getTable().equals(businessTable)){
				//TODO:remove dimension from the model --> da fare con command
				dimension.setModel(null);
			}
		}
	}
	
	//Create Olap Cube via CreateCubeCommand
	private  void createOlapCube(BusinessTable businessTable){
		Model rootModel = businessTable.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		olapModelInitializer.addCube(olapModel, businessTable);

		/*
		CommandParameter commandParameter =  new CommandParameter(olapModel, null, businessTable, new ArrayList<Object>());
	    if (editingDomain != null) {	    	
	    	editingDomain.getCommandStack().execute(new CreateCubeCommand(editingDomain,commandParameter));
	    }
	    */
		
		//Stampa di prova
		EList<Cube> cubes = olapModel.getCubes();
		for (Cube element:cubes){
			System.out.println("Trovato cube: "+element);
			System.out.println("Radice cube: "+element.getModel().getParentModel());
		}
		
		
		
	}
	
	private void checkPreviousObjects(SimpleBusinessColumn businessTable){
		
	}
}
