package it.eng.spagobi.managers;

import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;

import sun.misc.BASE64Encoder;

/**
 * Manages and implements utilities and task execution for BIObject notes functionality
 */
public class BIObjectNotesManager {

	
	/**
	 * Return an identifier for a specific execution. The identifier is composed using the
	 * parameters url names and values.
	 * @param biobj The biobject executed. The biobject must be filled with the parameter value 
	 * selected by the user
	 * @return String of the biobject execution identifier
	 */
	public String getExecutionIdentifier(BIObject biobj ) {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
				            "getExecutionIdentifier", 
				            "start method execution for biobject label " + biobj.getLabel());
		List biparvalues = null;
		BIObjectParameter biobjpar = null;
		String parUrlName = null;
		Iterator iterBiparValues = null;
		String identif = null;
		List biobjpars = null;
		Iterator iterBiobjPars = null;
		String parValueString = null;
		
		identif = "biobject=" + biobj.getLabel() + "&";
		biobjpars = biobj.getBiObjectParameters();
		iterBiobjPars = biobjpars.iterator();
		while(iterBiobjPars.hasNext()){
			biobjpar = (BIObjectParameter)iterBiobjPars.next();
			Parameter par = biobjpar.getParameter();
			if((par==null) || (!par.isFunctional())){
				continue;
			}
			parUrlName = biobjpar.getParameterUrlName();
		 	biparvalues = biobjpar.getParameterValues();
		 	iterBiparValues = biparvalues.iterator();
		 	parValueString = "";
		 	while(iterBiparValues.hasNext()){
		 		String value = iterBiparValues.next().toString();
		 		parValueString = parValueString + value;
		 		if(iterBiparValues.hasNext()){
		 			parValueString = parValueString + ",";
		 		}
		 	}
		 	identif = identif + parUrlName + "=" + parValueString;
		 	if(iterBiobjPars.hasNext()){
		 		identif = identif + "&";
		 	}
		}
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	                        "getExecutionIdentifier", "identifier produced : " + identif);
		BASE64Encoder encoder = new BASE64Encoder();
		
		String ecodedIdentif = "";
		int index = 0;
		while(index<identif.length()){
			String tmpStr = "";
			try{
				tmpStr = identif.substring(index, index + 10);
			} catch (Exception e) {
				tmpStr = identif.substring(index, identif.length());
			}
			String tmpEncoded = encoder.encode(tmpStr.getBytes());
			ecodedIdentif = ecodedIdentif + tmpEncoded;
			index = index + 10;
		}

		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	            "getExecutionIdentifier", "end method execution, returning encoded identifier: " + ecodedIdentif);
		return ecodedIdentif;
	}
	
}
