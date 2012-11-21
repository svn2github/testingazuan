/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.initializer;


import it.eng.spagobi.meta.initializer.properties.IPropertiesInitializer;
import it.eng.spagobi.meta.initializer.properties.OlapModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.initializer.properties.PhysicalModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.Measure;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.OlapModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.util.JDBCTypeMapper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.emf.common.util.EList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class OlapModelInitializer {
	
	IPropertiesInitializer propertiesInitializer;
	Model rootModel;
	private static Logger logger = LoggerFactory.getLogger(OlapModelInitializer.class);

	
	static public OlapModelFactory FACTORY = OlapModelFactory.eINSTANCE;
	
	public OlapModelInitializer() {
		setPropertiesInitializer(new OlapModelDefaultPropertiesInitializer());
		
	}
	

	
	//Initialize OlapModel
	public OlapModel initialize(String modelName) {
		OlapModel olapModel;
		
		try {
			olapModel = FACTORY.createOlapModel();
			olapModel.setName(modelName);
			
			if(getRootModel() != null) {
				olapModel.setParentModel(getRootModel());
			}
			getPropertiesInitializer().addProperties(olapModel);

		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize olap model", t);
		}
		
		return olapModel;
		
	}
	//Add a Cube to the OlapModel, with Measures if found
	public Cube addCube(OlapModel olapModel,BusinessColumnSet businessColumnSet){
		Cube cube;
		try {
			cube = FACTORY.createCube();
			cube.setModel(olapModel);
			cube.setTable(businessColumnSet);
			cube.setName(businessColumnSet.getName());	
			olapModel.getCubes().add(cube);
			
			getPropertiesInitializer().addProperties(cube);	
			
			//Scan the businessColumnSet for columns set as Measure
			EList<BusinessColumn> businessColumns = businessColumnSet.getColumns();
			for (BusinessColumn column:businessColumns){
				if (column.getProperties().get("structural.columntype").getValue().equals("measure")){
					logger.debug("Found existing measure on "+column);
					addMeasure(cube,column);
				}
			}
			
			//Check if the businessColumnSet has outbound relationships with other businessColumnSet marked as Dimension
			List<BusinessRelationship> businessRelationships = businessColumnSet.getRelationships();
			for (BusinessRelationship businessRelationship : businessRelationships){
				//check only outbound relationships
				if (businessRelationship.getSourceTable().equals(businessColumnSet)){
					BusinessColumnSet destinationTable = businessRelationship.getDestinationTable();
					if (destinationTable.getProperties().get("structural.tabletype").getValue().equals("dimension")){
						Dimension dimension = getDimension(destinationTable);
						//add a reference from the cube to the dimension
						if (dimension != null){
							cube.getDimensions().add(dimension);
						}
					}
				}
			}

			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to add cube ");
		}
		return cube;
		
	}
	
	//Add an existing Cube to the OlapModel (used typically for undo operation)
	public Cube addCube(OlapModel olapModel,Cube cube){
		try {

			olapModel.getCubes().add(cube);

			BusinessColumnSet businessColumnSet = cube.getTable();
			
			//Check if the businessColumnSet has outbound relationships with other businessColumnSet marked as Dimension
			List<BusinessRelationship> businessRelationships = businessColumnSet.getRelationships();
			for (BusinessRelationship businessRelationship : businessRelationships){
				//check only outbound relationships
				if (businessRelationship.getSourceTable().equals(businessColumnSet)){
					BusinessColumnSet destinationTable = businessRelationship.getDestinationTable();
					if (destinationTable.getProperties().get("structural.tabletype").getValue().equals("dimension")){
						Dimension dimension = getDimension(destinationTable);
						//add a reference from the cube to the dimension
						if (dimension != null){
							cube.getDimensions().add(dimension);
						}
					}
				}
			}

			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to add cube ");
		}
		return cube;
	}
	
	//Add an existing Dimension to the OlapModel, Add references to the Cubes if found (typically used for undo operation)
	public Dimension addDimension(OlapModel olapModel,Dimension dimension){
		try{

			olapModel.getDimensions().add(dimension);
			BusinessColumnSet businessColumnSet = dimension.getTable();


			//Check if the businessColumnSet has inbound relationships from other businessColumnSet marked as Cube
			List<BusinessRelationship> businessRelationships = businessColumnSet.getRelationships();
			for (BusinessRelationship businessRelationship : businessRelationships){
				//check only inbound relationships
				if (businessRelationship.getDestinationTable().equals(businessColumnSet)){
					BusinessColumnSet sourceTable = businessRelationship.getSourceTable();
					if (sourceTable.getProperties().get("structural.tabletype").getValue().equals("cube")){
						Cube cube = getCube(sourceTable);
						//add a reference from the existing cube to the new dimension
						if (cube != null){
							cube.getDimensions().add(dimension);
						}
					}
				}
			}			
			
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to add dimension ");
		}
		return dimension;
	}
	
	//Add a Dimension to the OlapModel, Add references to the Cubes if found
	public Dimension addDimension(OlapModel olapModel,BusinessColumnSet businessColumnSet){
		Dimension dimension;
		try{
			dimension = FACTORY.createDimension();
			dimension.setModel(olapModel);
			dimension.setTable(businessColumnSet);
			dimension.setName(businessColumnSet.getName());	
			olapModel.getDimensions().add(dimension);

			getPropertiesInitializer().addProperties(dimension);	
			
			//Check if the businessColumnSet has inbound relationships from other businessColumnSet marked as Cube
			List<BusinessRelationship> businessRelationships = businessColumnSet.getRelationships();
			for (BusinessRelationship businessRelationship : businessRelationships){
				//check only inbound relationships
				if (businessRelationship.getDestinationTable().equals(businessColumnSet)){
					BusinessColumnSet sourceTable = businessRelationship.getSourceTable();
					if (sourceTable.getProperties().get("structural.tabletype").getValue().equals("cube")){
						Cube cube = getCube(sourceTable);
						//add a reference from the existing cube to the new dimension
						if (cube != null){
							cube.getDimensions().add(dimension);
						}
					}
				}
			}			
			
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to add dimension ");
		}
		return dimension;
	}
	
	//Add a new Measure to the Cube 
	public Measure addMeasure(Cube cube, BusinessColumn businessColumn){
		Measure measure;
		try{
			measure = FACTORY.createMeasure();
			measure.setCube(cube);
			measure.setColumn(businessColumn);
			measure.setName(businessColumn.getName());
			cube.getMeasures().add(measure);
			
			getPropertiesInitializer().addProperties(measure);	

			
		}catch(Throwable t) {
			throw new RuntimeException("Impossible to add measure ");
		}
		return measure;
	}
	
	//Remove Cube or Dimension corresponding to the passed businessColumnSet
	public Object removeCorrespondingOlapObject(BusinessColumnSet businessColumnSet){
		Model rootModel = businessColumnSet.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		Dimension dimensionToRemove = null;
		
		//----------------------------------------------------------
		//	remove Cube from the model 
		//----------------------------------------------------------
		
		EList<Cube>cubes = olapModel.getCubes();
		boolean foundCube=false;
		int i=0;
		for (Cube cube:cubes){
			if (cube.getTable().equals(businessColumnSet)){			
				foundCube = true;
				break;
			}
			i++;
		}
		if (foundCube){
			return olapModel.getCubes().remove(i);
		}
		//----------------------------------------------------------

		//----------------------------------------------------------
		//	remove Dimension from the model 
		//----------------------------------------------------------

		EList<Dimension>dimensions = olapModel.getDimensions();
		boolean foundDimension=false;
		i=0;
		for (Dimension dimension:dimensions){
			if (dimension.getTable().equals(businessColumnSet)){
				foundDimension = true;
				dimensionToRemove = dimension;
				break;				
			}
			i++;
		}
		if (foundDimension){
			
			//Check if the businessColumnSet has inbound relationships from other businessColumnSet marked as Cube
			List<BusinessRelationship> businessRelationships = businessColumnSet.getRelationships();
			for (BusinessRelationship businessRelationship : businessRelationships){
				//check only inbound relationships
				if (businessRelationship.getDestinationTable().equals(businessColumnSet)){
					BusinessColumnSet sourceTable = businessRelationship.getSourceTable();
					if (sourceTable.getProperties().get("structural.tabletype").getValue().equals("cube")){
						Cube cube = getCube(sourceTable);
						//remove reference from the existing cube to the dimension to delete
						if (cube != null){
							if (dimensionToRemove != null){
								cube.getDimensions().remove(dimensionToRemove);
							}
						}
					}
				}
			}		
			//remove the dimension from the Olap Model
			return olapModel.getDimensions().remove(i);
		}
		//----------------------------------------------------------
		return null;

	}
	
	//Remove a Dimension from the model with all the correct checks
	public void removeDimension(OlapModel olapModel, Dimension dimension){
		BusinessColumnSet businessColumnSet = dimension.getTable();
		

		//Check if the businessColumnSet has inbound relationships from other businessColumnSet marked as Cube
		List<BusinessRelationship> businessRelationships = businessColumnSet.getRelationships();
		for (BusinessRelationship businessRelationship : businessRelationships){
			//check only inbound relationships
			if (businessRelationship.getDestinationTable().equals(businessColumnSet)){
				BusinessColumnSet sourceTable = businessRelationship.getSourceTable();
				if (sourceTable.getProperties().get("structural.tabletype").getValue().equals("cube")){
					Cube cube = getCube(sourceTable);
					//remove reference from the existing cube to the dimension to delete
					if (cube != null){
						cube.getDimensions().remove(dimension);
					}
				}
			}
		}		
		//remove the dimension from the Olap Model
		olapModel.getDimensions().remove(dimension);
	}
	
	//Remove Measure corresponding to the passed businessColumn
	public Measure removeCorrespondingOlapObject(BusinessColumn businessColumn, Cube cube){
		OlapModel olapModel = cube.getModel();
		
		//remove measure from the model 
		EList<Measure> measures = cube.getMeasures();
		boolean foundMeasure=false;
		int i=0;
		for (Measure measure:measures){
			if (measure.getColumn().equals(businessColumn)){			
				foundMeasure = true;
				break;
			}
			i++;
		}
		if (foundMeasure){
			Measure removedMeasure = cube.getMeasures().remove(i);
			return removedMeasure;
		}
		return null;
	}

	
	//NOTE: This function is correct if there is only one cube for a specific businessColumnSet
	//return the Cube corresponding to the businessColumnSet (if any)
	public Cube getCube(BusinessColumnSet businessColumnSet){
		Model rootModel = businessColumnSet.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		
		EList<Cube>cubes = olapModel.getCubes();
		for (Cube cube:cubes){
			if (cube.getTable().equals(businessColumnSet)){			
				return cube;
			}
		}
		//no corresponding cube found
		return null;		
	}

	//NOTE: This function is correct if there is only one Dimension for a specific businessColumnSet
	//return the Dimension corresponding to the businessColumnSet (if any)
	public Dimension getDimension(BusinessColumnSet businessColumnSet){
		Model rootModel = businessColumnSet.getModel().getParentModel();
		OlapModel olapModel = rootModel.getOlapModels().get(0);
		
		EList<Dimension>dimensions = olapModel.getDimensions();
		for (Dimension dimension:dimensions){
			if (dimension.getTable().equals(businessColumnSet)){			
				return dimension;
			}
		}
		//no corresponding dimension found
		return null;		
	}
	

	
	//  --------------------------------------------------------
	//	Accessor methods
	//  --------------------------------------------------------

	public IPropertiesInitializer getPropertiesInitializer() {
		return propertiesInitializer;
	}

	public void setPropertiesInitializer(IPropertiesInitializer propertyInitializer) {
		this.propertiesInitializer = propertyInitializer;
	}

	public Model getRootModel() {
		return rootModel;
	}

	public void setRootModel(Model rootModel) {
		this.rootModel = rootModel;
	}
	
	
	//  --------------------------------------------------------
	//	Static methods
	//  --------------------------------------------------------
	
	private static void log(String msg) {
		//System.out.println(msg);
	}

	
	
}
