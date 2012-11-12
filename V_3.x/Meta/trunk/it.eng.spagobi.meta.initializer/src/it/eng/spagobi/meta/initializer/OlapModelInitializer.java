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
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.olap.Cube;
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
	
	public Cube addCube(OlapModel olapModel,BusinessColumnSet businessColumnSet){
		Cube cube;
		try {
			cube = FACTORY.createCube();
			cube.setModel(olapModel);
			cube.setTable(businessColumnSet);
			cube.setName(businessColumnSet.getName());	
			olapModel.getCubes().add(cube);

			getPropertiesInitializer().addProperties(cube);	
			
			//TODO: to remove
			System.out.println("Cube created: "+cube+" for "+businessColumnSet);
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to add cube ");
		}
		return cube;
		
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
