package it.eng.spagobi.engines.chart.utils;



import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.dao.IModalitiesValueDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import org.apache.log4j.Logger;

public class LovAccessFunctions {

	private static transient Logger logger=Logger.getLogger(LovAccessFunctions.class);


	/**
	 * returns the result of a LOV 
	 * <p>
	 *	it is used both to get the value of the chart and to get its configuration parameters if there defined	  
	 * @param profile IEngUserProfile of the user
	 * @param lovLabel Label of the love to retrieve
	 */

	public static String getLovResult(IEngUserProfile profile, String lovLabel){
		String result = "";
		logger.debug("IN");
		try{
			// get the lov type
			//String typeLov = getLovType(lovLabel);
			// get the result
			if (profile == null) {
				result = GeneralUtilities.getLovResult(lovLabel);
			} else {
				result = GeneralUtilities.getLovResult(lovLabel, profile);
			}
		}	
		catch (Exception e) {
			logger.error("Error",e);
		}
		logger.debug("OUT");
		return result;
	}



	private static String getLovType(String lovName) {
		String toReturn = "";
		logger.debug("IN");
		try{
			IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
			ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(lovName);
			String type = lov.getITypeCd();
			toReturn = type;
		} catch (Exception e) {
			logger.error("Error while recovering type of lov " + lovName,e);
		}
		logger.debug("OUT");
		return toReturn;
	}

	
}
