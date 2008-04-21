/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;

/**
 * Spago Action which executes the map producing request  
 */
public class ExitAnalysisDetailsAction extends AbstractHttpAction {
	
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		System.out.println("ExitAnalysisDetailsAction");
	}
	

}