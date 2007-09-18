/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.datamart.provider.DatamartProviderFactory;
import it.eng.spagobi.geo.datamart.provider.IDatamartProvider;

/**
 * Spago Action which executes the map producing request  
 */
public class ShowDataDetailsAction extends AbstractHttpAction {
	
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		RequestContainer requestContainer = getRequestContainer();
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		MapConfiguration mapConfiguration = (MapConfiguration)sessionContainer.getAttribute("CONFIGURATION");
		IDatamartProvider datamartProvider = DatamartProviderFactory.getDatamartProvider(mapConfiguration.getDatamartProviderConfiguration());
				
		String featureDesc = (String)serviceRequest.getAttribute("featureValue");
		String targetLevel = mapConfiguration.getDatamartProviderConfiguration().getHierarchyLevel();
		String featureValue = featureDesc;
		if(featureValue.trim().startsWith(targetLevel + "_")) {
			featureValue = featureValue.substring(targetLevel.length()+1);
		}
		
		SourceBean result = datamartProvider.getDataDetails(featureValue);
		sessionContainer.setAttribute("RESULT_SET", result);
		sessionContainer.setAttribute("FEATURE_DESC", featureDesc);
	}
}