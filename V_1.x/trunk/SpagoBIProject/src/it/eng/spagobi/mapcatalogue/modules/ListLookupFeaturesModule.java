package it.eng.spagobi.mapcatalogue.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.services.commons.DelegatedHibernateConnectionListService;
/**
 * Loads the lov lookup list
 * 
 * @author sulis
 */

public class ListLookupFeaturesModule extends AbstractBasicListModule {
	
	public static final String MODULE_PAGE = "FeaturesLookupPage";
	/**
	 * Class Constructor
	 *
	 */
	public ListLookupFeaturesModule() {
		super();
	} 
	/**
	 * Gets the list
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * @return ListIFace 
	 */
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		response.setAttribute("MAP_ID", request.getAttribute("MAP_ID"));
		return DelegatedHibernateConnectionListService.getList(this, request, response);
	} 

} 

