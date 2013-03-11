/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.test.mapper;


import it.eng.spagobi.meta.mapper.cwm.jmi.SpagoBICWMJMIImpl;
import it.eng.spagobi.meta.mapper.cwm.jmi.SpagoBICWMMapperJMIImpl;
import it.eng.spagobi.meta.model.physical.PhysicalModel;


/**
 * @author Andrea Gioia (andrea.gioi@eng.it)
 *
 */
public class CWMMapperDecodeTest {
	
	public static String FILENAME = "D:\\Documenti\\Progetti\\metadati\\workspace\\SpagoBIMetaData\\model.xml";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		SpagoBICWMJMIImpl cwm;
		SpagoBICWMMapperJMIImpl modelMapper;
		PhysicalModel model;
		
        // xmi -> cwm (jmi) -> spagobi model
        
        cwm = new SpagoBICWMJMIImpl("modeldemo");
        cwm.importFromXMI(FILENAME);
        
        modelMapper = new SpagoBICWMMapperJMIImpl();
        model = modelMapper.decodeModel(cwm);
        
        System.out.println("Model name: " + model.getName());
        System.out.println("Model catalog: " + model.getCatalog());
	}

}
