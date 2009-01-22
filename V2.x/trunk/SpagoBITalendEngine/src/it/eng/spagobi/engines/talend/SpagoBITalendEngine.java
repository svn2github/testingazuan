/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
**/
package it.eng.spagobi.engines.talend;



import java.io.File;

import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBITalendEngine implements Version {
	
	
	private RuntimeRepository runtimeRepository;
	
	static private SpagoBITalendEngine instance;
	
	/**
	 * Gets the single instance of SpagoBITalendEngine.
	 * 
	 * @return single instance of SpagoBITalendEngine
	 */
	static public SpagoBITalendEngine getInstance() {
		if(instance == null) instance = new SpagoBITalendEngine();
		return instance;
	}
	
	private SpagoBITalendEngine() { 
	}
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public static String getVersion() {
		return MAJOR + "." + MINOR + "." + REVISION;
	}
	    
	/**
	 * Gets the full name.
	 * 
	 * @return the full name
	 */
	public static String getFullName() {
		return ENGINE_NAME + "-" + getVersion();
	}
	    
	/**
	 * Gets the info.
	 * 
	 * @return the info
	 */
	public static String getInfo() {
		return getFullName() + " [ " + WEB +" ]";
	}

	/**
	 * Gets the runtime repository.
	 * 
	 * @return the runtime repository
	 */
	public RuntimeRepository getRuntimeRepository() {
		if(runtimeRepository == null) {
			File rrRootDir = TalendEngineConfig.getInstance().getRuntimeRepositoryRootDir();
			runtimeRepository = new RuntimeRepository(rrRootDir);
		}
		return runtimeRepository;
	}

	
	/**
	 * Gets the compliance version.
	 * 
	 * @return the compliance version
	 */
	public static String getComplianceVersion() {
		return CLIENT_COMPLIANCE_VERSION;
	}
	
	
}
