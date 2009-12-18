/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.utility;

import groovy.util.GroovyScriptEngine;

// TODO: Auto-generated Javadoc
/**
 * The Class GroovyEngine.
 */
public class GroovyEngine {
	
	 	/** The singleton groovy engine. */
	 	private static GroovyEngine singletonGroovyEngine = new GroovyEngine();
	 	
	 	/** The groovy script engine. */
	 	private GroovyScriptEngine groovyScriptEngine = null;
	 	
	    /**
    	 * Instantiates a new groovy engine.
    	 */
    	private GroovyEngine(){
	    	// Cerca gli script groovy nelle directory configurate dall'utente
	    	try{
	    		
	    		String [] roots = new String[1];
	    		String groovyScriptDir = FileUtils.getQbeScriptDir(null);
	    		roots[0] = groovyScriptDir;
	    		
	    		groovyScriptEngine = new GroovyScriptEngine(roots);
	    	
	    	}catch (Exception e) {
			
	    		e.printStackTrace();
			
	    	}
	    }

	    /**
    	 * Gets the groovy engine.
    	 * 
    	 * @return the groovy engine
    	 */
    	public static GroovyEngine getGroovyEngine() { 
	    	return singletonGroovyEngine;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		public Object clone() throws CloneNotSupportedException {
				throw new CloneNotSupportedException(); 

		}

		/**
		 * Gets the groovy script engine.
		 * 
		 * @return the groovy script engine
		 */
		public GroovyScriptEngine getGroovyScriptEngine() {
			return groovyScriptEngine;
		}
}
