package it.eng.spagobi.tools.dataset.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.commons.services.DelegatedHibernateConnectionListService;

/**
 * Loads the datasource list
 */
public class ListDataSetModule extends AbstractBasicListModule {
	
	public static final String MODULE_PAGE = "ListDataSetPage";

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