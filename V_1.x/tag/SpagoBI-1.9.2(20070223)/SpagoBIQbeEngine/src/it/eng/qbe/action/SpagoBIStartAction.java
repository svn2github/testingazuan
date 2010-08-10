package it.eng.qbe.action;

import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.SpagoBIInfo;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.utilities.ParametersDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;


/**
 * @author Andrea Gioia
 */
public class SpagoBIStartAction extends AbstractAction {
	
	//private static transient Logger logger = Logger.getLogger(SpagoBIStartAction.class);
	
	public static final String TEMPLATE = "template";
	public static final String DATA_SOURCE_DIALECT = "DIALECT";
	public static final String DATA_SOURCE_NAME = "JNDI_DS";
	public static final String DATAMART_PATH = "PATH";	
	public static final String QUERI_ID =  "queryId";
	public static final String SPAGOBI_USER =  "SPAGOBI_USER";
	public static final String SPAGOBI_URL =  "SPAGOBI_URL";
	public static final String SPAGOBI_PATH =  "SPAGOBI_PATH";
	public static final String SPAGOBI_COUNTRY =  "SPAGOBI_COUNTRY";
	public static final String SPAGOBI_LANGUAGE =  "SPAGOBI_LANGUAGE";
	
	
	private void addParToParMap(Map params, String parName, String parValue) {
		String newParValue;
		
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			for(int i = 0; i < values.size(); i++) {
				newParValue += (i>0?",":"");
				newParValue += values.get(i);
			}
			
		} else {
			newParValue = parValue;
		}
		
		params.put(parName, newParValue);
	}
	
	private Map getParametersMap(SourceBean request) {
		Map params = new HashMap();
		List attributes = request.getContainedAttributes();
		Iterator it = attributes.iterator();
		String parName = null;
		String parValue = null;
		Logger.debug(SpagoBIStartAction.class, this.getClass().getName() +":service:Reading request parameters...");
		while (it.hasNext()) {
			SourceBeanAttribute attribute = (SourceBeanAttribute)it.next();
			parName = attribute.getKey();
			parValue = (String)attribute.getValue();
			addParToParMap(params, parName, parValue);
			System.out.println(this.getClass().getName() +":service:Read " +
					"parameter [" + parName + "] with value [" + parValue + "] from request");
			Logger.debug(SpagoBIStartAction.class, this.getClass().getName() +":service:Read " +
					"parameter [" + parName + "] with value [" + parValue + "] from request");
		}
		return params;
	}
	
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer requestContainer = getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		
		
		
		Logger.debug(SpagoBIStartAction.class, this.getClass().getName() + ":service:Start processing a new request...");
		Map parameters = getParametersMap(request);
		Logger.debug(SpagoBIStartAction.class, this.getClass().getName() +":service:Request parameters read sucesfully" + parameters);
				
		try {
			String templatePath = (String)parameters.get("templatePath");
			String spagobiurl = (String)parameters.get("spagobiurl");
			String user = (String)parameters.get("user");
			//SpagoBIInfo spagoBIInfo = new SpagoBIInfo(templatePath, spagobiurl, user);
			//session.setAttribute("spagobi", spagoBIInfo);
			
			String template = (String)parameters.get(TEMPLATE);
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			template = new String(bASE64Decoder.decodeBuffer(template));
			System.out.println("Template: " + template);
			
			SourceBean templateSB = SourceBean.fromXMLString(template);
			SourceBean datamartSB = (SourceBean)templateSB.getAttribute("DATAMART");
			SourceBean datasourceSB = (SourceBean)templateSB.getAttribute("DATASOURCE");
						
			String dmName = (String)datamartSB.getAttribute("name");
			System.out.println("Path: " + dmName);
			
			String dsName = (String)datasourceSB.getAttribute("name");
			System.out.println("Data-Source: " + dsName);
			String dsDialect = (String)datasourceSB.getAttribute("dialect");
			System.out.println("Dialect: " + dsDialect);
						
			String queryId = (String)parameters.get("query");
			System.out.println("Query: " + queryId);
			
			String country = (String)parameters.get("country");
			System.out.println("country: " + country);
			
			String language = (String)parameters.get("language");
			System.out.println("language: " + language);
			
			response.setAttribute(SPAGOBI_USER, user);
			response.setAttribute(SPAGOBI_URL, spagobiurl);
			response.setAttribute(SPAGOBI_PATH, templatePath);
			response.setAttribute(DATA_SOURCE_DIALECT, dsDialect);
			response.setAttribute(DATA_SOURCE_NAME, dsName);
			response.setAttribute(DATAMART_PATH, dmName);
			response.setAttribute(QUERI_ID, queryId);
			response.setAttribute(SPAGOBI_COUNTRY, country);
			response.setAttribute(SPAGOBI_LANGUAGE, language);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
