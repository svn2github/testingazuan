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
package it.eng.spagobi.engines.talend;



import java.io.File;

import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBITalendEngine implements Version {
	
	private SpagoBITalendEngineConfig config;
	private RuntimeRepository runtimeRepository;
	
	static private SpagoBITalendEngine instance;
	
	static public SpagoBITalendEngine getInstance() {
		if(instance == null) instance = new SpagoBITalendEngine();
		return instance;
	}
	
	private SpagoBITalendEngine() { 
		config = new SpagoBITalendEngineConfig();
	}
	
	public static String getVersion() {
		return MAJOR + "." + MINOR + "." + REVISION;
	}
	    
	public static String getFullName() {
		return ENGINE_NAME + "-" + getVersion();
	}
	    
	public static String getInfo() {
		return getFullName() + " [ " + WEB +" ]";
	}

	public RuntimeRepository getRuntimeRepository() {
		if(runtimeRepository == null) {
			File rrRootDir = config.getRuntimeRepositoryRootDir();
			runtimeRepository = new RuntimeRepository(rrRootDir);
		}
		return runtimeRepository;
	}

	public SpagoBITalendEngineConfig getConfig() {
		return config;
	}

	public static String getComplianceVersion() {
		return CLIENT_COMPLIANCE_VERSION;
	}
	
	
}
