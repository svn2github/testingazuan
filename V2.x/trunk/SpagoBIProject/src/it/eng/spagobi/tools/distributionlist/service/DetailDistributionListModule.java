package it.eng.spagobi.tools.distributionlist.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spagobi.commons.constants.AdmintoolsConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.tools.distributionlist.bo.DistributionList;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
* @author Chiarelli Chiara (chiara.chiarelli@eng.it)
*/

public class DetailDistributionListModule extends AbstractModule {
	
	static private Logger logger = Logger.getLogger(DetailDistributionListModule.class);
	public static final String MOD_SAVE = "SAVE";
	public static final String MOD_SAVEBACK = "SAVEBACK";
	public final static String NAME_ATTR_LIST_DIALECTS = "dialects";

	private String modalita = "";
	
	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the insertion, updation or deletion methods
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		String message = (String) request.getAttribute("MESSAGEDET");
		logger.debug("begin of detail Distribution List service with message =" +message);
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				logger.debug("The message parameter is null");
				throw userError;
			}
			logger.debug("The message parameter is: " + message.trim());
			if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_SELECT)) {
				getDistributionList(request, response);
			} //else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_MOD)) {
			 // modifyDistributionList(request, SpagoBIConstants.DETAIL_MOD, response);
			//} 
		else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_NEW)) {
				newDistributionList(response);
			} //else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_INS)) {
			 //modifyDistributionList(request, SpagoBIConstants.DETAIL_INS, response);
		    //} 
		else if (message.trim().equalsIgnoreCase(SpagoBIConstants.DETAIL_DEL)) {
				deleteDistributionList(request, SpagoBIConstants.DETAIL_DEL, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}
	
	 
	
	/**
	 * Gets the detail of a data source choosed by the user from the 
	 * data sources list. It reaches the key from the request and asks to the DB all detail
	 * data source information, by calling the method <code>loadDataSourceByID</code>.
	 *   
	 * @param key The choosed data source id key
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */   
	private void getDistributionList(SourceBean request, SourceBean response) throws EMFUserError {		
		try {		 									
			DistributionList dl = DAOFactory.getDistributionListDAO().loadDistributionListById(new Integer((String)request.getAttribute("ID")));		
			this.modalita = SpagoBIConstants.DETAIL_MOD;
			if (request.getAttribute("SUBMESSAGEDET") != null &&
				((String)request.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVEBACK))
			{
				response.setAttribute("loopback", "true");
				return;
			}
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List dialects = domaindao.loadListDomainsByType("DIALECT_HIB");
			response.setAttribute(NAME_ATTR_LIST_DIALECTS, dialects);
			response.setAttribute("modality", modalita);
			response.setAttribute("dsObj", dl);
		} catch (Exception ex) {
			logger.error("Cannot fill response container" + ex.getLocalizedMessage());	
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListDistributionListModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8003, new Vector(), params);
		}
		
	}
	 /**
	 * Inserts/Modifies the detail of a data source according to the user request. 
	 * When a data source is modified, the <code>modifyDataSource</code> method is called; when a new
	 * data source is added, the <code>insertDataSource</code>method is called. These two cases are 
	 * differentiated by the <code>mod</code> String input value .
	 * 
	 * @param request The request information contained in a SourceBean Object
	 * @param mod A request string used to differentiate insert/modify operations
	 * @param response The response SourceBean 
	 * @throws EMFUserError If an exception occurs
	 * @throws SourceBeanException If a SourceBean exception occurs
	 */
	/*
	private void modifyDistributionList(SourceBean serviceRequest, String mod, SourceBean serviceResponse)
		throws EMFUserError, SourceBeanException {
		
		try {
			
			DataSource dsNew = recoverDataSourceDetails(serviceRequest);
			
			EMFErrorHandler errorHandler = getErrorHandler();
			 
			// if there are some validation errors into the errorHandler does not write into DB
			Collection errors = errorHandler.getErrors();
			if (errors != null && errors.size() > 0) {
				Iterator iterator = errors.iterator();
				while (iterator.hasNext()) {
					Object error = iterator.next();
					if (error instanceof EMFValidationError) {
						serviceResponse.setAttribute("dsObj", dsNew);
						serviceResponse.setAttribute("modality", mod);
						return;
					}
				}
			}
			
			if (mod.equalsIgnoreCase(SpagoBIConstants.DETAIL_INS)) {			
				//if a ds with the same label not exists on db ok else error
				if (DAOFactory.getDataSourceDAO().loadDataSourceByLabel(dsNew.getLabel()) != null){
					HashMap params = new HashMap();
					params.put(AdmintoolsConstants.PAGE, ListDataSourceModule.MODULE_PAGE);
					EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 8004, new Vector(), params );
					getErrorHandler().addError(error);
					return;
				}	 		
				 
				DAOFactory.getDataSourceDAO().insertDataSource(dsNew);
				
				DataSource tmpDS = DAOFactory.getDataSourceDAO().loadDataSourceByLabel(dsNew.getLabel());
				dsNew.setDsId(tmpDS.getDsId());
				mod = SpagoBIConstants.DETAIL_MOD; 
			} else {				
				//update ds
				DAOFactory.getDataSourceDAO().modifyDataSource(dsNew);			
			}  
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List dialects = domaindao.loadListDomainsByType("DIALECT_HIB");
			serviceResponse.setAttribute(NAME_ATTR_LIST_DIALECTS, dialects);
			
			if (serviceRequest.getAttribute("SUBMESSAGEDET") != null && 
				((String)serviceRequest.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVE)) {	
				serviceResponse.setAttribute("modality", mod);
				serviceResponse.setAttribute("dsObj", dsNew);				
				return;
			}
			else if (serviceRequest.getAttribute("SUBMESSAGEDET") != null && 
					((String)serviceRequest.getAttribute("SUBMESSAGEDET")).equalsIgnoreCase(MOD_SAVEBACK)){
					serviceResponse.setAttribute("loopback", "true");
				    return;
			}					     
		} catch (EMFUserError e){
			logger.error("Cannot fill response container" + e.getLocalizedMessage());
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, ListDataSourceModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005, new Vector(), params);
			
		}
		
		catch (Exception ex) {		
			logger.error("Cannot fill response container" , ex);		
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}			
	}*/

	/**
	 * Deletes a data source choosed by user from the data sources list.
	 * 
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void deleteDistributionList(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		
		try {
			String id = (String) request.getAttribute("DL_ID");
			//TODO controllare che non ci siano associate altre cose...
			/*boolean bObjects =  DAOFactory.getDistributionListDAO().hasBIObjAssociated(id);
			boolean bEngines =  DAOFactory.getDistributionListDAO().hasBIEngineAssociated(id);
			if (bObjects || bEngines){
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, ListDistributionListModule.MODULE_PAGE);
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 8007, new Vector(), params );
				getErrorHandler().addError(error);
				return;
			}*/
			
			//delete the dl
			DistributionList dl = DAOFactory.getDistributionListDAO().loadDistributionListById(new Integer(id));
			DAOFactory.getDistributionListDAO().eraseDistributionList(dl);
		}
		catch (EMFUserError e){
			  logger.error("Cannot fill response container" + e.getLocalizedMessage());
			  HashMap params = new HashMap();		  
			  params.put(AdmintoolsConstants.PAGE, ListDistributionListModule.MODULE_PAGE);
			  throw new EMFUserError(EMFErrorSeverity.ERROR, 8006, new Vector(), params);
				
		}
	    catch (Exception ex) {		
		    ex.printStackTrace();
			logger.error("Cannot fill response container" ,ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	    }
	    response.setAttribute("loopback", "true");			
	}



	/**
	 * Instantiates a new <code>datasource<code> object when a new data source insertion is required, in order
	 * to prepare the page for the insertion.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */

	private void newDistributionList(SourceBean response) throws EMFUserError {
		
		try {
			
			DistributionList dl = null;
			this.modalita = SpagoBIConstants.DETAIL_INS;
			response.setAttribute("modality", modalita);
			dl = new DistributionList();
			dl.setId(-1);
			dl.setDescr("");
			dl.setName("");
			dl.setEmails(null);
			response.setAttribute("dlObj", dl);
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List dialects = domaindao.loadListDomainsByType("DIALECT_HIB");
			response.setAttribute(NAME_ATTR_LIST_DIALECTS, dialects);
		} catch (Exception ex) {
			logger.error("Cannot prepare page for the insertion" , ex);		
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		
	}

/*
	private DataSource recoverDataSourceDetails (SourceBean serviceRequest) throws EMFUserError, SourceBeanException, IOException  {
		DataSource ds  = new DataSource();
		
		String idStr = (String)serviceRequest.getAttribute("ID");
		Integer id = new Integer(idStr);
		Integer dialectId = Integer.valueOf((String)serviceRequest.getAttribute("DIALECT"));	
		String description = (String)serviceRequest.getAttribute("DESCR");	
		String label = (String)serviceRequest.getAttribute("LABEL");
		String jndi = (String)serviceRequest.getAttribute("JNDI");
		String url = (String)serviceRequest.getAttribute("URL_CONNECTION");
		String user = (String)serviceRequest.getAttribute("USER");
		String pwd = (String)serviceRequest.getAttribute("PWD");
		String driver = (String)serviceRequest.getAttribute("DRIVER");
		
		ds.setDsId(id.intValue());
		ds.setDialectId(dialectId);
		ds.setLabel(label);
		ds.setDescr(description);
		ds.setJndi(jndi);
		ds.setUrlConnection(url);
		ds.setUser(user);
		ds.setPwd(pwd);
		ds.setDriver(driver);
				
		return ds;
	}*/

}
