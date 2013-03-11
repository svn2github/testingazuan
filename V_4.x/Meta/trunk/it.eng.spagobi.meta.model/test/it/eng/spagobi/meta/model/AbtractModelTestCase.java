/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model;

import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.serializer.IModelSerializer;
import junit.framework.TestCase;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbtractModelTestCase extends TestCase {
	
	protected ClassLoader classLoader;
	protected IModelSerializer serializer;
	protected String modelName;
	protected Model model;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		if(modelName == null) modelName = "My Model";
		setUpClassLoader();
		setUpSerializer();
		System.err.println("-- SET-UP --------------------------------");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSerializer();
		tearDownClassLoader();
		
		System.err.println("-- TEAR-DOWN --------------------------------");
	}
	

	
	protected void setUpSerializer() {
		serializer = new EmfXmiSerializer();	
	}
	
	protected void tearDownSerializer() {
		serializer = null;
	}
	
	protected void setUpClassLoader() {
		classLoader = Thread.currentThread().getContextClassLoader();
	}
	
	protected void tearDownClassLoader() {
		Thread.currentThread().setContextClassLoader(classLoader);
	}

}
