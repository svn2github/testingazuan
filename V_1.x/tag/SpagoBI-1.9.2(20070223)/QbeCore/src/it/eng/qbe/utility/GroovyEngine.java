package it.eng.qbe.utility;

import it.eng.spago.configuration.ConfigSingleton;
import groovy.util.GroovyScriptEngine;

public class GroovyEngine {
	
	 	private static GroovyEngine singletonGroovyEngine = new GroovyEngine();
	 	
	 	private GroovyScriptEngine groovyScriptEngine = null;
	 	
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

	    public static GroovyEngine getGroovyEngine() { 
	    	return singletonGroovyEngine;
		}

		public Object clone() throws CloneNotSupportedException {
				throw new CloneNotSupportedException(); 

		}

		public GroovyScriptEngine getGroovyScriptEngine() {
			return groovyScriptEngine;
		}
}
