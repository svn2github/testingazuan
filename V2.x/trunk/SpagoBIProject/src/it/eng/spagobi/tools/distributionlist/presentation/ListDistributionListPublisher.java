package it.eng.spagobi.tools.distributionlist.presentation;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.presentation.PublisherDispatcherIFace;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import org.apache.log4j.Logger;
/**
* @author Chiarelli Chiara (chiara.chiarelli@eng.it)
*/

/**
 * Publishes the results of a list information request for distributionlists
 * into the correct jsp page according to what contained into the request. If any error occurred during the 
 * execution of the <code>ListDistributionListModule</code> class, the publisher
 * is able to call the error page with the error message caught before and put into 
 * the error handler. If the input information don't fall into any of the cases declared,
 * another error is generated. 
 */
public class ListDistributionListPublisher implements PublisherDispatcherIFace{
	
	static private Logger logger = Logger.getLogger(ListDistributionListPublisher.class);
	/**
	 *Given the request at input, gets the name of the reference publisher,driving
	 * the execution into the correct jsp page, or jsp error page, if any error occurred.
	 * 
	 * @param requestContainer The object containing all request information
	 * @param responseContainer The object containing all response information
	 * @return A string representing the name of the correct publisher, which will
	 * 		   call the correct jsp reference.
	 */
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		logger.debug("IN");
		EMFErrorHandler errorHandler = responseContainer.getErrorHandler();
		
		// if there are errors and they are only validation errors return the name for the detail publisher
		if(!errorHandler.isOK()) {
			if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {				
				logger.info("Publish: listDistributionList"  );
				logger.debug("OUT");
				return "listDistributionList";
			}
		}
		
		if (errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)){
			logger.info("Publish: listDistributionList"  );
			logger.debug("OUT");			
			return new String("listDistributionList");
		}
		else {
			logger.info("Publish: error"  );
			logger.debug("OUT");
			return new String("error");
		}
	}


}
