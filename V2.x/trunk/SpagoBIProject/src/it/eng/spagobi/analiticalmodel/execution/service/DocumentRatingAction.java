package it.eng.spagobi.analiticalmodel.execution.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.BaseProfileAction;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

public class DocumentRatingAction extends BaseProfileAction{
	
	 private static transient Logger logger = Logger.getLogger(DocumentRatingAction.class);
	 
	 public void service(SourceBean request, SourceBean response) throws Exception {
	    	
	    	//Check of the userId in order to keep performing the request
			super.service(request, response);
			
			logger.debug("IN");
			
			String message = (String) request.getAttribute("MESSAGEDET");
			
			EMFErrorHandler errorHandler = getErrorHandler();
			try {
				if (message == null) {
					EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
					logger.debug("The message parameter is null");
					throw userError;
				}
				logger.debug("The message parameter is: " + message.trim());
				if (message.trim().equalsIgnoreCase("GOTO_DOCUMENT_RATE")) {
					goToDocumentRating(request, "GOTO_DOCUMENT_RATE", response);
				} 
				else if (message.trim().equalsIgnoreCase("DOCUMENT_RATE")) {
					documentRating(request, "DOCUMENT_RATE", response);
					} 
			} catch (EMFUserError eex) {
				errorHandler.addError(eex);
				return;
			} catch (Exception ex) {
				EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
				errorHandler.addError(internalError);
				return;
			}
		
		logger.debug("OUT");
	    }
	 
		private void goToDocumentRating(SourceBean request, String mod, SourceBean response) throws EMFUserError, SourceBeanException  {
			
			String objId= (String)request.getAttribute("OBJECT_ID");
			String userId= (String)request.getAttribute("userId");
			
			response.setAttribute("userId", userId);
			response.setAttribute("OBJECT_ID", objId);
			response.setAttribute("MESSAGEDET", mod);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ratingBIObjectPubJ");
		
		}
		
		private void documentRating(SourceBean request, String mod, SourceBean response) throws EMFUserError, SourceBeanException  {
			
			String objId = "";
			String rating = "";
			String userId= (String)request.getAttribute("userid");
			List params = request.getContainedAttributes();
		    ListIterator it = params.listIterator();

		    while (it.hasNext()) {

			Object par = it.next();
			SourceBeanAttribute p = (SourceBeanAttribute) par;
			String parName = (String) p.getKey();
			logger.debug("got parName=" + parName);
			if (parName.equals("OBJECT_ID")) {
			    objId = (String) request.getAttribute("OBJECT_ID");
			    logger.debug("got OBJECT_ID from Request=" + objId);
				} 
			else if(parName.equals("RATING")){
				rating = (String)request.getAttribute("RATING");
			}
		    }
		    
		    if (objId != null && !objId.equals("")){
		    	if (rating != null && !rating.equals("")){
					//VOTE!
		    		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(new Integer(objId));
					DAOFactory.getBIObjectRatingDAO().voteBIObject(obj, userId, rating);
		       }
		     }
		   
		    response.setAttribute("MESSAGEDET", mod);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ratingBIObjectPubJ");
			
		 }
}
