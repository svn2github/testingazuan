/**
 * Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
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
 */

package it.eng.spagobi.drivers.bo;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.drivers.EngineURL;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import sun.misc.BASE64Encoder;



/**
 * Driver Implementation (IEngineDriver Interface) for BO Engine. 
 */
public class BODriver implements IEngineDriver {


	private static final String REPORTNAME = "REPORTNAME";
	private static final String REPOSITORY = "REPOSITORY";
	private static final String REPORTID = "REPORTID";
	private static final String OUTPUTTYPE = "OUTPUTTYPE";
	
    /**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @return Map The map of the execution call parameters
  	*/
	public Map getParameterMap(Object biobject){
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)biobject;
			SpagoBITracer.debug("ENGINES",
								this.getClass().getName(),
								"getParameterMap(Object)",
								"Start Extracting parameters from BIObject");	
			map = getMap(biobj);
			SpagoBITracer.debug("ENGINES",
								this.getClass().getName(),
								"getParameterMap(Object)",
								"End Extraction parameters from BIObject, the parameters map " +
								"which is going to be used for execution is \n" + map);
			traceParametersError(map);
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object)",
					"The parameter is not a BIObject type",
					cce);
		} 
		return map;
	}			
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @param profile Profile of the user 
	 * @param roleName the name of the execution role
	 * @return Map The map of the execution call parameters
	 */
	public Map getParameterMap(Object object, IEngUserProfile profile, String roleName){
		return getParameterMap(object);
	}
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @return Map The map of the execution call parameters
  	 */
	public Map getParameterMap(Object object, Object subObject){
		return getParameterMap(object);
	}
	/**
	 * Returns a map of parameters which will be send in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @param profile Profile of the user 
	 * @param roleName the name of the execution role
	 * @return Map The map of the execution call parameters
  	 */
	public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile, String roleName) {
		return getParameterMap(object, profile, roleName);
	}
    /**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @param profile Profile of the user 
	 * @return Map The map of the execution call parameters
  	 */
    public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile){
		return getParameterMap(object);
	}

    
        
        
    /**
     * Starting from a BIObject extracts from it the map of the paramaeters for the
     * execution call
     * @param biobj BIObject to execute
     * @return Map The map of the execution call parameters
     */    
	private Map getMap(BIObject biobj) {
		Map pars = new Hashtable();
		biobj.loadTemplate();
		UploadedFile uplFile = biobj.getTemplate();
		byte[] content = uplFile.getFileContent();
		BASE64Encoder encoder = new BASE64Encoder();
		String strContent64 = encoder.encode(content);
		pars.put("spagobi_template", strContent64);
		//pars.put("templatePath",biobj.getPath() + "/template");
        //pars.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
        pars = addBIParameters(biobj, pars);
        return pars;
	} 
 
         
    /**
     * Add into the parameters map the BIObject's BIParameter names and values
     * @param biobj BIOBject to execute
     * @param pars Map of the parameters for the execution call  
     * @return Map The map of the execution call parameters
     */
	private Map addBIParameters(BIObject biobj, Map pars) {
		if(biobj==null) {
			SpagoBITracer.warning("ENGINES",
								  this.getClass().getName(),
								  "addBIParameters",
								  "BIObject parameter null");
			return pars;
		}
		if(biobj.getBiObjectParameters() != null){
			BIObjectParameter biobjPar = null;
			for (Iterator it = biobj.getBiObjectParameters().iterator(); it.hasNext();){
				try {
					biobjPar = (BIObjectParameter) it.next();
					String value = "";
					for(int i = 0; i < biobjPar.getParameterValues().size(); i++)
						value += (i>0?",":"") + (String)biobjPar.getParameterValues().get(i);
					pars.put(biobjPar.getParameterUrlName(), value);
				} catch (Exception e) {
					SpagoBITracer.warning("ENGINES",
										  this.getClass().getName(),
										  "addBIParameters",
										  "Error while processing a BIParameter",
										  e);
				}
			}
		}
  		return pars;
	}
	
	
	/**
	 * Controls the parameters, looking for missing or wrong values, and logs error/warning
	 * @param pars Map of the execution parameters
	 */
	private void traceParametersError(Map pars) {
		// control mandatory REPORTNAME parameter
		/*String repName = (String)pars.get(REPORTNAME);
	    if((repName==null) || (repName.trim().equals(""))) {
	    	SpagoBITracer.critical("ENGINES",
					  this.getClass().getName(),
					  "traceParametersError",
					  "Cannot find REPORTNAME parameter: it's mandatory");
	    }
	    // control mandatory REPORTID parameter
	    String repID = (String)pars.get(REPORTID);
	    if((repID==null) || (repID.trim().equals(""))) {
	    	SpagoBITracer.critical("ENGINES",
					  this.getClass().getName(),
					  "traceParametersError",
					  "Cannot find REPORTID parameter: it's mandatory");
	    }
	    // control REPOSITORY parameter
	    String repository = (String)pars.get(REPOSITORY);
	    if((repository==null) || (repository.trim().equals(""))) {
	    	SpagoBITracer.warning("ENGINES",
					  this.getClass().getName(),
					  "traceParametersError",
					  "Cannot find REPOSITORY parameter: the default value corporate will be used");
	    } else if (!repository.equals("corporate") || 
	    		   !repository.equals("personal") ||
	    		   !repository.equals("inbox")) {
	    	SpagoBITracer.warning("ENGINES",
					  this.getClass().getName(),
					  "traceParametersError",
					  "The value of the REPOSITORY parameter is wrong: " +
					  "the possible values are " +
					  "corporate|personal|inbox");
	    }*/
        // control OUTPUTTYPE  parameter
	    String out = (String)pars.get(OUTPUTTYPE);
	    if((out==null) || (out.trim().equals(""))) {
	    	SpagoBITracer.warning("ENGINES",
					  this.getClass().getName(),
					  "traceParametersError",
					  "Cannot find OUTPUTTYPE parameter: the default value HTML will be used");
	    } else if (!out.equals("HTML") || 
	    		   !out.equals("XSL") ||
	    		   !out.equals("PDF")) {
	    	SpagoBITracer.warning("ENGINES",
					  this.getClass().getName(),
					  "traceParametersError",
					  "The value of the OUTPUTTYPE parameter is wrong: " +
					  "the possible values are " +
					  "HTML|PDF|XSL");
	    }
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param biobject The BIOBject to edit
	 * @throws InvalidOperationRequest
	 */
	public EngineURL getEditDocumentTemplateBuildUrl(Object biobject) throws InvalidOperationRequest {
		SpagoBITracer.major("ENGINES",
				  this.getClass().getName(),
				  "getEditDocumentTemplateBuildUrl",
				  "Function not implemented");
		throw new InvalidOperationRequest();
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param biobject The BIOBject to edit
	 * @throws InvalidOperationRequest
	 */
	public EngineURL getNewDocumentTemplateBuildUrl(Object biobject) throws InvalidOperationRequest {
		SpagoBITracer.major("ENGINES",
				  this.getClass().getName(),
				  "getNewDocumentTemplateBuildUrl",
				  "Function not implemented");
		throw new InvalidOperationRequest();
	}
	
	
}

