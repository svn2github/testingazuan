/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.generator.jpamapping.views;

import it.eng.spagobi.meta.generator.jpamapping.AbstractJpaMappingGeneratorTestCase;
import it.eng.spagobi.meta.generator.utils.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractJpaMappingViewsGenertorTestCase extends AbstractJpaMappingGeneratorTestCase{
	public static final File OUTPUT_FOLDER = new File(AbstractJpaMappingGeneratorTestCase.OUTPUT_FOLDER, "views");

	public void doTest() {
		super.doTest();
		doTestViewFileJSONContent();
		doTestPersistenceUnitContent();
	}
	
	public void doTestViewFileJSONContent()  {
		
		try {
			File viewFile = new File(jarGenerator.getSrcDir(), "views.json");
			String viewFileContent = StringUtils.getStringFromFile(viewFile);
			JSONObject viewJSON = new JSONObject( viewFileContent );
				
			JSONArray views = viewJSON.optJSONArray("views");
			assertNotNull("Impossible lo read views configurations", views);
			assertEquals("Model contains a number of view different from 1", 1, views.length());
			JSONObject view = (JSONObject)views.get(0);
			assertNotNull("View name not defined", view.optString("name"));
			assertEquals("First view name is wrong", "EmployeeClosure", view.optString("name"));
				
			JSONArray tables = view.optJSONArray("tables");
			assertNotNull("View does not contains any table", tables);
			assertEquals("The number of tables contained in the view is different from 2", 2, tables.length());
				
			JSONArray joins = view.optJSONArray("joins");
			assertNotNull("View does not contains any table", joins);
			assertEquals("The number of joins contained in the view is different from 2", tables.length()-1, joins.length());
		} catch(Throwable t) {
			t.printStackTrace();
			fail();
		}
		
	}
	
	public void doTestPersistenceUnitContent()  {
		String fileContents;
		
		fileContents = null;
		try {
			JarFile jarFile = new JarFile( jarGenerator.getJarFile() );
			JarEntry entry = jarFile.getJarEntry("META-INF/persistence.xml");
			InputStream in = jarFile.getInputStream(entry);
			fileContents = StringUtils.getStringFromStream(in);
			in.close();
		} catch(Throwable t) {
			t.printStackTrace();
			fail();
		}
		
		assertNotNull(fileContents);
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.Employee</class>"));
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.Position</class>"));
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.EmployeeClosureEmployee</class>"));
		assertTrue(fileContents.contains("<class>it.eng.spagobi.meta.EmployeeClosurePosition</class>"));
	}
}
