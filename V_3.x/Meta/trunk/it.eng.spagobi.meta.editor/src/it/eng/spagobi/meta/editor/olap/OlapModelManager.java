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
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.model.SortBusinessModelTablesCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.commands.edit.cube.CreateCubeCommand;
import it.eng.spagobi.meta.model.olap.commands.edit.cube.CreateMeasureCommand;
import it.eng.spagobi.meta.model.olap.commands.edit.cube.RemoveMeasureCommand;
import it.eng.spagobi.meta.model.olap.commands.edit.dimension.CreateDimensionCommand;
import it.eng.spagobi.meta.model.olap.commands.edit.generic.SetGenericCommand;

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
	public void checkForOlapModel(EditingDomain editingDomain,Object selectedBusinessObject, Object propertyId, Object value, String previousValue){
		this.editingDomain = editingDomain;
		//Gestione per l'OlapModel
	    if (selectedBusinessObject instanceof BusinessTable){
		    //propertyId ="structural.tabletype", value="cube"
		    if ( (propertyId.equals("structural.tabletype")) && (value.equals("cube")) ){
		    	//System.out.println("structural.tabletype cube su "+selectedBusinessObject);
		    	//removePreviousObjects((BusinessColumnSet)selectedBusinessObject);
		    	createOlapCube((BusinessColumnSet)selectedBusinessObject,previousValue);
		    } 
		    //propertyId ="structural.tabletype", value="dimension"
		    else if ( (propertyId.equals("structural.tabletype")) && (value.equals("dimension")) ){
		    	//System.out.println("structural.tabletype dimension su "+selectedBusinessObject);
		    	//removePreviousObjects((BusinessColumnSet)selectedBusinessObject);
		    	createOlapDimension((BusinessColumnSet)selectedBusinessObject,previousValue);
		    } 
		    //propertyId ="structural.tabletype", value="generic"
		    else if ( (propertyId.equals("structural.tabletype")) && (value.equals("generic")) ){
		    	//System.out.println("structural.tabletype generic su "+selectedBusinessObject);
		    	//removePreviousObjects((BusinessColumnSet)selectedBusinessObject);
		    	setGenericTable((BusinessColumnSet)selectedBusinessObject,previousValue);
		    } 
	    }
	    else if (selectedBusinessObject instanceof SimpleBusinessColumn){
		    //propertyId="structural.columntype", value="measure"
		    if ( (propertyId.equals("structural.columntype")) && (value.equals("measure")) ){
		    	//System.out.println("structural.columntype measure su "+selectedBusinessObject);
		    	//search if BusinessColumnSet of SimpleBusinessColumn is a cube
		    	Cube cube = checkIfInsideCube((SimpleBusinessColumn)selectedBusinessObject);
		    	if (cube != null){
			    	createOlapMeasure(cube,(SimpleBusinessColumn)selectedBusinessObject,previousValue);
		    	}
		    	
		    } else if ( (propertyId.equals("structural.columntype")) && (value.equals("attribute")) ){
			    //propertyId="structural.columntype", value="attribute"
		    	//System.out.println("structural.columntype attribute su "+selectedBusinessObject);
		    	Cube cube = checkIfInsideCube((SimpleBusinessColumn)selectedBusinessObject);
		    	if (cube != null){
			    	//removePreviousObjects((SimpleBusinessColumn)selectedBusinessObject,cube);
		    		setAttribute(cube,(SimpleBusinessColumn)selectedBusinessObject,previousValue);
		    	}
		    } 
	    }
	}
	
	private Cube checkIfInsideCube(SimpleBusinessColumn simpleBusinessColumn){
		return olapModelInitializer.getCube(simpleBusinessColumn.getTable());
	}
	
	//check if there are previous Olap Object (corresponding to this BusinessTable) to remove from the model (because tabletype changed)
	private void removePreviousObjects(BusinessColumnSet businessColumnSet){
		olapModelInitializer.removeCorrespondingOlapObject(businessColumnSet);
	}
	
	
	//Create Olap Cube via CreateCubeCommand
	private  void createOlapCube(BusinessColumnSet businessColumnSet,String previousValue){
		Model rootModel = businessColumnSet.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		
		CommandParameter commandParameter =  new CommandParameter(olapModel, previousValue, businessColumnSet, new ArrayList<Object>());
	    if (editingDomain != null) {	    	
	    	editingDomain.getCommandStack().execute(new CreateCubeCommand(editingDomain,commandParameter));
	    }
	}
	
	//Create Olap Dimension via CreateDimensionCommand
	private void createOlapDimension(BusinessColumnSet businessColumnSet, String previousValue){
		Model rootModel = businessColumnSet.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		
		CommandParameter commandParameter =  new CommandParameter(olapModel, previousValue, businessColumnSet, new ArrayList<Object>());
	    if (editingDomain != null) {	    	
	    	editingDomain.getCommandStack().execute(new CreateDimensionCommand(editingDomain,commandParameter));
	    }	   
	}
	//Erase Olap Objects and reset type to generic via SetGenericCommand
	private void setGenericTable(BusinessColumnSet businessColumnSet, String previousValue){
		Model rootModel = businessColumnSet.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		
		CommandParameter commandParameter =  new CommandParameter(olapModel,previousValue, businessColumnSet, new ArrayList<Object>());
	    if (editingDomain != null) {	    	
	    	editingDomain.getCommandStack().execute(new SetGenericCommand(editingDomain,commandParameter));
	    }	  		
	}

	
	//Create Olap Measure via CreateMeasureCommand
	private void createOlapMeasure(Cube cube, SimpleBusinessColumn simpleBusinessColumn, String previousValue){
		
		CommandParameter commandParameter =  new CommandParameter(cube, previousValue, simpleBusinessColumn, new ArrayList<Object>());
	    if (editingDomain != null) {	    	
	    	editingDomain.getCommandStack().execute(new CreateMeasureCommand(editingDomain,commandParameter));
	    }	   
	}	
	
	//Set column as Attribute via RemoveMeasureCommand
	private void setAttribute(Cube cube, SimpleBusinessColumn simpleBusinessColumn,String previousValue){
		
		CommandParameter commandParameter =  new CommandParameter(cube, previousValue, simpleBusinessColumn, new ArrayList<Object>());
	    if (editingDomain != null) {	    	
	    	editingDomain.getCommandStack().execute(new RemoveMeasureCommand(editingDomain,commandParameter));
	    }	   
	}		
	
	//check if there are previous Olap Object (corresponding to this BusinessColumn) to remove from the model (because columntype changed)
	private void removePreviousObjects(SimpleBusinessColumn businessColumn, Cube cube){
		olapModelInitializer.removeCorrespondingOlapObject(businessColumn,cube);
	}
}
