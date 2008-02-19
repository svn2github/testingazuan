package it.eng.spagobi.tools.distributionlist.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.commons.services.DelegatedHibernateConnectionListService;

/**
 * Loads the distributionlist list
 */
public class ListDistributionListModule extends AbstractBasicListModule{
	
	public static final String MODULE_PAGE = "ListDistributionListPage";

	/**
	 * Gets the list
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * @return ListIFace 
	 */
	
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		return DelegatedHibernateConnectionListService.getList(this, request, response);
	} 


}
