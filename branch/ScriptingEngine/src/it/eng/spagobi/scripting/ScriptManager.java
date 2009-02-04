/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.scripting;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;

import java.io.InputStream;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ScriptManager {
	
	private GroovyClassLoader classLoader;
	
	static ScriptManager instance;
	
	public static ScriptManager getInstance() {
		if(instance == null) {
			instance = new ScriptManager();
		}
		return instance;
	}
	
	private ScriptManager() {
		classLoader = new GroovyClassLoader();
	}
	
	public Object evaluate(String script, Binding binding) {
		GroovyShell shell = new GroovyShell(binding);
		return shell.evaluate( script );
	}
	
	public Object loadAsObject(InputStream  straem) throws InstantiationException, IllegalAccessException {
		
		Class clazz = classLoader.parseClass(straem);
		Object aScript = clazz.newInstance();
		
		return aScript;
	}
	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		InputStream  stream;
		stream = ScriptManager.class.getResourceAsStream("script.groovy");
		ITestInterface o = (ITestInterface)ScriptManager.getInstance().loadAsObject(stream);
		System.out.println(o.getMessage());
		
		Binding binding = new Binding();
		binding.setVariable("a", new Integer(22));

		System.out.println(ScriptManager.getInstance().evaluate("a * 10", binding));
	}
}
