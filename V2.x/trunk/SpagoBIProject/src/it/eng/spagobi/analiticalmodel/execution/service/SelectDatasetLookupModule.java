package it.eng.spagobi.analiticalmodel.execution.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.commons.services.DelegatedHibernateConnectionListService;

import org.apache.log4j.Logger;

public class SelectDatasetLookupModule extends AbstractBasicListModule {

	static private Logger logger = Logger.getLogger(SelectDatasetLookupModule.class);

		public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
			return DelegatedHibernateConnectionListService.getList(this, request, response);
		}
	
	}

