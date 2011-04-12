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
package it.eng.spagobi.meta.generator.jpamapping.views;

import java.io.File;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.spagobi.meta.generator.jpamapping.AbstractJpaMappingGeneratorTestCase;
import it.eng.spagobi.meta.generator.utils.StringUtils;

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
