/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.weka;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;
import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.EngineStartServletIOManager;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

/**
 * Process weka execution requests and returns bytes of the filled
 * reports
 */
public class WekaServlet extends AbstractEngineStartServlet {
	
	/**
	 * Logger component
	 */
	private static transient Logger logger = Logger.getLogger(WekaServlet.class);

	/**
	 * Input parameters map
	 */
	private Map params = null;
	
	
	
	public static final String PROCESS_ACTIVATED_MSG = "processActivatedMsg";
	public static final String PROCESS_NOT_ACTIVATED_MSG = "processNotActivatedMsg";
	
	protected AuditAccessUtils auditAccessUtils;
	
	
	public void doService( EngineStartServletIOManager servletIOManager ) throws SpagoBIEngineException {

		WekaEngineInstance engineInstance = null;
		
		Map env = servletIOManager.getEnv();
		String template = servletIOManager.getTemplateAsString();
				
		String message = null;
				
		try {
			
			engineInstance = WekaEngine.createInstance(template, env);
			engineInstance.start();
			message = servletIOManager.getLocalizedMessage("weka.correct.execution");
			//message = (String) params.get(PROCESS_ACTIVATED_MSG);
			logger.info(":service: Return the default waiting message");

		} catch (Exception e) {
			logger.error(":service: error while process startup", e);
			//message = (String) params.get(PROCESS_NOT_ACTIVATED_MSG);
			message = servletIOManager.getLocalizedMessage("an.unpredicted.error.occured");
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>\n");
		buffer.append("<head><title>Service Response</title></head>\n");
		buffer.append("<body>");
		buffer
				.append("<p style=\"text-align:center;font-size:13pt;font-weight:bold;color:#000033;\">");
		buffer.append(message);
		buffer.append("</p>");
		buffer.append("</body>\n");
		buffer.append("</html>\n");

		servletIOManager.getResponse().setContentLength(buffer.length());
		servletIOManager.getResponse().setContentType("text/html");
		PrintWriter writer;
		try {
			writer = servletIOManager.getResponse().getWriter();
			writer.print(buffer.toString());
			writer.flush();
		} catch (IOException e) {
			throw new SpagoBIRuntimeException("Impossible to write back response to client", e);
		}
		

		logger.info("service:Request processed");
	}

	
	/**
	 * @param params
	 * @param parName
	 * @param parValue
	 */
	private void addParToParMap(Map params, String parName, String parValue) {
		logger.debug("IN");
		String newParValue;
		
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			newParValue = (String)values.get(0);
			
		} else {
			newParValue = parValue;
		}
		
		params.put(parName, newParValue);
		logger.debug("OUT");
	}



	
	
	


}
