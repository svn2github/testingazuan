/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.utilities.GenericSavingException;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

/**
 * Spago Action which executes the map producing request  
 */
public class SaveAnalysisAction extends AbstractHttpAction {	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		GeoAction.SubObjectDetails subObjectDetails = (GeoAction.SubObjectDetails)getRequestContainer().getSessionContainer().getAttribute("SUBOBJECT");
		
			
		String name = (String)serviceRequest.getAttribute("name");
		String description = (String)serviceRequest.getAttribute("description");
		String scope = (String)serviceRequest.getAttribute("scope");
		
		subObjectDetails.setName(name);
		subObjectDetails.setDescription(description);
		subObjectDetails.setScope(scope);
		
		System.out.println(new String(subObjectDetails.getData()));
		
		SpagoBIAccessUtils spagoBIProxy = new SpagoBIAccessUtils();
		try {
			spagoBIProxy.saveSubObject(subObjectDetails.getSpagobiurl(), 
				  					   subObjectDetails.getTemplatePath(), 
									   subObjectDetails.getName(), 
									   subObjectDetails.getDescription(), 
									   subObjectDetails.getUser(), 
									   subObjectDetails.getScope().equalsIgnoreCase("public"), 
									   subObjectDetails.getData() == null?"": new String(subObjectDetails.getData()));
		} catch (GenericSavingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}